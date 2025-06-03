package br.edu.cs.poo.ac.seguro.excecoes;

import java.util.ArrayList;
import java.util.List;

public class ExcecaoValidacaoDados extends Exception {
    private static final long serialVersionUID = 1L;
    private List<String> mensagens;

    public ExcecaoValidacaoDados() {
        super();
        this.mensagens = new ArrayList<>();
    }

    public ExcecaoValidacaoDados(String mensagemInicial) {
        super(mensagemInicial);
        this.mensagens = new ArrayList<>();
        if (mensagemInicial != null && !mensagemInicial.isEmpty()) {
            this.mensagens.add(mensagemInicial);
        }
    }

    public List<String> getMensagens() {
        return mensagens;
    }

    public void adicionarMensagem(String mensagem) {
        if (mensagem != null && !mensagem.isEmpty()) {
            this.mensagens.add(mensagem);
        }
    }

    @Override
    public String getMessage() {
        if (mensagens == null || mensagens.isEmpty()) {
            return super.getMessage();
        }
        StringBuilder sb = new StringBuilder();
        for (String msg : mensagens) {
            sb.append(msg).append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
}
