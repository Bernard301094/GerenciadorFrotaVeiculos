package br.com.gestaofrota.main;

import br.com.gestaofrota.enums.StatusVeiculo;
import br.com.gestaofrota.enums.TipoCaminhao;
import br.com.gestaofrota.enums.TipoCombustivel;
import br.com.gestaofrota.enums.TipoMotocicleta;
import br.com.gestaofrota.exception.VeiculoException;
import br.com.gestaofrota.model.frota.FrotaVeiculos;
import br.com.gestaofrota.model.veiculo.Caminhao;
import br.com.gestaofrota.model.veiculo.Carro;
import br.com.gestaofrota.model.veiculo.Motocicleta;
import br.com.gestaofrota.model.veiculo.Veiculo;
import br.com.gestaofrota.service.RelatorioFrota;
import br.com.gestaofrota.service.ServicoManutencao;

import java.util.Calendar;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // CRIAÇÃO DOS VEÍCULOS
        System.out.println("=== CRIANDO FROTA DE VEÍCULOS ===\n");

        Carro carro1 = new Carro("RIO2A24", "Hyundai", "HB20", 2023, 4, TipoCombustivel.GASOLINA, true);
        Carro carro2 = new Carro("SAO5B31", "Tesla", "Model 3", 2024, 4, TipoCombustivel.ELETRICO, true);
        Carro carro3 = new Carro("BHZ8C99", "Toyota", "Corolla Cross", 2025, 4, TipoCombustivel.HIBRIDO, true);

        carro1.setQuilometragem(5000.0);
        carro2.setQuilometragem(12000.0);
        carro3.setQuilometragem(8500.0);

        Motocicleta moto1 = new Motocicleta("MTO1D23", "Honda", "PCX", 2024, 160, false, TipoMotocicleta.SCOOTER);
        Motocicleta moto2 = new Motocicleta("MOT2E45", "BMW", "GS 1250", 2023, 1254, true, TipoMotocicleta.TRAIL);
        Motocicleta moto3 = new Motocicleta("MTC3F67", "Haojue", "DK150", 2025, 150, true, TipoMotocicleta.CARGO);

        moto1.setQuilometragem(9000.0);
        moto2.setQuilometragem(3000.0);
        moto3.setQuilometragem(7500.0);

        Caminhao caminhao1 = new Caminhao("TRK8G89", "Mercedes-Benz", "Atego", 2022, 17000.0, 3, false, TipoCaminhao.BAU_SECO);
        Caminhao caminhao2 = new Caminhao("CAM9H01", "Scania", "G 420", 2021, 25000.0, 4, true, TipoCaminhao.BASCULANTE);
        Caminhao caminhao3 = new Caminhao("TRC2I12", "Volvo", "VM 330", 2024, 12000.0, 2, false, TipoCaminhao.TANQUE);

        caminhao1.setQuilometragem(18000.0);
        caminhao2.setQuilometragem(10000.0);
        caminhao3.setQuilometragem(5000.0);

        FrotaVeiculos frotaVeiculos = new FrotaVeiculos(new java.util.ArrayList<>(
                List.of(carro1, carro2, carro3, moto1, moto2, moto3, caminhao1, caminhao2, caminhao3)
        ));

        // Configure status inicial de TODOS como DISPONIVEL
        for (Veiculo veiculo : frotaVeiculos.getVeiculos()) {
            veiculo.setStatus(StatusVeiculo.DISPONIVEL);
        }
        System.out.println("Status inicial configurado para todos os veículos: DISPONIVEL\n");

        // Configurar datas de última manutenção para simular cenários
        Calendar cal = Calendar.getInstance();

        // Veículos com manutenção vencida por tempo
        cal.add(Calendar.MONTH, -8);
        carro2.setDataUltimaManutencao(cal.getTime());

        cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -6);
        moto1.setDataUltimaManutencao(cal.getTime());

        cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -5);
        caminhao1.setDataUltimaManutencao(cal.getTime());

        // Veículos com manutenção recente
        cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        carro1.setDataUltimaManutencao(cal.getTime());
        moto2.setDataUltimaManutencao(cal.getTime());
        caminhao3.setDataUltimaManutencao(cal.getTime());

        System.out.println("=== TESTES DAS OPERAÇÕES BÁSICAS ===\n");

        // TESTE 1: Buscar veículo existente
        System.out.println("1. Buscar veículo existente:");
        try {
            Veiculo encontrado = frotaVeiculos.buscarVeiculo("RIO2A24");
            System.out.println("Veículo encontrado: " + encontrado.getMarca() + " " + encontrado.getModelo());
        } catch (VeiculoException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        // TESTE 2: Buscar veículo inexistente
        System.out.println("\n2. Buscar veículo inexistente:");
        try {
            frotaVeiculos.buscarVeiculo("XXX0000");
        } catch (VeiculoException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        // TESTE 3: Alterar status de alguns veículos
        System.out.println("\n3. Alterando status de alguns veículos:");
        try {
            carro1.atribuirTarefa();
            System.out.println("Carro1 colocado em uso");

            moto2.enviarParaManutencao();
            System.out.println("Moto2 enviada para manutenção");

            caminhao2.colocarForaDeServico();
            System.out.println("Caminhao2 colocado fora de serviço");

        } catch (VeiculoException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        // TESTE 4: Listar veículos por status
        System.out.println("\n4. Veículos DISPONIVEL:");
        for (Veiculo v : frotaVeiculos.listarPorStatus(StatusVeiculo.DISPONIVEL)) {
            System.out.println("- " + v.getPlaca() + " (" + v.getMarca() + " " + v.getModelo() + ")");
        }

        System.out.println("\n5. Veículos EM_USO:");
        for (Veiculo v : frotaVeiculos.listarPorStatus(StatusVeiculo.EM_USO)) {
            System.out.println("- " + v.getPlaca() + " (" + v.getMarca() + " " + v.getModelo() + ")");
        }

        // TESTE 6: Adicionar e Remover Veículos
        System.out.println("\n=== TESTES DE ADIÇÃO E REMOÇÃO ===\n");

        System.out.println("6. Testando adição de novo veículo:");
        try {
            Carro carroNovo = new Carro("ABC1234", "Ford", "Ka", 2023, 4, TipoCombustivel.GASOLINA, false);
            carroNovo.setStatus(StatusVeiculo.DISPONIVEL);
            carroNovo.setQuilometragem(2000.0);
            frotaVeiculos.adicionarVeiculo(carroNovo);
        } catch (VeiculoException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("\n7. Testando adição de veículo com placa duplicada:");
        try {
            Carro carroDuplicado = new Carro("RIO2A24", "Volkswagen", "Gol", 2020, 4, TipoCombustivel.GASOLINA, false);
            frotaVeiculos.adicionarVeiculo(carroDuplicado);
        } catch (VeiculoException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        System.out.println("\n8. Testando remoção de veículo:");
        try {
            frotaVeiculos.removerVeiculo("ABC1234");
        } catch (VeiculoException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("\n9. Testando remoção de veículo inexistente:");
        try {
            frotaVeiculos.removerVeiculo("INEXISTENTE");
        } catch (VeiculoException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        // TESTE 7: Métodos específicos dos veículos
        System.out.println("\n=== TESTES ESPECÍFICOS DOS VEÍCULOS ===\n");

        System.out.println("10. Testando atualização de quilometragem:");
        try {
            System.out.println("Quilometragem atual do Carro3: " + carro3.getQuilometragem());
            carro3.atualizarQuilometragem(500.0);
            System.out.println("Nova quilometragem após adicionar 500km: " + carro3.getQuilometragem());
        } catch (VeiculoException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("\n11. Testando atualização com valor inválido:");
        try {
            carro3.atualizarQuilometragem(-100.0);
        } catch (VeiculoException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        System.out.println("\n12. Testando necessidade de manutenção:");
        System.out.println("Carro1 (5000km) precisa manutenção? " + carro1.necessitaManutencao());
        System.out.println("Carro2 (12000km) precisa manutenção? " + carro2.necessitaManutencao());
        System.out.println("Moto1 (9000km) precisa manutenção? " + moto1.necessitaManutencao());
        System.out.println("Caminhao1 (18000km) precisa manutenção? " + caminhao1.necessitaManutencao());

        // TESTE 8: Métodos específicos por tipo
        System.out.println("\n=== MÉTODOS ESPECÍFICOS POR TIPO ===\n");

        System.out.println("13. Consumo médio dos carros:");
        System.out.println("Carro1 (Gasolina): " + carro1.calcularConsumoMedio() + " km/L");
        System.out.println("Carro2 (Elétrico): " + carro2.calcularConsumoMedio() + " km/kWh");
        System.out.println("Carro3 (Híbrido): " + carro3.calcularConsumoMedio() + " km/L");

        System.out.println("\n14. Inspeção técnica das motocicletas:");
        System.out.println("Moto1 (2024) precisa inspeção? " + moto1.necessitaInspecaoTecnica());
        System.out.println("Moto2 (2023) precisa inspeção? " + moto2.necessitaInspecaoTecnica());
        System.out.println("Moto3 (2025) precisa inspeção? " + moto3.necessitaInspecaoTecnica());

        System.out.println("\n15. Validação de carga dos caminhões:");
        System.out.println("Caminhao1 pode carregar 15000kg? " + caminhao1.validarCapacidadeCarga(15000.0));
        System.out.println("Caminhao1 pode carregar 20000kg? " + caminhao1.validarCapacidadeCarga(20000.0));
        System.out.println("Caminhao2 pode carregar 30000kg? " + caminhao2.validarCapacidadeCarga(30000.0));

        System.out.println("\n16. Custo operacional dos caminhões:");
        System.out.println("Custo operacional diário Caminhao1: R$ " + String.format("%.2f", caminhao1.calcularCustoOperacao()));
        System.out.println("Custo operacional diário Caminhao2: R$ " + String.format("%.2f", caminhao2.calcularCustoOperacao()));

        // TESTE 5: Serviços de Manutenção e Relatórios
        System.out.println("\n=== TESTES DOS SERVIÇOS BÁSICOS ===\n");

        ServicoManutencao servicoManutencao = new ServicoManutencao();
        RelatorioFrota relatorioFrota = new RelatorioFrota();

        // Teste do serviço de manutenção
        System.out.println("17. Calculando próxima manutenção do Carro2:");
        servicoManutencao.calcularProximaManutencao(carro2);

        System.out.println("\n18. Validando status para operação:");
        servicoManutencao.validarStatusParaOperacao(carro1);
        servicoManutencao.validarStatusParaOperacao(carro2);

        // Teste dos relatórios
        System.out.println("\n19. Relatório de Status:");
        System.out.println(relatorioFrota.gerarRelatorioStatus(frotaVeiculos));

        System.out.println("\n20. Relatório de Custos:");
        System.out.println(relatorioFrota.gerarRelatorioCustos(frotaVeiculos));

        // TESTE 9: Mais funcionalidades dos serviços
        System.out.println("\n=== FUNCIONALIDADES AVANÇADAS DOS SERVIÇOS ===\n");

        System.out.println("21. Relatório completo de manutenção:");
        String relatorioManutencao = servicoManutencao.gerarRelatorioManutencao(frotaVeiculos.getVeiculos());
        System.out.println(relatorioManutencao);

        System.out.println("\n22. Agendamento de manutenção preventiva:");
        servicoManutencao.agendarManutencaoPreventiva(carro2);
        servicoManutencao.agendarManutencaoPreventiva(moto1);
        servicoManutencao.agendarManutencaoPreventiva(caminhao1);

        System.out.println("\n23. Relatório de quilometragem:");
        System.out.println(relatorioFrota.gerarRelatorioQuilometragem(frotaVeiculos));

        // TESTE 10: Mais testes de mudança de status
        System.out.println("\n=== TESTES AVANÇADOS DE STATUS ===\n");

        System.out.println("24. Testando liberação de veículo:");
        try {
            System.out.println("Status do Carro1 antes: " + carro1.getStatus());
            carro1.liberarVeiculo();
            System.out.println("Status do Carro1 depois de liberar: " + carro1.getStatus());
        } catch (VeiculoException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("\n25. Testando reativação de veículo:");
        try {
            System.out.println("Status do Caminhao2 antes: " + caminhao2.getStatus());
            caminhao2.reativarVeiculo();
            System.out.println("Status do Caminhao2 depois de reativar: " + caminhao2.getStatus());
        } catch (VeiculoException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("\n26. Testando operação inválida de status:");
        try {
            // Tentar colocar veículo em uso quando não está disponível
            moto2.atribuirTarefa(); // moto2 está EM_MANUTENCAO
        } catch (VeiculoException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        // TESTE 11: Exportação de relatórios
        System.out.println("\n=== TESTE DE EXPORTAÇÃO ===\n");

        System.out.println("27. Exportando relatórios:");
        try {
            String relatorioStatus = relatorioFrota.gerarRelatorioStatus(frotaVeiculos);
            relatorioFrota.exportarRelatorios(relatorioStatus, "TXT");

            String relatorioCustos = relatorioFrota.gerarRelatorioCustos(frotaVeiculos);
            relatorioFrota.exportarRelatorios(relatorioCustos, "CSV");

        } catch (VeiculoException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("\n28. Testando formato inválido:");
        try {
            relatorioFrota.exportarRelatorios("teste", "PDF");
        } catch (VeiculoException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        // TESTE 12: Listagens finais
        System.out.println("\n=== LISTAGENS FINAIS ===\n");

        System.out.println("29. Veículos disponíveis:");
        for (Veiculo v : frotaVeiculos.obterVeiculosDisponiveis()) {
            System.out.println("- " + v.getPlaca() + " (" + v.getMarca() + " " + v.getModelo() + ")");
        }

        System.out.println("\n30. Custo total de manutenção da frota:");
        System.out.println("R$ " + String.format("%.2f", frotaVeiculos.calcularCustoManutencaoTotal()));

        System.out.println("\n31. Todos os status da frota:");
        System.out.println("DISPONIVEL: " + frotaVeiculos.listarPorStatus(StatusVeiculo.DISPONIVEL).size());
        System.out.println("EM_USO: " + frotaVeiculos.listarPorStatus(StatusVeiculo.EM_USO).size());
        System.out.println("EM_MANUTENCAO: " + frotaVeiculos.listarPorStatus(StatusVeiculo.EM_MANUTENCAO).size());
        System.out.println("FORA_DE_SERVICO: " + frotaVeiculos.listarPorStatus(StatusVeiculo.FORA_DE_SERVICO).size());

        System.out.println("\n=== ESTATÍSTICAS FINAIS DA FROTA ===");
        System.out.println(frotaVeiculos.obterEstatisticas());

        System.out.println("\n=== FIM DOS TESTES - SISTEMA COMPLETO DEMONSTRADO ===");
    }
}