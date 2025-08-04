package br.com.gestaofrota.service;

import br.com.gestaofrota.exception.VeiculoException;
import br.com.gestaofrota.model.veiculo.Caminhao;
import br.com.gestaofrota.model.veiculo.Carro;
import br.com.gestaofrota.model.veiculo.Motocicleta;
import br.com.gestaofrota.model.veiculo.Veiculo;

import java.util.Date;
import java.util.List;

public class ServicoManutencao {

    public Date calcularProximaManutencao(Veiculo veiculo) {
        if (veiculo == null) {
            throw new VeiculoException("Veículo não pode ser null");
        }

        double quilometragemAtual = veiculo.getQuilometragem();
        double quilometragem = 0;
        int limiteManutencao = 0;
        int limiteTempoMeses = 0;

        if (veiculo instanceof Carro) {
            limiteManutencao = 10000;
            limiteTempoMeses = 6;
        } else if (veiculo instanceof Motocicleta) {
            limiteManutencao = 8000;
            limiteTempoMeses = 4;
        } else if (veiculo instanceof Caminhao) {
            limiteManutencao = 15000;
            limiteTempoMeses = 3;
        }

        if (quilometragemAtual < limiteManutencao) {
            quilometragem += limiteManutencao - quilometragemAtual;

            System.out.println("Status: OK | Quilometragem atual: "
                    + quilometragemAtual
                    + " km | Próxima manutenção: "
                    + limiteManutencao
                    + " km | Restante: "
                    + quilometragem
                    + " km");

        } else if (quilometragemAtual > limiteManutencao) {
            quilometragem += quilometragemAtual - limiteManutencao;
            System.out.println("ATENÇÃO: Veículo rodou "
                    + quilometragem
                    + " km além do limite de manutenção");
        }
    }


    public String gerarRelatorioManutencao(List<Veiculo> veiculos) {

    }

    public boolean validarStatusParaOperacao(Veiculo veiculo) {

    }

    public void agendarManutencaoPreventiva(Veiculo veiculo) {

    }
}
