package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

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

    private ApoliceMediator() {}

    public static ApoliceMediator getInstancia() {
        if (instancia == null) {
            instancia = new ApoliceMediator();
        }
        return instancia;
    }

    public RetornoInclusaoApolice incluirApolice(DadosVeiculo dados) {
        if (dados == null) {
            return new RetornoInclusaoApolice(null, "Dados do ve�culo devem ser informados");
        }
        if (StringUtils.ehNuloOuBranco(dados.getPlaca())) {
            return new RetornoInclusaoApolice(null, "Placa do ve�culo deve ser informada");
        }

        String erroValidacaoInicial = validarCpfCnpjAnoCategoriaValorMaximo(dados);
        if (erroValidacaoInicial != null) {
            return new RetornoInclusaoApolice(null, erroValidacaoInicial);
        }

        Veiculo veiculo = daoVel.buscar(dados.getPlaca());
        boolean veiculoExistia = (veiculo != null);

        Segurado segurado;
        String cpfOuCnpjLimpo = dados.getCpfOuCnpj().replaceAll("[^0-9]", "");
        boolean ehPessoa = cpfOuCnpjLimpo.length() == 11;


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

        int anoAtual = LocalDate.now().getYear();
        String numeroApolice;
        if (segurado.isEmpresa()) {
            numeroApolice = anoAtual + segurado.getIdUnico() + dados.getPlaca();
        } else {
            numeroApolice = anoAtual + "000" + segurado.getIdUnico() + dados.getPlaca();
        }


        if (daoApo.buscar(numeroApolice) != null) {
            return new RetornoInclusaoApolice(null, "Ap�lice j� existente para ano atual e ve�culo");
        }

        CategoriaVeiculo categoria = CategoriaVeiculo.values()[dados.getCodigoCategoria() - 1];
        if (!veiculoExistia) {
            veiculo = new Veiculo();
            veiculo.setPlaca(dados.getPlaca());
            veiculo.setAno(dados.getAno());
            veiculo.setCategoria(categoria);
            veiculo.setProprietario(segurado);
            daoVel.incluir(veiculo);
        } else {
            veiculo.setProprietario(segurado);
            daoVel.alterar(veiculo);
        }

        BigDecimal cem = new BigDecimal("100.0");
        BigDecimal tresPorcento = new BigDecimal("3.0");
        BigDecimal umPontoDois = new BigDecimal("1.2");
        BigDecimal umPontoTres = new BigDecimal("1.3");
        BigDecimal dez = new BigDecimal("10.0");

        BigDecimal valorMaximoSeguradoAjustado = dados.getValorMaximoSegurado().setScale(2, RoundingMode.HALF_UP);

        BigDecimal vpa = valorMaximoSeguradoAjustado.multiply(tresPorcento).divide(cem, 2, RoundingMode.HALF_UP);
        BigDecimal vpb;

        if (segurado.isEmpresa() && ((SeguradoEmpresa) segurado).isEhLocadoraDeVeiculos()) {
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

        LocalDate dataInicioVigencia = LocalDate.now();
        Apolice apolice = new Apolice(numeroApolice, veiculo, valorFranquia, valorPremio, valorMaximoSeguradoAjustado, dataInicioVigencia);
        daoApo.incluir(apolice);

        boolean teveSinistroAnoAnterior = false;
        int anoAnterior = dataInicioVigencia.getYear() - 1;
        br.edu.cs.poo.ac.seguro.entidades.Registro[] todosSinistrosRegistros = daoSin.buscarTodos();


        for (br.edu.cs.poo.ac.seguro.entidades.Registro regSinistro : todosSinistrosRegistros) {
            if (regSinistro instanceof Sinistro) {
                Sinistro sin = (Sinistro) regSinistro;
                if (sin.getDataHoraOcorrencia().getYear() == anoAnterior) {
                    Veiculo veiculoSinistro = sin.getVeiculo();
                    if (veiculoSinistro != null && veiculoSinistro.getProprietario() != null) {
                        if (veiculoSinistro.getProprietario().getIdUnico().equals(segurado.getIdUnico())) {
                            teveSinistroAnoAnterior = true;
                            break;
                        }
                    }
                }
            }
        }


        if (!teveSinistroAnoAnterior) {
            BigDecimal bonusCredito = valorPremio.multiply(new BigDecimal("0.30")).setScale(2, RoundingMode.HALF_UP);
            if (bonusCredito.compareTo(BigDecimal.ZERO) > 0) {
                segurado.creditarBonus(bonusCredito);
                if (segurado.isEmpresa()) {
                    daoSegEmp.alterar(segurado);
                } else {
                    daoSegPes.alterar((SeguradoPessoa) segurado);
                }
            }
        }

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
            return "N�mero deve ser informado";
        }

        Apolice apolice = daoApo.buscar(numero);
        if (apolice == null) {
            return "Ap�lice inexistente";
        }

        int anoApolice = apolice.getDataInicioVigencia().getYear();
        Veiculo veiculoApolice = apolice.getVeiculo();

        br.edu.cs.poo.ac.seguro.entidades.Registro[] todosSinistrosRegistros = daoSin.buscarTodos();

        for (br.edu.cs.poo.ac.seguro.entidades.Registro regSinistro : todosSinistrosRegistros) {
            if (regSinistro instanceof Sinistro) {
                Sinistro sin = (Sinistro) regSinistro;
                if (sin.getVeiculo() != null && sin.getVeiculo().getPlaca().equals(veiculoApolice.getPlaca()) &&
                        sin.getDataHoraOcorrencia().getYear() == anoApolice) {
                    return "Existe sinistro cadastrado para o ve�culo em quest�o e no mesmo ano da ap�lice";
                }
            }
        }


        boolean excluiu = daoApo.excluir(numero);
        if (excluiu) {
            return null;
        } else {
            return "Erro ao excluir apólice";
        }
    }

    private String validarCpfCnpjAnoCategoriaValorMaximo(DadosVeiculo dados) {
        if (StringUtils.ehNuloOuBranco(dados.getCpfOuCnpj())) {
            return "CPF ou CNPJ deve ser informado";
        }

        String cpfOuCnpjLimpo = dados.getCpfOuCnpj().replaceAll("[^0-9]", "");
        boolean ehTipoCpf = cpfOuCnpjLimpo.length() == 11;
        boolean ehTipoCnpj = cpfOuCnpjLimpo.length() == 14;

        if (ehTipoCpf) {
            if (!ValidadorCpfCnpj.ehCpfValido(dados.getCpfOuCnpj())) { // Passa o valor original para validação
                return "CPF inv�lido";
            }
        } else if (ehTipoCnpj) {
            if (!ValidadorCpfCnpj.ehCnpjValido(dados.getCpfOuCnpj())) { // Passa o valor original para validação
                return "CNPJ inv�lido";
            }
        } else {
            return "CPF ou CNPJ com formato/comprimento inv�lido";
        }

        String erroAno = validarAno(dados.getAno());
        if (erroAno != null) {
            return erroAno;
        }

        String erroCat = validarCodigoCategoria(dados.getCodigoCategoria());
        if (erroCat != null) {
            return erroCat;
        }

        if (dados.getValorMaximoSegurado() == null) {
            return "Valor m�ximo segurado deve ser informado";
        }
        if (dados.getValorMaximoSegurado().compareTo(BigDecimal.ZERO) <= 0) {
            return "Valor m�ximo segurado deve ser maior que zero";
        }

        BigDecimal valorReferenciaCategoria = obterValorReferenciaCategoria(dados.getAno(), dados.getCodigoCategoria());

        if (valorReferenciaCategoria == null) {
            return "Valor de referência não encontrado para o veículo no ano e categoria informados";
        }

        BigDecimal limiteInferior = valorReferenciaCategoria.multiply(new BigDecimal("0.75")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal limiteSuperior = valorReferenciaCategoria;


        if (dados.getValorMaximoSegurado().compareTo(limiteInferior) < 0 ||
                dados.getValorMaximoSegurado().compareTo(limiteSuperior) > 0) {
            return "Valor m�ximo segurado deve estar entre 75% e 100% do valor do carro encontrado na categoria";
        }

        return null;
    }

    private String validarAno(int ano) {
        if (ano < 2020 || ano > 2025) {
            return "Ano tem que estar entre 2020 e 2025, incluindo estes";
        }
        return null;
    }

    private String validarCodigoCategoria(int codigoCategoria) {
        if (codigoCategoria < 1 || codigoCategoria > CategoriaVeiculo.values().length) {
            return "Categoria inv�lida";
        }
        return null;
    }

    private BigDecimal obterValorReferenciaCategoria(int ano, int codigoCat) {
        try {
            CategoriaVeiculo categoria = CategoriaVeiculo.values()[codigoCat - 1];
            for (PrecoAno pa : categoria.getPrecosAnos()) {
                if (pa.getAno() == ano) {
                    return BigDecimal.valueOf(pa.getPreco()).setScale(2, RoundingMode.HALF_UP);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }
}
