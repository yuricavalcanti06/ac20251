package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DadosVeiculo {
    private String cpfOuCnpj;
    private String placa;
    private int ano;
    private BigDecimal valorMaximoSegurado;
    private int codigoCategoria;
}
