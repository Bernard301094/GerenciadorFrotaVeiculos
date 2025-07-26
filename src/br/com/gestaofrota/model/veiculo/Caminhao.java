package br.com.gestaofrota.model.veiculo;

import br.com.gestaofrota.enums.TipoCaminhao;
import br.com.gestaofrota.exception.VeiculoException;

import java.time.Year;

public class Caminhao extends Veiculo{

    private double capacidadeCarga;
    private int numeroEixos;
    private boolean temCacamba;
    private TipoCaminhao tipo;

    public Caminhao(String placa, String marca, String modelo, Integer ano, double capacidadeCarga, int numeroEixos, boolean temCacamba, TipoCaminhao tipo) {
        super(placa, marca, modelo, ano);
        this.capacidadeCarga = capacidadeCarga;
        this.numeroEixos = numeroEixos;
        this.temCacamba = temCacamba;
        this.tipo = tipo;
    }

    public double getCapacidadeCarga() {
        return capacidadeCarga;
    }

    public void setCapacidadeCarga(double capacidadeCarga) {
        this.capacidadeCarga = capacidadeCarga;
    }

    public int getNumeroEixos() {
        return numeroEixos;
    }

    public void setNumeroEixos(int numeroEixos) {
        this.numeroEixos = numeroEixos;
    }

    public boolean isTemCacamba() {
        return temCacamba;
    }

    public void setTemCacamba(boolean temCacamba) {
        this.temCacamba = temCacamba;
    }

    public TipoCaminhao getTipo() {
        return tipo;
    }

    public void setTipo(TipoCaminhao tipo) {
        this.tipo = tipo;
    }

    @Override
    public double calcularCustoManutencao() {
        // Define o custo base de manutenção como R$ 700
        double custoTotal = 700.0;

        // Verifica se a quilometragem é maior que 15.000 km
        if (getQuilometragem() > 15000) {
            // Adiciona R$ 450 ao custo total
            custoTotal += 450;
        }

        // Define o custo por tonelada como R$ 40
        double custoPorTonelada = 40.0;

        // Verifica se a capacidade de carga é maior ou igual a 3 toneladas
        if (getCapacidadeCarga() >= 3) {
            // Adiciona ao custo total: custo por tonelada multiplicado pela capacidade de carga
            custoTotal += custoPorTonelada * getCapacidadeCarga();
        }

        // Verifica o tipo do caminhão e adiciona custo específico
        switch (this.tipo) {
            case FRIGORIFICO -> {
                // Adiciona R$ 450 para caminhão frigorífico
                custoTotal += 450;
            }
            case GUINCHO -> {
                // Adiciona R$ 400 para caminhão guincho
                custoTotal += 400;
            }
            case TANQUE -> {
                // Adiciona R$ 300 para caminhão tanque
                custoTotal += 300;
            }
        }

        // Obtém o ano atual
        int anoAtual = Year.now().getValue();
        // Calcula a idade do caminhão
        int idadeDoCaminhao = anoAtual - getAno();

        // Verifica se o caminhão tem 8 anos ou mais
        if (idadeDoCaminhao >= 8) {
            // Adiciona ao custo total: idade do caminhão multiplicada por R$ 100
            custoTotal += idadeDoCaminhao * 100;
        }

        // Retorna o custo total de manutenção calculado
        return custoTotal;
    }

    public double calcularCustoOperacao() {
        // 1. DEFINIÇÃO DA DISTÂNCIA DIÁRIA MÉDIA
        // Estabelece uma quilometragem padrão que um caminhão roda em um dia de operação.
        final double distanciaDiariaMedia = 500.0; // 500 km

        // 2. CUSTO COM COMBUSTÍVEL (DIESEL) PARA O DIA
        // Define o consumo médio do caminhão e o preço do litro do diesel.
        final double consumoMedioKmLitro = 2.5;
        final double precoLitroDiesel = 6.00; // Valor de exemplo
        double custoCombustivel = (distanciaDiariaMedia / consumoMedioKmLitro) * precoLitroDiesel;

        // 3. CUSTO DE DESGASTE GERAL PARA O DIA
        // Custo provisionado para o desgaste de pneus, óleo, freios, etc., baseado na distância diária.
        final double custoDesgastePorKm = 0.90; // R$ 0,90 por km
        double custoDesgaste = distanciaDiariaMedia * custoDesgastePorKm;

        // 4. CUSTO COM MOTORISTA PARA O DIA
        // Custo diário com o motorista (salário, encargos, etc.) calculado com base na distância.
        final double custoMotoristaPorKm = 1.80; // R$ 1,80 por km
        double custoMotorista = distanciaDiariaMedia * custoMotoristaPorKm;

        // 5. CUSTO COM PEDÁGIOS PARA O DIA
        // Estimativa de custo diário com pedágios.
        final double custoMedioPedagioPor100Km = 35.00; // R$ 35 a cada 100 km
        double custoPedagios = (distanciaDiariaMedia / 100.0) * custoMedioPedagioPor100Km;

        // 6. CÁLCULO FINAL
        // Soma todos os custos para obter o custo operacional diário total.
        double custoTotalOperacaoDiaria = custoCombustivel + custoDesgaste + custoMotorista + custoPedagios;

        return custoTotalOperacaoDiaria;
    }

    public boolean validarCapacidadeCarga(double peso) {
        if (peso > 0 && peso <= this.capacidadeCarga) {return true;}

        return false;
    }
}
