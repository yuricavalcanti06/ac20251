package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Registro;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;

public class SinistroDAO extends DAOGenerico<Sinistro> {

    public SinistroDAO() {
        super();
    }

    @Override
    public Class<Sinistro> getClasseEntidade() {
        return Sinistro.class;
    }

    @Override
    public Sinistro[] buscarTodos() {
        Registro[] registros = super.buscarTodos(); // Chama o método da superclasse que retorna Registro[]
        if (registros == null || registros.length == 0) {
            return new Sinistro[0];
        }

        Sinistro[] sinistros = new Sinistro[registros.length];

        for (int i = 0; i < registros.length; i++) {
            if (registros[i] instanceof Sinistro) {
                sinistros[i] = (Sinistro) registros[i];
            } else {
                throw new ClassCastException("Objeto inesperado no DAO de Sinistro: " + registros[i].getClass().getName());
            }
        }
        return sinistros;
    }

}
