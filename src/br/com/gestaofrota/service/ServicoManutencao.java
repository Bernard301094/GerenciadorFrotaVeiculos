package br.com.gestaofrota.service;

import br.com.gestaofrota.exception.VeiculoException;
import br.com.gestaofrota.model.veiculo.Caminhao;
import br.com.gestaofrota.model.veiculo.Carro;
import br.com.gestaofrota.model.veiculo.Motocicleta;
import br.com.gestaofrota.model.veiculo.Veiculo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ServicoManutencao {

    public Date calcularProximaManutencao(Veiculo veiculo) {
        // PASSO 1: Validação de entrada
        if (veiculo == null) {
            throw new VeiculoException("Veículo não pode ser null");
        }

        // PASSO 2: Obter dados do veículo
        double quilometragemAtual = veiculo.getQuilometragem();
        double quilometragem = 0;

        // PASSO 3: Definir limites conforme o tipo de veículo
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

        // PASSO 4: Calcular data por tempo
        Calendar calTempo = Calendar.getInstance();

        if (veiculo.getDataUltimaManutencao() != null) {
            calTempo.setTime(veiculo.getDataUltimaManutencao());
        } else {
            calTempo.setTime(new Date());
        }

        calTempo.add(Calendar.MONTH, limiteTempoMeses);
        Date proximaPorTempo = calTempo.getTime();

        // PASSO 5: Calcular data por quilometragem
        double kmRestantes;
        Calendar calKm = Calendar.getInstance();

        if (quilometragemAtual >= limiteManutencao) {
            // Manutenção já vencida por quilometragem - deve ser imediata
            Date hoje = new Date();
            System.out.println("URGENTE: Manutenção vencida por quilometragem!");
            return hoje;
        } else {
            // Calcular quantos km faltam para próxima manutenção
            kmRestantes = limiteManutencao - quilometragemAtual;

            // Estimar km por mês baseado no tipo de veículo (inline)
            int kmPromedioMes;
            if (veiculo instanceof Carro) {
                kmPromedioMes = 1000; // Carros rodam em média 1000 km/mês
            } else if (veiculo instanceof Motocicleta) {
                kmPromedioMes = 800;  // Motos rodam em média 800 km/mês
            } else if (veiculo instanceof Caminhao) {
                kmPromedioMes = 2000; // Caminhões rodam em média 2000 km/mês
            } else {
                kmPromedioMes = 1000; // Padrão caso não identifique o tipo
            }

            // Calcular quantos meses faltam baseado no km restante
            int mesesParaLimite = (int) Math.ceil(kmRestantes / kmPromedioMes);

            calKm.add(Calendar.MONTH, mesesParaLimite);
        }

        Date proximaPorKm = calKm.getTime();

        // PASSO 6: Determinar qual data é mais próxima
        Date proximaManutencao;
        String criterio;

        if (proximaPorTempo.before(proximaPorKm)) {
            proximaManutencao = proximaPorTempo;
            criterio = "tempo";
        } else {
            proximaManutencao = proximaPorKm;
            criterio = "quilometragem";
        }

        // PASSO 7: Validar se a manutenção já está vencida por tempo
        Date hoje = new Date();
        if (proximaManutencao.before(hoje)) {
            System.out.println("URGENTE: Manutenção vencida por " + criterio + "!");
            return hoje; // Retorna data atual se já está vencida
        }

        // PASSO 8: Mostrar informação e retornar
        System.out.println("Próxima manutenção calculada por: " + criterio);
        System.out.println("Data prevista: " + proximaManutencao);

        return proximaManutencao;
    }

    public String gerarRelatorioManutencao(List<Veiculo> veiculos) {

    }

    public boolean validarStatusParaOperacao(Veiculo veiculo) {

    }

    public void agendarManutencaoPreventiva(Veiculo veiculo) {

    }
}
