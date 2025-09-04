package br.com.gestaofrota.model.frota;

import br.com.gestaofrota.enums.StatusVeiculo;
import br.com.gestaofrota.exception.VeiculoException;
import br.com.gestaofrota.model.veiculo.Caminhao;
import br.com.gestaofrota.model.veiculo.Carro;
import br.com.gestaofrota.model.veiculo.Motocicleta;
import br.com.gestaofrota.model.veiculo.Veiculo;

import java.util.ArrayList;
import java.util.List;

public class FrotaVeiculos {

    private List<Veiculo> veiculos;

    public FrotaVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void adicionarVeiculo(Veiculo veiculo) {
        if (veiculo == null) {
            throw new VeiculoException("Veículo não pode ser nulo");
        }

        for(Veiculo v : veiculos) {
            if (v.getPlaca().equals(veiculo.getPlaca())) {
                throw new VeiculoException("Placa já cadastrada");
            }
        }
        veiculos.add(veiculo);
        System.out.println("Veículo adicionado com sucesso");
    }

    public void removerVeiculo(String placa) {
        if (placa == null) {
            throw new VeiculoException("Veículo não pode ser nulo");
        }

        for(Veiculo v : veiculos) {
            if (v.getPlaca().equals(placa)) {
                veiculos.remove(v);
                System.out.println("Veículo removido com sucesso");
                return;
            }
        }
        throw new VeiculoException("Veículo não encontrado");
    }

    public Veiculo buscarVeiculo(String placa) {
        if (placa == null) {
            throw new VeiculoException("Veículo não pode ser nulo");
        }

        for(Veiculo v : veiculos) {
            if (v.getPlaca().equals(placa)) {
                return v;
            }
        }
        throw new VeiculoException("Veículo não encontrado");
    }

    public List<Veiculo> listarPorStatus(StatusVeiculo status) {
        if (status == null) {
            throw new VeiculoException("Status não pode ser nulo");
        }

        ArrayList<Veiculo> veiculosFiltrados = new ArrayList<>();

        for(Veiculo v : veiculos) {
            if (v.getStatus().equals(status)) {
                veiculosFiltrados.add(v);
            }
        }
        return veiculosFiltrados;
    }

    public double calcularCustoManutencaoTotal() {
        double custoTotal = 0.0;

        for(Veiculo v : veiculos) {
            custoTotal += v.calcularCustoManutencao();
        }

        return custoTotal;
    }

    public List<Veiculo> obterVeiculosDisponiveis() {
        if (veiculos == null) {
            throw new VeiculoException("Veículo não pode ser nulo");
        }

        ArrayList<Veiculo> veiculosFiltrados = new ArrayList<>();
        for(Veiculo v : veiculos) {
            if (v.getStatus().equals(StatusVeiculo.DISPONIVEL)) {
                veiculosFiltrados.add(v);
            }
        }

        return veiculosFiltrados;
    }

    public String obterEstatisticas() {
        if (veiculos == null) {
            throw new VeiculoException("Veículo não pode ser nulo");
        }

        int totalVeiculos = 0,
                totalCarros = 0,
                totalMotos = 0,
                totalCaminhoes = 0,
                disponiveis = 0,
                emUso = 0,
                emManutencao = 0,
                foraDeServico = 0;
        
        for (Veiculo v : veiculos) {
            totalVeiculos++;

            switch (v) {
                case Carro carro -> totalCarros++;
                case Motocicleta motocicleta -> totalMotos++;
                case Caminhao caminhao -> totalCaminhoes++;
                default -> {
                }
            }
            switch (v.getStatus()) {
                case DISPONIVEL -> disponiveis++;
                case EM_USO -> emUso++;
                case EM_MANUTENCAO -> emManutencao++;
                case FORA_DE_SERVICO -> foraDeServico++;
                }
            }

        return String.format("""
                === ESTATÍSTICAS DA FROTA ===
                Total de veículos: %d
                Carros: %d | Motos: %d | Caminhões: %d
                Disponíveis: %d | Em uso: %d | Em manutenção: %d | Fora de serviço: %d""",
                totalVeiculos, totalCarros, totalMotos, totalCaminhoes, disponiveis, emUso, emManutencao, foraDeServico);
    }
}
