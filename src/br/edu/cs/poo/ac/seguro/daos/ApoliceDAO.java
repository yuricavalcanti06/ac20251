package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import java.io.Serializable;

public class ApoliceDAO extends DAOGenerico {

    public ApoliceDAO() {
        super();
        cadastro = new CadastroObjetos(Apolice.class);
    }

    private String obterChave(Apolice apolice) {
        return apolice.getNumero();
    }

    public Apolice buscar(String numero) {
        return (Apolice) cadastro.buscar(numero);
    }

    public boolean incluir(Apolice apolice) {
        String chave = obterChave(apolice);
        if (chave == null || chave.trim().isEmpty()) {
            return false;
        }
        if (buscar(chave) != null) {
            return false;
        } else {
            cadastro.incluir(apolice, chave);
            return true;
        }
    }

    public boolean alterar(Apolice apolice) {
        String chave = obterChave(apolice);
        if (chave == null || chave.trim().isEmpty()) {
            return false;
        }
        if (buscar(chave) == null) {
            return false;
        } else {
            cadastro.alterar(apolice, chave);
            return true;
        }
    }

    public boolean excluir(String numero) {
        if (numero == null || numero.trim().isEmpty()) {
            return false;
        }
        if (buscar(numero) == null) {
            return false;
        } else {
            cadastro.excluir(numero);
            return true;
        }
    }
}
