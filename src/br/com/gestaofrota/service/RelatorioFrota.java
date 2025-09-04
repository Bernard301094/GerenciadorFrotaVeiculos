package br.com.gestaofrota.service;

import br.com.gestaofrota.exception.VeiculoException;
import br.com.gestaofrota.model.frota.FrotaVeiculos;
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

        return "";
    }

    public String gerarRelatorioQuilometragem(FrotaVeiculos frota) {

        return "";
    }

    public void exportarRelatorios(String conteudo, String formato) {

    }
}
