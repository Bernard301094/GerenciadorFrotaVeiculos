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
        int limiteManutencao;
        int limiteTempoMeses;

        // Usando pattern matching para switch (recurso padrão do Java 21+) para definir os limites
        switch (veiculo) {
            case Carro c -> {
                limiteManutencao = 10000;
                limiteTempoMeses = 6;
            }
            case Motocicleta m -> {
                limiteManutencao = 8000;
                limiteTempoMeses = 4;
            }
            case Caminhao c -> {
                limiteManutencao = 15000;
                limiteTempoMeses = 3;
            }
            // Adicionar um caso default para tipos de veículo inesperados
            default -> throw new VeiculoException("Tipo de veículo não suportado: " + veiculo.getClass().getSimpleName());
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

            switch (veiculo) {
                case Carro c -> kmPromedioMes = 1000; // Carros rodam em média 1000 km/mês
                case Motocicleta m -> kmPromedioMes = 800; // Motos rodam em média 800 km/mês
                case Caminhao c -> kmPromedioMes = 2000; // Caminhões rodam em média 2000 km/mês
                default -> kmPromedioMes = 1000; // Padrão caso não identifique o tipo
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
        // PASSO 1: Validação inicial
        if (veiculos == null) {
            throw new VeiculoException("A lista de veículos não pode ser null.");
        }
        if (veiculos.isEmpty()) {
            return "Não há veículos para analisar.\n";
        }

        // PASSO 2: Variáveis de controle
        int veiculosEmDia = 0;
        int veiculosVencidos = 0;
        int veiculosProximoVencimento = 0;
        StringBuilder relatorio = new StringBuilder();
        Date hoje = new Date();

        // Listas para alertas
        StringBuilder alertasVencidos = new StringBuilder();
        StringBuilder alertasProximos = new StringBuilder();

        // PASSO 3: Loop detalhado
        relatorio.append("DETALHES DOS VEÍCULOS:\n");
        relatorio.append("Placa | Modelo | Tipo | Status | Próxima Manutenção\n");
        relatorio.append("---------------------------------------------------\n");

        for (Veiculo veiculo : veiculos) {
            String placa = veiculo.getPlaca();
            String modelo = veiculo.getModelo();
            String tipo = veiculo.getClass().getSimpleName();

            Date proximaManutencao = calcularProximaManutencao(veiculo);

            long diffMillis = proximaManutencao.getTime() - hoje.getTime();
            long diffDias = diffMillis / (1000 * 60 * 60 * 24);

            String status;
            if (!proximaManutencao.after(hoje)) {
                status = "VENCIDO";
                veiculosVencidos++;
                alertasVencidos.append(placa).append(" (").append(modelo).append(")\n");
            } else if (diffDias <= 30) {
                status = "PRÓXIMO DO VENCIMENTO";
                veiculosProximoVencimento++;
                alertasProximos.append(placa).append(" (").append(modelo).append(")\n");
            } else {
                status = "EM DIA";
                veiculosEmDia++;
            }

            relatorio.append(placa).append(" | ")
                    .append(modelo).append(" | ")
                    .append(tipo).append(" | ")
                    .append(status).append(" | ")
                    .append(proximaManutencao).append("\n");
        }

        // PASSO 4: Estatísticas gerais
        int total = veiculos.size();
        double percEmDia = (veiculosEmDia * 100.0) / total;
        double percVencidos = (veiculosVencidos * 100.0) / total;
        double percProximos = (veiculosProximoVencimento * 100.0) / total;

        // PASSO 5: Montar relatório final
        StringBuilder resultado = new StringBuilder();
        resultado.append("RELATÓRIO DE MANUTENÇÃO DA FROTA\n");
        resultado.append("Data: ").append(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(hoje)).append("\n");
        resultado.append("===================================================\n");
        resultado.append("RESUMO:\n");
        resultado.append("Total de veículos analisados: ").append(total).append("\n");
        resultado.append(String.format("Em dia: %d (%.1f%%)\n", veiculosEmDia, percEmDia));
        resultado.append(String.format("Vencidos: %d (%.1f%%)\n", veiculosVencidos, percVencidos));
        resultado.append(String.format("Próximos do vencimento: %d (%.1f%%)\n", veiculosProximoVencimento, percProximos));
        resultado.append("---------------------------------------------------\n");
        resultado.append(relatorio);

        // Seção de alertas
        if (veiculosVencidos > 0) {
            resultado.append("\nALERTAS - MANUTENÇÃO VENCIDA:\n").append(alertasVencidos);
        }
        if (veiculosProximoVencimento > 0) {
            resultado.append("\nALERTAS - PRÓXIMOS DO VENCIMENTO:\n").append(alertasProximos);
        }

        // PASSO 6: Retornar relatório
        return resultado.toString();
    }

    public boolean validarStatusParaOperacao(Veiculo veiculo) {
        if (veiculo == null) {
            throw new VeiculoException("Veículo não pode ser null");
        }

        Date hoje = new Date();
        Date dataProximaManutencao = calcularProximaManutencao(veiculo);

        if (!dataProximaManutencao.after(hoje)) {
            System.out.println("ALERTA: Manutenção vencida ou para hoje (" + dataProximaManutencao + "). Veículo não pode operar.");
            return false; // Não pode operar
        }
        System.out.println("Status OK. Veículo pode operar. Próxima manutenção em: " + dataProximaManutencao);
        return true; // Pode operar
    }

    public void agendarManutencaoPreventiva(Veiculo veiculo) {

    }
}
