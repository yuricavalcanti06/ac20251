package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.Registro;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import br.edu.cs.poo.ac.seguro.excecoes.ExcecaoValidacaoDados;

public class SinistroMediator {

    private VeiculoDAO daoVeiculo = new VeiculoDAO();
    private ApoliceDAO daoApolice = new ApoliceDAO();
    private SinistroDAO daoSinistro = new SinistroDAO();
    private static SinistroMediator instancia;

    public static SinistroMediator getInstancia() {
        if (instancia == null)
            instancia = new SinistroMediator();
        return instancia;
    }

    private SinistroMediator() {
    }

    public String incluirSinistro(DadosSinistro dados, LocalDateTime dataHoraAtual) throws ExcecaoValidacaoDados {
        List<String> errosValidacao = new ArrayList<>();

        if (dados == null) {
            ExcecaoValidacaoDados ex = new ExcecaoValidacaoDados();
            ex.adicionarMensagem("Dados do sinistro devem ser informados");
            throw ex;
        }

        if (dados.getDataHoraSinistro() == null) {
            errosValidacao.add("Data/hora do sinistro deve ser informada");
        } else if (dataHoraAtual != null && dados.getDataHoraSinistro().isAfter(dataHoraAtual)) {
            errosValidacao.add("Data/hora do sinistro deve ser menor que a data/hora atual");
        }

        if (dados.getPlaca() == null || dados.getPlaca().trim().isEmpty()) {
            errosValidacao.add("Placa do Ve�culo deve ser informada");
        }

        if (dados.getUsuarioRegistro() == null || dados.getUsuarioRegistro().trim().isEmpty()) {
            errosValidacao.add("Usu�rio do registro de sinistro deve ser informado");
        }

        if (dados.getValorSinistro() <= 0) {
            errosValidacao.add("Valor do sinistro deve ser maior que zero");
        }

        Veiculo veiculoSinistrado = null;
        if (dados.getPlaca() != null && !dados.getPlaca().trim().isEmpty()) {
            veiculoSinistrado = daoVeiculo.buscar(dados.getPlaca());
            if (veiculoSinistrado == null) {
                errosValidacao.add("Ve�culo n�o cadastrado");
            }
        }

        TipoSinistro tipoSinistroSelecionado = null;
        boolean tipoSinistroValido = false;
        for (TipoSinistro ts : TipoSinistro.values()) {
            if (ts.getCodigo() == dados.getCodigoTipoSinistro()) {
                tipoSinistroSelecionado = ts;
                tipoSinistroValido = true;
                break;
            }
        }
        if (!tipoSinistroValido) {
            errosValidacao.add("C�digo do tipo de sinistro inv�lido");
        }

        Apolice apoliceCobertura = null;
        if (veiculoSinistrado != null && dados.getDataHoraSinistro() != null && errosValidacao.isEmpty()) {
            Registro[] todasApolicesRegistros = daoApolice.buscarTodos();
            for (Registro reg : todasApolicesRegistros) {
                if (reg instanceof Apolice) {
                    Apolice ap = (Apolice) reg;
                    if (ap.getVeiculo() != null && ap.getVeiculo().getPlaca().equals(dados.getPlaca()) && ap.getDataInicioVigencia() != null) {
                        LocalDateTime dataInicioVigenciaApolice = ap.getDataInicioVigencia().atStartOfDay();
                        LocalDateTime dataFimVigenciaApolice = dataInicioVigenciaApolice.plusYears(1);
                        if (!dados.getDataHoraSinistro().isBefore(dataInicioVigenciaApolice) && dados.getDataHoraSinistro().isBefore(dataFimVigenciaApolice)) {
                            apoliceCobertura = ap;
                            break;
                        }
                    }
                }
            }
            if (apoliceCobertura == null) {
                errosValidacao.add("N�o existe ap�lice vigente para o ve�culo");
            } else {
                if (apoliceCobertura.getValorMaximoSegurado() != null) {
                    BigDecimal valorSinistroBd = new BigDecimal(dados.getValorSinistro());
                    if (valorSinistroBd.compareTo(apoliceCobertura.getValorMaximoSegurado()) > 0) {
                        errosValidacao.add("Valor do sinistro n�o pode ultrapassar o valor m�ximo segurado constante na ap�lice");
                    }
                }
            }
        }

        if (!errosValidacao.isEmpty()) {
            ExcecaoValidacaoDados ex = new ExcecaoValidacaoDados();
            for (String erro : errosValidacao) {
                ex.adicionarMensagem(erro);
            }
            throw ex;
        }

        if (apoliceCobertura == null) {
            ExcecaoValidacaoDados ex = new ExcecaoValidacaoDados();
            ex.adicionarMensagem("Erro interno: Ap�lice de cobertura n�o foi determinada apesar de passar nas valida��es.");
            throw ex;
        }

        int sequencial = 1;
        Registro[] todosSinistrosRegistros = daoSinistro.buscarTodos();
        List<Sinistro> sinistrosDaApolice = new ArrayList<>();

        for (Registro reg : todosSinistrosRegistros) {
            if (reg instanceof Sinistro) {
                Sinistro s = (Sinistro) reg;
                if (s.getNumeroApolice() != null && s.getNumeroApolice().equals(apoliceCobertura.getNumero())) {
                    sinistrosDaApolice.add(s);
                }
            }
        }

        if (!sinistrosDaApolice.isEmpty()) {
            sinistrosDaApolice.sort(new ComparadorSinistroSequencial());
            sequencial = sinistrosDaApolice.get(sinistrosDaApolice.size() - 1).getSequencial() + 1;
        }

        String sequencialFormatado = String.format("%03d", sequencial);
        String numeroSinistro = "S" + apoliceCobertura.getNumero() + sequencialFormatado;

        BigDecimal valorSinistroParaNovoSinistro = new BigDecimal(dados.getValorSinistro());

        Sinistro novoSinistro = new Sinistro(
                numeroSinistro,
                veiculoSinistrado,
                dados.getDataHoraSinistro(),
                dataHoraAtual,
                dados.getUsuarioRegistro(),
                valorSinistroParaNovoSinistro,
                tipoSinistroSelecionado
        );

        novoSinistro.setNumeroApolice(apoliceCobertura.getNumero());
        novoSinistro.setSequencial(sequencial);

        boolean incluido = daoSinistro.incluir(novoSinistro);
        if (!incluido) {
            ExcecaoValidacaoDados ex = new ExcecaoValidacaoDados();
            if (daoSinistro.buscar(numeroSinistro) != null) {
                ex.adicionarMensagem("Falha ao incluir o sinistro. O sinistro com n�mero " + numeroSinistro + " j� existe.");
            } else {
                ex.adicionarMensagem("Falha ao incluir o sinistro no sistema devido a um erro interno ou valida��o do DAO.");
            }
            throw ex;
        }
        return numeroSinistro;
    }
}
