package br.edu.cs.poo.ac.seguro.entidades;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class Sinistro {
    private String numero;
    private Veiculo veiculo;
    private LocalDateTime dataHoraSinistro;
    private LocalDateTime dataHoraRegistro;
    private String usuarioRegistro;
    private BigDecimal valorSinistro;
    private TipoSinistro tipo;
}