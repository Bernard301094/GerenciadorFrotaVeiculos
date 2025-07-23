package br.com.gestaofrota.model.veiculo;

import br.com.gestaofrota.enums.StatusVeiculo;

import java.util.Date;

public abstract class Veiculo {

    private String placa;
    private String marca;
    private String modelo;
    private Integer ano;
    private StatusVeiculo status;
    private Double quilometragem;
    private Date dataUltimaManutencao;

    public Veiculo(String placa, String marca, String modelo, Integer ano) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
    }
}
