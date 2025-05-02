package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SinistroDAO extends DAOGenerico {

    public SinistroDAO() {
        super();
        cadastro = new CadastroObjetos(Sinistro.class);
    }

    private String obterChave(Sinistro sinistro) {
        return sinistro.getNumero();
    }

    public Sinistro buscar(String numero) {
        return (Sinistro) cadastro.buscar(numero);
    }

    public boolean incluir(Sinistro sinistro) {
        String chave = obterChave(sinistro);
        if (chave == null || chave.trim().isEmpty()) {
            return false;
        }
        if (buscar(chave) != null) {
            return false;
        } else {
            cadastro.incluir(sinistro, chave);
            return true;
        }
    }

    public boolean alterar(Sinistro sinistro) {
        String chave = obterChave(sinistro);
        if (chave == null || chave.trim().isEmpty()) {
            return false;
        }
        if (buscar(chave) == null) {
            return false;
        } else {
            cadastro.alterar(sinistro, chave);
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

    public Sinistro[] buscarTodos() {
        Serializable[] rets = cadastro.buscarTodos(Sinistro.class);
        Sinistro[] sinistros = new Sinistro[rets.length];
        for(int i=0; i<rets.length; i++) {
            sinistros[i] = (Sinistro)rets[i];
        }
        return sinistros;
    }
}
