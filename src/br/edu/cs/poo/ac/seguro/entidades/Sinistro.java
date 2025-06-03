package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Sinistro implements Serializable, Registro {

    private static final long serialVersionUID = 1L;
    private String numero;
    private Veiculo veiculo;
    private LocalDateTime dataHoraOcorrencia;
    private LocalDateTime dataHoraComunicacao;
    private String local;
    private BigDecimal valorSinistro;
    private TipoSinistro tipo;

    private int sequencial;
    private String numeroApolice;

    private String usuarioRegistro;

    public Sinistro(String numero, Veiculo veiculo, LocalDateTime dataHoraOcorrencia,
                    LocalDateTime dataHoraComunicacao, String local, BigDecimal valor, TipoSinistro tipo) {
        this.numero = numero;
        this.veiculo = veiculo;
        this.dataHoraOcorrencia = dataHoraOcorrencia;
        this.dataHoraComunicacao = dataHoraComunicacao;
        this.local = local;
        this.valorSinistro = valor;
        this.tipo = tipo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public LocalDateTime getDataHoraOcorrencia() {
        return dataHoraOcorrencia;
    }

    public void setDataHoraOcorrencia(LocalDateTime dataHoraOcorrencia) {
        this.dataHoraOcorrencia = dataHoraOcorrencia;
    }

    public LocalDateTime getDataHoraComunicacao() {
        return dataHoraComunicacao;
    }

    public void setDataHoraComunicacao(LocalDateTime dataHoraComunicacao) {
        this.dataHoraComunicacao = dataHoraComunicacao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    // Getter e Setter para valorSinistro
    public BigDecimal getValorSinistro() {
        return valorSinistro;
    }

    public void setValorSinistro(BigDecimal valorSinistro) {
        this.valorSinistro = valorSinistro;
    }

    public TipoSinistro getTipo() {
        return tipo;
    }

    public void setTipo(TipoSinistro tipo) {
        this.tipo = tipo;
    }

    public int getSequencial() {
        return sequencial;
    }

    public void setSequencial(int sequencial) {
        this.sequencial = sequencial;
    }

    public String getNumeroApolice() {
        return numeroApolice;
    }

    public void setNumeroApolice(String numeroApolice) {
        this.numeroApolice = numeroApolice;
    }

    // Getter e Setter para usuarioRegistro
    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    @Override
    public String getIdUnico() {
        return this.getNumero();
    }
}
