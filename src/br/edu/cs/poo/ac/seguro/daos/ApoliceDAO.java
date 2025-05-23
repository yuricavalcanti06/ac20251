package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Apolice;

public class ApoliceDAO extends DAOGenerico<Apolice> {

    public ApoliceDAO() {
        super();
    }

    @Override
    public Class<Apolice> getClasseEntidade() {
        return Apolice.class;
    }
}
