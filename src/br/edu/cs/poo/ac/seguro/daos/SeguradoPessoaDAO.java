package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import java.io.Serializable;

public class SeguradoPessoaDAO extends DAOGenerico {

    public SeguradoPessoaDAO() {
        super();
        cadastro = new CadastroObjetos(SeguradoPessoa.class);
    }

    private String obterChave(SeguradoPessoa segurado) {
        return segurado.getCpf();
    }

    public SeguradoPessoa buscar(String cpf) {
        return (SeguradoPessoa)cadastro.buscar(cpf);
    }

    public boolean incluir(SeguradoPessoa segurado) {
        String chave = obterChave(segurado);
        if (buscar(chave) != null) {
            return false;
        } else {
            cadastro.incluir(segurado, chave);
            return true;
        }
    }

    public boolean alterar(SeguradoPessoa segurado) {
        String chave = obterChave(segurado);
        if (buscar(chave) == null) {
            return false;
        } else {
            cadastro.alterar(segurado, chave);
            return true;
        }
    }

    public boolean excluir(String cpf) {
        if (buscar(cpf) == null) {
            return false;
        } else {
            cadastro.excluir(cpf);
            return true;
        }
    }
}
