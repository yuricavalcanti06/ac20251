package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Segurado;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class SeguradoEmpresaDAO extends SeguradoDAO {

    public SeguradoEmpresaDAO() {
        super();
    }

    @Override
    public Class<Segurado> getClasseEntidade() {
        @SuppressWarnings("unchecked")
        Class<Segurado> clazz = (Class<Segurado>)(Class<?>)SeguradoEmpresa.class;
        return clazz;
    }

    public SeguradoEmpresa buscar(String cnpj) {
        Segurado segurado = super.buscar(cnpj);
        if (segurado instanceof SeguradoEmpresa) {
            return (SeguradoEmpresa) segurado;
        }
        return null;
    }
}
