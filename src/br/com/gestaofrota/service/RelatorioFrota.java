package br.com.gestaofrota.service;

import br.com.gestaofrota.exception.VeiculoException;
import br.com.gestaofrota.model.frota.FrotaVeiculos;
import br.com.gestaofrota.model.veiculo.Caminhao;
import br.com.gestaofrota.model.veiculo.Carro;
import br.com.gestaofrota.model.veiculo.Motocicleta;
import br.com.gestaofrota.model.veiculo.Veiculo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RelatorioFrota {

    public String gerarRelatorioStatus(FrotaVeiculos frota) {

        if (frota == null || frota.getVeiculos() == null || frota.getVeiculos().isEmpty()) {
            throw new VeiculoException("Não há veículos na frota.");
        }

        int disponiveis = 0,
                emUso = 0,
                emManutencao = 0,
                foraDeServico = 0;

        for (Veiculo veiculo : frota.getVeiculos()) {
            switch (veiculo.getStatus()) {
                case DISPONIVEL -> disponiveis++;
                case EM_USO -> emUso++;
                case EM_MANUTENCAO -> emManutencao++;
                case FORA_DE_SERVICO -> foraDeServico++;
            }
        }

        // Criar um array com os valores e nomes
        int[] valores = {disponiveis, emUso, emManutencao, foraDeServico};
        String[] nomes = {"DISPONÍVEL", "EM USO", "MANUTENÇÃO", "FORA DE SERVIÇO"};

        // Encontrar índices do maior e menor
        int indiceMaior = 0, indiceMenor = 0;

        for (int i = 1; i < valores.length; i++) {
            if (valores[i] > valores[indiceMaior]) {
                indiceMaior = i;
            }
            if (valores[i] < valores[indiceMenor]) {
                indiceMenor = i;
            }
        }

        String statusMaisComum = nomes[indiceMaior];
        String statusMenosComum = nomes[indiceMenor];

        // 1. Total de veículos
        int total = disponiveis + emUso + emManutencao + foraDeServico;

        // 2. Percentuais (cuidado com divisão por zero!)
        double percDisponiveis = (total > 0) ? (disponiveis * 100.0 / total) : 0;
        double percEmUso = (total > 0) ? (emUso * 100.0 / total) : 0;
        double percEmManutencao = (total > 0) ? (emManutencao * 100.0 / total) : 0;
        double percForaDeServico = (total > 0) ? (foraDeServico * 100.0 / total) : 0;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataAtual = sdf.format(new Date());

        StringBuilder relatorio = new StringBuilder();

        // Cabeçalho
        relatorio.append("RELATÓRIO DE STATUS - FROTA DE VEÍCULOS\n");
        relatorio.append("Data: ").append(dataAtual).append("\n");
        relatorio.append("=====================================\n");

        // Resumo Geral
        relatorio.append("RESUMO GERAL:\n");
        relatorio.append("- Total de veículos: ").append(total).append("\n");
        relatorio.append("- Status mais comum: ").append(statusMaisComum).append("\n");
        relatorio.append("- Status menos comum: ").append(statusMenosComum).append("\n");

        // Detalhamento
        relatorio.append("DETALHAMENTO:\n");
        relatorio.append(String.format("[DISPONÍVEL]     %d veículos (%.1f%%)\n", disponiveis, percDisponiveis));
        relatorio.append(String.format("[EM USO]         %d veículos (%.1f%%)\n", emUso, percEmUso));
        relatorio.append(String.format("[MANUTENÇÃO]     %d veículos (%.1f%%)\n", emManutencao, percEmManutencao));
        relatorio.append(String.format("[FORA SERVIÇO]   %d veículos (%.1f%%)\n", foraDeServico, percForaDeServico));

        return relatorio.toString();
    }

    public String gerarRelatorioCustos(FrotaVeiculos frota) {
        if (frota == null || frota.getVeiculos() == null || frota.getVeiculos().isEmpty()) {
            throw new VeiculoException("Não há veículos na frota.");
        }

        int quantosCarros = 0,
                quantasMotos = 0,
                quantosCaminhoes = 0;

        double custoTotalCarros = 0.0,
                custoTotalMotos = 0.0,
                custoTotalCaminhoes = 0.0,
                custoTotalFrota = 0.0;

        for (Veiculo veiculo : frota.getVeiculos()) {
            switch (veiculo) {
                case Carro carro -> {
                    custoTotalCarros += carro.calcularCustoManutencao();
                    quantosCarros++;
                }
                case Motocicleta motocicleta -> {
                    custoTotalMotos += motocicleta.calcularCustoManutencao();
                    quantasMotos++;
                }
                case Caminhao caminhao -> {
                    custoTotalCaminhoes += caminhao.calcularCustoManutencao();
                    quantosCaminhoes++;
                }
                default -> {
                }
            }
        }
        custoTotalFrota += custoTotalCaminhoes + custoTotalMotos + custoTotalCarros;

        double mediaGeral = custoTotalFrota / (quantosCarros + quantasMotos + quantosCaminhoes);
        double mediaCarros = 0.0;
        if (quantosCarros > 0) {
            mediaCarros = custoTotalCarros / quantosCarros;
        }

        double mediaMotos = 0.0;
        if (quantasMotos > 0) {
            mediaMotos = custoTotalMotos / quantasMotos;
        }

        double mediaCaminhoes = 0.0;
        if (quantosCaminhoes > 0) {
            mediaCaminhoes = custoTotalCaminhoes / quantosCaminhoes;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataAtual = sdf.format(new Date());

        int totalVeiculos = quantosCarros + quantasMotos + quantosCaminhoes;

        double percCarros = (totalVeiculos > 0) ? (custoTotalCarros * 100.0 / custoTotalFrota) : 0.0;
        double percMotos = (totalVeiculos > 0) ? (custoTotalMotos * 100.0 / custoTotalFrota) : 0.0;
        double percCaminhoes = (totalVeiculos > 0) ? (custoTotalCaminhoes * 100.0 / custoTotalFrota) : 0.0;

        StringBuilder relatorio = new StringBuilder();

        // Cabeçalho
        relatorio.append("=== RELATÓRIO DE CUSTOS DA FROTA ===\n");
        relatorio.append("Data: ").append(dataAtual).append("\n\n");

        // Resumo Geral
        relatorio.append(String.format("- Total de veículos analisados: %d\n", totalVeiculos));
        relatorio.append(String.format("- Custo total da frota: R$ %.2f\n", custoTotalFrota));
        relatorio.append(String.format("- Custo médio por veículo: R$ %.2f\n\n", mediaGeral));

        // Detalhamento
        relatorio.append("BREAKDOWN POR CATEGORIA:\n");
        relatorio.append(String.format("Carros (%d unidades):\n", quantosCarros));
        relatorio.append(String.format("- Custo total: R$ %.2f\n", custoTotalCarros));
        relatorio.append(String.format("- Custo médio: R$ %.2f\n", mediaCarros));
        relatorio.append(String.format("- Percentual do total: %.1f%%\n\n", percCarros));
        relatorio.append(String.format("Motocicletas (%d unidades):\n", quantasMotos));
        relatorio.append(String.format("- Custo total: R$ %.2f\n", custoTotalMotos));
        relatorio.append(String.format("- Custo médio: R$ %.2f\n", mediaMotos));
        relatorio.append(String.format("- Percentual do total: %.1f%%\n\n", percMotos));
        relatorio.append(String.format("Caminhões (%d unidades):\n", quantosCaminhoes));
        relatorio.append(String.format("- Custo total: R$ %.2f\n", custoTotalCaminhoes));
        relatorio.append(String.format("- Custo médio: R$ %.2f\n", mediaCaminhoes));
        relatorio.append(String.format("- Percentual do total: %.1f%%\n", percCaminhoes));

        return relatorio.toString();
    }

    public String gerarRelatorioQuilometragem(FrotaVeiculos frota) {
        if (frota == null || frota.getVeiculos() == null || frota.getVeiculos().isEmpty()) {
            throw new VeiculoException("Não há veículos na frota.");
        }

        int totalCarros = 0, totalMotos = 0, totalCaminhoes = 0;

        double kmTotalFrota = 0.0, totalKmCarros = 0.0, totalKmMotos = 0.0, totalKmCaminhoes = 0.0;

        for (Veiculo veiculo : frota.getVeiculos()) {
            switch (veiculo) {
            case Carro carro -> {
                if (carro.getQuilometragem() != null) {
                    totalKmCarros += carro.getQuilometragem();
                    totalCarros++;
                }
            }
            case Motocicleta motocicleta -> {
                if (motocicleta.getQuilometragem() != null) {
                    totalKmMotos += motocicleta.getQuilometragem();
                    totalMotos++;
                }
            }
            case Caminhao caminhao -> {
                if (caminhao.getQuilometragem() != null) {
                    totalKmCaminhoes += caminhao.getQuilometragem();
                    totalCaminhoes++;
                }
            }
            default -> throw new VeiculoException("Valor inesperado: " + veiculo);
            }
        }

        kmTotalFrota += totalKmCarros + totalKmMotos + totalKmCaminhoes;
        int totalVeiculos = totalCarros + totalMotos + totalCaminhoes;

        if (totalVeiculos == 0) {
            return "Nenhum veículo possui quilometragem registrada.";
        }

        double mediaKmCarros = (totalCarros > 0) ? totalKmCarros / totalCarros : 0.0,
                mediaKmMotos = (totalMotos > 0) ? totalKmMotos / totalMotos : 0.0,
                mediaKmCaminhoes = (totalCaminhoes > 0) ? totalKmCaminhoes / totalCaminhoes : 0.0,
                mediaKmGeral = (totalVeiculos > 0) ? kmTotalFrota / totalVeiculos : 0.0;

        double percCarros = (kmTotalFrota > 0) ? (totalKmCarros * 100.0 / kmTotalFrota) : 0.0;
        double percMotos = (kmTotalFrota > 0) ? (totalKmMotos * 100.0 / kmTotalFrota) : 0.0;
        double percCaminhoes = (kmTotalFrota > 0) ? (totalKmCaminhoes * 100.0 / kmTotalFrota) : 0.0;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataAtual = sdf.format(new Date());

        StringBuilder relatorio = new StringBuilder();

        relatorio.append("=== RELATÓRIO DE QUILOMETRAGEM DA FROTA ===\n");
        relatorio.append("Data: ").append(dataAtual).append("\n\n");

        relatorio.append("RESUMO GERAL:\n");
        relatorio.append(String.format("- Total de veículos: %d\n", totalVeiculos));
        relatorio.append(String.format("- Quilometragem total da frota: %.2f km\n", kmTotalFrota));
        relatorio.append(String.format("- Quilometragem média da frota: %.2f km\n\n", mediaKmGeral));

        relatorio.append("BREAKDOWN POR CATEGORIA:\n");
        relatorio.append(String.format("Carros (%d unidades):\n", totalCarros));
        relatorio.append(String.format("- Quilometragem total: %.2f km\n", totalKmCarros));
        relatorio.append(String.format("- Quilometragem média: %.2f km\n", mediaKmCarros));
        relatorio.append(String.format("- Percentual do total: %.1f%%\n\n", percCarros));

        relatorio.append(String.format("Motocicletas (%d unidades):\n", totalMotos));
        relatorio.append(String.format("- Quilometragem total: %.2f km\n", totalKmMotos));
        relatorio.append(String.format("- Quilometragem média: %.2f km\n", mediaKmMotos));
        relatorio.append(String.format("- Percentual do total: %.1f%%\n\n", percMotos));

        relatorio.append(String.format("Caminhões (%d unidades):\n", totalCaminhoes));
        relatorio.append(String.format("- Quilometragem total: %.2f km\n", totalKmCaminhoes));
        relatorio.append(String.format("- Quilometragem média: %.2f km\n", mediaKmCaminhoes));
        relatorio.append(String.format("- Percentual do total: %.1f%%\n\n", percCaminhoes));


        return relatorio.toString();
    }

    public void exportarRelatorios(String conteudo, String formato) {

    }
}
