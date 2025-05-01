package br.edu.cs.poo.ac.seguro.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.io.Serializable;

public class Segurado implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nome;
	private Endereco endereco;
	private LocalDate dataCriacao;
	private BigDecimal bonus;

	public Segurado(String nome, Endereco endereco, LocalDate dataCriacao, BigDecimal bonus){
		this.nome = nome;
		this.endereco = endereco;
		this.dataCriacao = dataCriacao;
		this.bonus = bonus;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	protected LocalDate getDataCriacao() {
		return dataCriacao;
	}

	protected void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public int getIdade(){
		return Period.between(this.dataCriacao,  LocalDate.now()).getYears();
	}

	public BigDecimal getBonus() {
		return bonus;
	}

	public void creditarBonus(BigDecimal valor){
		this.bonus = this.bonus.add(valor);
	}

	public void debitarBonus(BigDecimal valor){
		this.bonus = this.bonus.subtract(valor);
	}
}