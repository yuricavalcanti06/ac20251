package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import java.io.Serializable;

public class VeiculoDAO extends DAOGenerico {

    public VeiculoDAO() {
        super();
        cadastro = new CadastroObjetos(Veiculo.class);
    }

    private String obterChave(Veiculo veiculo) {
        return veiculo.getPlaca();
    }

    public Veiculo buscar(String placa) {
        return (Veiculo) cadastro.buscar(placa);
    }

    public boolean incluir(Veiculo veiculo) {
        String chave = obterChave(veiculo);
        if (buscar(chave) != null) {
            return false;
        } else {
            cadastro.incluir(veiculo, chave);
            return true;
        }
    }

    public boolean alterar(Veiculo veiculo) {
        String chave = obterChave(veiculo);
        if (buscar(chave) == null) {
            return false;
        } else {
            cadastro.alterar(veiculo, chave);
            return true;
        }
    }

    public boolean excluir(String placa) {
        if (buscar(placa) == null) {
            return false;
        } else {
            cadastro.excluir(placa);
            return true;
        }
    }
}
