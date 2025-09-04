package br.com.gestaofrota.model.frota;

import br.com.gestaofrota.enums.StatusVeiculo;
import br.com.gestaofrota.exception.VeiculoException;
import br.com.gestaofrota.model.veiculo.Veiculo;

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

    }

    public Veiculo buscarVeiculo(String placa) {

        return null;
    }

    public List<Veiculo> listarPorStatus(StatusVeiculo status) {
        return null;
    }

    public double calcularCustoManutencaoTotal() {
        return 0;
    }

    public List<Veiculo> obterVeiculosDisponiveis() {
        return null;
    }

    public String obterEstatisticas() {

        return "";
    }
}
