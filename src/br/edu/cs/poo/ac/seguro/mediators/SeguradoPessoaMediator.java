package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import java.math.BigDecimal;

public class SeguradoPessoaMediator {

    private static SeguradoPessoaMediator instancia;
    private SeguradoPessoaDAO seguradoPessoaDAO = new SeguradoPessoaDAO();
    private SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();

    private SeguradoPessoaMediator() {}

    public static SeguradoPessoaMediator getInstancia() {
        if (instancia == null) {
            instancia = new SeguradoPessoaMediator();
        }
        return instancia;
    }

    public String validarCpf(String cpf) {
        if (StringUtils.ehNuloOuBranco(cpf)) {
            return "CPF deve ser informado";
        }
        if (cpf.length() != 11) {
            return "CPF deve ter 11 caracteres";
        }
        if (!ValidadorCpfCnpj.ehCpfValido(cpf)) {
            return "CPF com d�gito inv�lido";
        }
        return null;
    }

    public String validarRenda(double renda) {
        if (renda < 0) {
            return "Renda deve ser maior ou igual � zero";
        }
        return null;
    }

    public String validarSeguradoPessoa(SeguradoPessoa seg) {
        if (seg == null) {
            return "Segurado Pessoa n�o pode ser nulo";
        }
        String msg = seguradoMediator.validarNome(seg.getNome());
        if (msg != null) return msg;

        msg = seguradoMediator.validarEndereco(seg.getEndereco());
        if (msg != null) return msg;

        msg = seguradoMediator.validarDataCriacao(seg.getDataNascimento());
        if (msg != null) {
            if (msg.contains("Data da cria��o")) {
                return "Data do nascimento deve ser informada";
            }
            return msg;
        }

        msg = validarCpf(seg.getCpf());
        if (msg != null) return msg;

        msg = validarRenda(seg.getRenda());
        if (msg != null) return msg;

        if (seg.getBonus() != null && seg.getBonus().compareTo(BigDecimal.ZERO) < 0) {
            return "B�nus n�o pode ser negativo";
        }

        return null;
    }


    public String incluirSeguradoPessoa(SeguradoPessoa seg) {
        String msg = validarSeguradoPessoa(seg);
        if (msg != null) {
            return msg;
        }

        if (seguradoPessoaDAO.buscar(seg.getCpf()) != null) {
            return "CPF do segurado pessoa j� existente";
        }

        boolean incluiu = seguradoPessoaDAO.incluir(seg);
        if (!incluiu) {
            return "Erro inesperado ao incluir segurado pessoa";
        }
        return null;
    }

    public String alterarSeguradoPessoa(SeguradoPessoa seg) {
        String msg = validarSeguradoPessoa(seg);
        if (msg != null) {
            return msg;
        }

        if (seguradoPessoaDAO.buscar(seg.getCpf()) == null) {
            return "CPF do segurado pessoa n�o existente";
        }

        boolean alterou = seguradoPessoaDAO.alterar(seg);
        if (!alterou) {
            return "Erro inesperado ao alterar segurado pessoa";
        }
        return null;
    }

    public String excluirSeguradoPessoa(String cpf) {
        boolean excluiu = seguradoPessoaDAO.excluir(cpf);

        if (!excluiu) {
            String msgValidacao = validarCpf(cpf);
            if (msgValidacao != null) {
                if (seguradoPessoaDAO.buscar(cpf) == null) {
                    return "CPF do segurado pessoa n�o existente";
                } else {
                    return "Erro ao excluir segurado pessoa";
                }

            } else {
                return "CPF do segurado pessoa n�o existente";
            }
        }
        return null;
    }

    public SeguradoPessoa buscarSeguradoPessoa(String cpf) {
        return seguradoPessoaDAO.buscar(cpf);
    }
}