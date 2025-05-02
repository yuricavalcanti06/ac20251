package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import java.math.BigDecimal;

public class SeguradoEmpresaMediator {

    private static SeguradoEmpresaMediator instancia;
    private SeguradoEmpresaDAO seguradoEmpresaDAO = new SeguradoEmpresaDAO();
    private SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();

    private SeguradoEmpresaMediator() {}

    public static SeguradoEmpresaMediator getInstancia() {
        if (instancia == null) {
            instancia = new SeguradoEmpresaMediator();
        }
        return instancia;
    }

    public String validarCnpj(String cnpj) {
        if (StringUtils.ehNuloOuBranco(cnpj)) {
            return "CNPJ deve ser informado";
        }
        if (cnpj.length() != 14) {
            return "CNPJ deve ter 14 caracteres";
        }
        if (!ValidadorCpfCnpj.ehCnpjValido(cnpj)) {
            return "CNPJ com dígito inválido";
        }
        return null;
    }

    public String validarFaturamento(double faturamento) {
        if (faturamento <= 0) {
            return "Faturamento deve ser maior que zero";
        }
        return null;
    }

    public String validarSeguradoEmpresa(SeguradoEmpresa seg) {
        if (seg == null) {
            return "Segurado Empresa não pode ser nulo";
        }
        String msg = seguradoMediator.validarNome(seg.getNome());
        if (msg != null) return msg;

        msg = seguradoMediator.validarEndereco(seg.getEndereco());
        if (msg != null) return msg;

        msg = seguradoMediator.validarDataCriacao(seg.getDataAbertura());
        if (msg != null) {
            if (msg.contains("Data da criação")) {
                return "Data da abertura deve ser informada";
            }
            return msg;
        }

        msg = validarCnpj(seg.getCnpj());
        if (msg != null) return msg;

        msg = validarFaturamento(seg.getFaturamento());
        if (msg != null) return msg;

        if (seg.getBonus() != null && seg.getBonus().compareTo(BigDecimal.ZERO) < 0) {
            return "Bônus não pode ser negativo";
        }

        return null;
    }

    public String incluirSeguradoEmpresa(SeguradoEmpresa seg) {
        String msg = validarSeguradoEmpresa(seg);
        if (msg != null) {
            return msg;
        }

        if (seguradoEmpresaDAO.buscar(seg.getCnpj()) != null) {
            return "CNPJ do segurado empresa já existente";
        }

        boolean incluiu = seguradoEmpresaDAO.incluir(seg);
        if (!incluiu) {
            return "Erro inesperado ao incluir segurado empresa";
        }
        return null;
    }

    public String alterarSeguradoEmpresa(SeguradoEmpresa seg) {
        String msg = validarSeguradoEmpresa(seg);
        if (msg != null) {
            return msg;
        }

        if (seguradoEmpresaDAO.buscar(seg.getCnpj()) == null) {
            return "CNPJ do segurado empresa não existente";
        }

        boolean alterou = seguradoEmpresaDAO.alterar(seg);
        if (!alterou) {
            return "Erro inesperado ao alterar segurado empresa";
        }
        return null;
    }

    public String excluirSeguradoEmpresa(String cnpj) {
        boolean excluiu = seguradoEmpresaDAO.excluir(cnpj);

        if (!excluiu) {
            String msgValidacao = validarCnpj(cnpj);
            if (msgValidacao != null) {
                if (seguradoEmpresaDAO.buscar(cnpj) == null) {
                    return "CNPJ do segurado empresa não existente";
                } else {
                    return "Erro ao excluir segurado empresa";
                }
            } else {
                return "CNPJ do segurado empresa não existente";
            }
        }
        return null;
    }

    public SeguradoEmpresa buscarSeguradoEmpresa(String cnpj) {
        return seguradoEmpresaDAO.buscar(cnpj);
    }
}
