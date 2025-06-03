package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.time.LocalDate;
import br.edu.cs.poo.ac.seguro.entidades.Endereco;

public class SeguradoMediator {

    private static SeguradoMediator instancia;

    private SeguradoMediator() {}

    public static SeguradoMediator getInstancia() {
        if (instancia == null) {
            instancia = new SeguradoMediator();
        }
        return instancia;
    }

    public String validarNome(String nome) {
        if (StringUtils.ehNuloOuBranco(nome)) {
            return "Nome deve ser informado";
        }
        if (nome.trim().length() > 100) {
            return "Tamanho do nome deve ser no m�ximo 100 caracteres";
        }
        return null;
    }

    public String validarEndereco(Endereco endereco) {
        if (endereco == null) {
            return "Endere�o deve ser informado";
        }
        if (StringUtils.ehNuloOuBranco(endereco.getLogradouro())) {
            return "Logradouro deve ser informado";
        }
        if (StringUtils.ehNuloOuBranco(endereco.getCep())) {
            return "CEP deve ser informado";
        }
        if (endereco.getCep().length() != 8) {
            return "Tamanho do CEP deve ser 8 caracteres";
        }
        if (!StringUtils.temSomenteNumeros(endereco.getCep())) {
            return "CEP deve ter formato NNNNNNNN";
        }
        if (StringUtils.ehNuloOuBranco(endereco.getCidade())) {
            return "Cidade deve ser informada";
        }
        if (endereco.getCidade().length() > 100) {
            return "Tamanho da cidade deve ser no m�ximo 100 caracteres";
        }
        if (StringUtils.ehNuloOuBranco(endereco.getEstado())) {
            return "Sigla do estado deve ser informada";
        }
        if (endereco.getEstado().length() != 2) {
            return "Tamanho da sigla do estado deve ser 2 caracteres";
        }
        if (StringUtils.ehNuloOuBranco(endereco.getPais())) {
            return "Pa�s deve ser informado";
        }
        if (endereco.getPais().length() > 40) {
            return "Tamanho do pa�s deve ser no m�ximo 40 caracteres";
        }
        if (endereco.getNumero() != null && endereco.getNumero().trim().length() > 20) {
            return "Tamanho do n�mero deve ser no m�ximo 20 caracteres";
        }
        if (endereco.getComplemento() != null && endereco.getComplemento().trim().length() > 30) {
            return "Tamanho do complemento deve ser no m�ximo 30 caracteres";
        }

        return null;
    }

    public String validarDataCriacao(LocalDate dataCriacao) {
        if (dataCriacao == null) {
            return "Data da cria��o deve ser informada";
        }
        if (dataCriacao.isAfter(LocalDate.now())) {
            return "Data da cria��o deve ser menor ou igual � data atual";
        }
        return null;
    }

    public BigDecimal ajustarDebitoBonus(BigDecimal bonus, BigDecimal valorDebito) {
        if (bonus == null || valorDebito == null || valorDebito.compareTo(BigDecimal.ZERO) <= 0) {
            return bonus;
        }
        if (bonus.compareTo(valorDebito) < 0) {
            return bonus;
        } else {
            return valorDebito;
        }
    }
}
