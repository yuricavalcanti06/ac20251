package br.edu.cs.poo.ac.seguro.entidades;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class Veiculo implements Serializable{
    private static final long serialVersionUID = 1L;
    private String placa;
    private int ano;
    private SeguradoEmpresa proprietarioEmpresa;
    private SeguradoPessoa proprietarioPessoa;
    private CategoriaVeiculo categoria;

    public Veiculo(String placa, int ano, SeguradoEmpresa proprietarioEmpresa, SeguradoPessoa proprietarioPessoa, CategoriaVeiculo categoria){
        this.placa = placa;
        this.ano = ano;
        this.proprietarioEmpresa = proprietarioEmpresa;
        this.proprietarioPessoa = proprietarioPessoa;
        this.categoria = categoria;
    }
}