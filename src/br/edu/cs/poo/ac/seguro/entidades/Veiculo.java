package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;

public class Veiculo implements Serializable, Registro {

    private static final long serialVersionUID = 1L;
    private String placa;
    private int ano;
    private Segurado proprietario;
    private CategoriaVeiculo categoria;

    public Veiculo() {
    }

    public Veiculo(String placa, int ano, SeguradoPessoa proprietarioPessoa, SeguradoEmpresa proprietarioEmpresa, CategoriaVeiculo categoria) {
        this.placa = placa;
        this.ano = ano;
        this.categoria = categoria;

        if (proprietarioPessoa != null) {
            this.proprietario = proprietarioPessoa;
        } else if (proprietarioEmpresa != null) {
            this.proprietario = proprietarioEmpresa;
        } else {
            this.proprietario = null;
        }
    }

    public Veiculo(String placa, int ano, Segurado proprietario, CategoriaVeiculo categoria) {
        this.placa = placa;
        this.ano = ano;
        this.proprietario = proprietario;
        this.categoria = categoria;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Segurado getProprietario() {
        return proprietario;
    }

    public void setProprietario(Segurado proprietario) {
        this.proprietario = proprietario;
    }

    public CategoriaVeiculo getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaVeiculo categoria) {
        this.categoria = categoria;
    }

    @Override
    public String getIdUnico() {
        return this.getPlaca();
    }
}
