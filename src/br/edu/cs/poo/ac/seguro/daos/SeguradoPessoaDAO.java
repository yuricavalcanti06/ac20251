package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Segurado;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

public class SeguradoPessoaDAO extends SeguradoDAO {

    public SeguradoPessoaDAO() {
        super();
    }

    @Override
    public Class<Segurado> getClasseEntidade() {
        @SuppressWarnings("unchecked")
        Class<Segurado> clazz = (Class<Segurado>)(Class<?>)SeguradoPessoa.class;
        return clazz;
    }

    public SeguradoPessoa buscar(String cpf) {
        Segurado segurado = super.buscar(cpf);
        if (segurado instanceof SeguradoPessoa) {
            return (SeguradoPessoa) segurado;
        }
        return null;
    }
}
