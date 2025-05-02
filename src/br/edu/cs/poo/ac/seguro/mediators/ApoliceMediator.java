package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.PrecoAno;
import br.edu.cs.poo.ac.seguro.entidades.Segurado;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

public class ApoliceMediator {
    private static ApoliceMediator instancia;

    private SeguradoPessoaDAO daoSegPes = new SeguradoPessoaDAO();
    private SeguradoEmpresaDAO daoSegEmp = new SeguradoEmpresaDAO();
    private VeiculoDAO daoVel = new VeiculoDAO();
    private ApoliceDAO daoApo = new ApoliceDAO();
    private SinistroDAO daoSin = new SinistroDAO();
    private SeguradoPessoaMediator segPessoaMediator = SeguradoPessoaMediator.getInstancia();
    private SeguradoEmpresaMediator segEmpresaMediator = SeguradoEmpresaMediator.getInstancia();

    private ApoliceMediator() {}

    public static ApoliceMediator getInstancia() {
        if (instancia == null) {
            instancia = new ApoliceMediator();
        }
        return instancia;
    }

    public RetornoInclusaoApolice incluirApolice(DadosVeiculo dados) {
        // 1. Validação inicial dos dados recebidos
        if (dados == null) {
            return new RetornoInclusaoApolice(null, "Dados do veículo devem ser informados");
        }
        if (StringUtils.ehNuloOuBranco(dados.getPlaca())) {
            return new RetornoInclusaoApolice(null, "Placa do veículo deve ser informada");
        }

        // 2. Validação de CPF/CNPJ, Ano, Categoria e Valor Máximo
        //    (A ordem foi ajustada para validar ano e categoria antes de buscar preço)
        String erroValidacaoInicial = validarCpfCnpjAnoCategoriaValorMaximo(dados);
        if (erroValidacaoInicial != null) {
            return new RetornoInclusaoApolice(null, erroValidacaoInicial);
        }

        // 3. Busca o veículo pela placa
        Veiculo veiculo = daoVel.buscar(dados.getPlaca());
        boolean veiculoExistia = (veiculo != null);



        // 5. Busca o segurado (Pessoa ou Empresa)
        Segurado segurado = null;
        boolean ehPessoa = dados.getCpfOuCnpj().length() == 11;
        if (ehPessoa) {
            segurado = daoSegPes.buscar(dados.getCpfOuCnpj());
            if (segurado == null) {
                return new RetornoInclusaoApolice(null, "CPF inexistente no cadastro de pessoas");
            }
        } else {
            segurado = daoSegEmp.buscar(dados.getCpfOuCnpj());
            if (segurado == null) {
                return new RetornoInclusaoApolice(null, "CNPJ inexistente no cadastro de empresas");
            }
        }

        // 6. Gera o número da apólice
        int anoAtual = LocalDate.now().getYear();
        String numeroApolice;
        if (ehPessoa) {
            numeroApolice = anoAtual + "000" + dados.getCpfOuCnpj() + dados.getPlaca();
        } else {
            numeroApolice = anoAtual + dados.getCpfOuCnpj() + dados.getPlaca();
        }

        // 7. Verifica se já existe apólice para este ano e veículo
        if (daoApo.buscar(numeroApolice) != null) {
            return new RetornoInclusaoApolice(null, "Apólice já existente para ano atual e veículo");
        }

        // 8. Prepara ou atualiza o veículo
        CategoriaVeiculo categoria = CategoriaVeiculo.values()[dados.getCodigoCategoria() - 1];
        if (!veiculoExistia) {
            veiculo = new Veiculo();
            veiculo.setPlaca(dados.getPlaca());
            veiculo.setAno(dados.getAno());
            veiculo.setCategoria(categoria);
            if (ehPessoa) {
                veiculo.setProprietarioPessoa((SeguradoPessoa) segurado);
                veiculo.setProprietarioEmpresa(null);
            } else {
                veiculo.setProprietarioEmpresa((SeguradoEmpresa) segurado);
                veiculo.setProprietarioPessoa(null);
            }
            daoVel.incluir(veiculo);
        } else {
            if (ehPessoa) {
                veiculo.setProprietarioPessoa((SeguradoPessoa) segurado);
                veiculo.setProprietarioEmpresa(null);
            } else {
                veiculo.setProprietarioEmpresa((SeguradoEmpresa) segurado);
                veiculo.setProprietarioPessoa(null);
            }
            // O ano e categoria do veículo existente não são alterados aqui,
            // apenas o proprietário é atualizado.
            daoVel.alterar(veiculo);
        }

        // 9. Calcula valor do prêmio e franquia
        BigDecimal cem = new BigDecimal("100.0");
        BigDecimal tresPorcento = new BigDecimal("3.0");
        BigDecimal umPontoDois = new BigDecimal("1.2");
        BigDecimal umPontoTres = new BigDecimal("1.3");
        BigDecimal dez = new BigDecimal("10.0");

        BigDecimal valorMaximoSeguradoAjustado = dados.getValorMaximoSegurado().setScale(2, RoundingMode.HALF_UP);

        BigDecimal vpa = valorMaximoSeguradoAjustado.multiply(tresPorcento).divide(cem, 2, RoundingMode.HALF_UP);
        BigDecimal vpb;
        if (!ehPessoa && ((SeguradoEmpresa) segurado).isEhLocadoraDeVeiculos()) {
            vpb = vpa.multiply(umPontoDois).setScale(2, RoundingMode.HALF_UP);
        } else {
            vpb = vpa;
        }

        BigDecimal bonusSegurado = (segurado.getBonus() == null) ? BigDecimal.ZERO : segurado.getBonus();
        BigDecimal vpc = vpb.subtract(bonusSegurado.divide(dez, 2, RoundingMode.HALF_UP));

        BigDecimal valorPremio;
        if (vpc.compareTo(BigDecimal.ZERO) > 0) {
            valorPremio = vpc.setScale(2, RoundingMode.HALF_UP);
        } else {
            valorPremio = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal valorFranquia = vpb.multiply(umPontoTres).setScale(2, RoundingMode.HALF_UP);

        // 10. Cria e inclui a apólice
        LocalDate dataInicioVigencia = LocalDate.now();
        Apolice apolice = new Apolice(numeroApolice, veiculo, valorFranquia, valorPremio, valorMaximoSeguradoAjustado, dataInicioVigencia);
        daoApo.incluir(apolice);

        // 11. Bonificação (se aplicável)
        boolean teveSinistroAnoAnterior = false;
        int anoAnterior = dataInicioVigencia.getYear() - 1;
        Sinistro[] todosSinistros = daoSin.buscarTodos();

        for (Sinistro sin : todosSinistros) {
            if (sin.getDataHoraSinistro().getYear() == anoAnterior) {
                Veiculo veiculoSinistro = sin.getVeiculo();
                if (veiculoSinistro != null) {
                    if (ehPessoa && veiculoSinistro.getProprietarioPessoa() != null &&
                            veiculoSinistro.getProprietarioPessoa().getCpf().equals(dados.getCpfOuCnpj())) {
                        teveSinistroAnoAnterior = true;
                        break;
                    } else if (!ehPessoa && veiculoSinistro.getProprietarioEmpresa() != null &&
                            veiculoSinistro.getProprietarioEmpresa().getCnpj().equals(dados.getCpfOuCnpj())) {
                        teveSinistroAnoAnterior = true;
                        break;
                    }
                }
            }
        }

        if (!teveSinistroAnoAnterior) {
            BigDecimal bonusCredito = valorPremio.multiply(new BigDecimal("0.30")).setScale(2, RoundingMode.HALF_UP);
            if (bonusCredito.compareTo(BigDecimal.ZERO) > 0) {
                if (ehPessoa) {
                    SeguradoPessoa sp = daoSegPes.buscar(dados.getCpfOuCnpj());
                    if (sp != null) {
                        sp.creditarBonus(bonusCredito);
                        daoSegPes.alterar(sp);
                    }
                } else {
                    SeguradoEmpresa se = daoSegEmp.buscar(dados.getCpfOuCnpj());
                    if (se != null) {
                        se.creditarBonus(bonusCredito);
                        daoSegEmp.alterar(se);
                    }
                }
            }
        }

        // 12. Retorna sucesso
        return new RetornoInclusaoApolice(numeroApolice, null);
    }

    public Apolice buscarApolice(String numero) {
        if (StringUtils.ehNuloOuBranco(numero)) {
            return null;
        }
        return daoApo.buscar(numero);
    }

    public String excluirApolice(String numero) {
        if (StringUtils.ehNuloOuBranco(numero)) {
            return "Número deve ser informado";
        }

        Apolice apolice = daoApo.buscar(numero);
        if (apolice == null) {
            return "Apólice inexistente";
        }

        int anoApolice = apolice.getDataInicioVigencia().getYear();
        Veiculo veiculoApolice = apolice.getVeiculo();
        Sinistro[] todosSinistros = daoSin.buscarTodos();

        for (Sinistro sin : todosSinistros) {
            if (sin.getDataHoraSinistro().getYear() == anoApolice &&
                    sin.getVeiculo() != null && sin.getVeiculo().equals(veiculoApolice)) {
                return "Existe sinistro cadastrado para o veículo em questão e no mesmo ano da apólice";
            }
        }

        boolean excluiu = daoApo.excluir(numero);
        if (excluiu) {
            return null;
        } else {
            return "Erro ao excluir apólice";
        }
    }

    // --- Métodos Privados Sugeridos (Ajustados) ---

    private String validarCpfCnpjAnoCategoriaValorMaximo(DadosVeiculo dados) {
        // Valida CPF ou CNPJ
        if (StringUtils.ehNuloOuBranco(dados.getCpfOuCnpj())) {
            return "CPF ou CNPJ deve ser informado";
        }
        boolean ehCpf = dados.getCpfOuCnpj().length() == 11;
        boolean ehCnpj = dados.getCpfOuCnpj().length() == 14;

        if (ehCpf) {
            if (!ValidadorCpfCnpj.ehCpfValido(dados.getCpfOuCnpj())) {
                return "CPF inválido";
            }
        } else if (ehCnpj) {
            if (!ValidadorCpfCnpj.ehCnpjValido(dados.getCpfOuCnpj())) {
                return "CNPJ inválido";
            }
        } else {
            return "CPF ou CNPJ inválido";
        }

        // Valida Ano
        String erroAno = validarAno(dados.getAno());
        if (erroAno != null) {
            return erroAno;
        }

        // Valida Categoria
        String erroCat = validarCodigoCategoria(dados.getCodigoCategoria());
        if (erroCat != null) {
            return erroCat;
        }

        // Valida Valor Máximo Segurado (null/positive check)
        if (dados.getValorMaximoSegurado() == null) {
            return "Valor máximo segurado deve ser informado";
        }
        if (dados.getValorMaximoSegurado().compareTo(BigDecimal.ZERO) <= 0) {
            return "Valor máximo segurado deve ser maior que zero";
        }

        // Valida se o valor está dentro do range permitido pela categoria/ano
        // Agora que ano e categoria são válidos, podemos buscar o preço
        BigDecimal valorMaximoPermitido = obterValorMaximoPermitido(dados.getAno(), dados.getCodigoCategoria());

        if (valorMaximoPermitido == null) {
            // Se ainda assim for nulo, indica um problema interno (não deveria acontecer com dados válidos)
            return "Valor de referência não encontrado para o ano e categoria informados";
        }

        BigDecimal limiteInferior = valorMaximoPermitido.multiply(new BigDecimal("0.75")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal limiteSuperior = valorMaximoPermitido;

        if (dados.getValorMaximoSegurado().compareTo(limiteInferior) < 0 ||
                dados.getValorMaximoSegurado().compareTo(limiteSuperior) > 0) {
            return "Valor máximo segurado deve estar entre 75% e 100% do valor do carro encontrado na categoria";
        }

        return null; // Todas as validações iniciais passaram
    }

    private String validarAno(int ano) {
        if (ano < 2020 || ano > 2025) {
            return "Ano tem que estar entre 2020 e 2025, incluindo estes";
        }
        return null;
    }

    private String validarCodigoCategoria(int codigoCategoria) {
        if (codigoCategoria < 1 || codigoCategoria > CategoriaVeiculo.values().length) {
            return "Categoria inválida";
        }
        return null;
    }

    private BigDecimal obterValorMaximoPermitido(int ano, int codigoCat) {
        try {
            CategoriaVeiculo categoria = CategoriaVeiculo.values()[codigoCat - 1];
            for (PrecoAno pa : categoria.getPrecosAnos()) {
                if (pa.getAno() == ano) {
                    return BigDecimal.valueOf(pa.getPreco()).setScale(2, RoundingMode.HALF_UP);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Segurança caso a validação falhe por algum motivo
            return null;
        }
        return null; // Não encontrou preço para o ano específico na categoria
    }
}
