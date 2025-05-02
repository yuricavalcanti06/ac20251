package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import java.io.Serializable;

public class SeguradoEmpresaDAO extends DAOGenerico {

    public SeguradoEmpresaDAO() {
        super();
        cadastro = new CadastroObjetos(SeguradoEmpresa.class);
    }

    private String obterChave(SeguradoEmpresa segurado) {
        return segurado.getCnpj();
    }

    public SeguradoEmpresa buscar(String cnpj) {
        return (SeguradoEmpresa) cadastro.buscar(cnpj);
    }

    public boolean incluir(SeguradoEmpresa segurado) {
        String chave = obterChave(segurado);
        if (buscar(chave) != null) {
            return false;
        } else {
            cadastro.incluir(segurado, chave);
            return true;
        }
    }

    public boolean alterar(SeguradoEmpresa segurado) {
        String chave = obterChave(segurado);
        if (buscar(chave) == null) {
            return false;
        } else {
            cadastro.alterar(segurado, chave);
            return true;
        }
    }

    public boolean excluir(String cnpj) {
        if (buscar(cnpj) == null) {
            return false;
        } else {
            cadastro.excluir(cnpj);
            return true;
        }
    }
}
