package br.com.gestaofrota.model.veiculo;

import br.com.gestaofrota.enums.StatusVeiculo;
import br.com.gestaofrota.exception.VeiculoException;

import java.util.Calendar;
import java.util.Date;

public abstract class Veiculo {

    private String placa;
    private String marca;
    private String modelo;
    private Integer ano;
    private StatusVeiculo status;
    private Double quilometragem;
    private Date dataUltimaManutencao = null;

    public Veiculo(String placa, String marca, String modelo, Integer ano) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public StatusVeiculo getStatus() {
        return status;
    }

    public void setStatus(StatusVeiculo status) {
        this.status = status;
    }

    public Double getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(Double quilometragem) {
        this.quilometragem = quilometragem;
    }

    public Date getDataUltimaManutencao() {
        return dataUltimaManutencao;
    }

    public void setDataUltimaManutencao(Date dataUltimaManutencao) {
        this.dataUltimaManutencao = dataUltimaManutencao;
    }

    public void atribuirTarefa() {
        if (status == StatusVeiculo.DISPONIVEL) {
            status = StatusVeiculo.EM_USO;
        } else {
            throw new VeiculoException("O veículo não está disponível para uso.");
        }
    }

    public void liberarVeiculo() {
        if (status == StatusVeiculo.EM_USO) {
            status = StatusVeiculo.DISPONIVEL;
        } else {
            throw new VeiculoException("O veículo não pode ser liberado, pois não está em uso.");
        }
    }

    public void enviarParaManutencao() {
        if (status != StatusVeiculo.EM_USO) {
            status = StatusVeiculo.EM_MANUTENCAO;

            setDataUltimaManutencao(dataUltimaManutencao = new Date());
        }
    }

    public void colocarForaDeServico() {
        if (status != StatusVeiculo.EM_USO) {
            status = StatusVeiculo.FORA_DE_SERVICO;
        }
    }

    public void reativarVeiculo() {
        if (status == StatusVeiculo.FORA_DE_SERVICO || status == StatusVeiculo.EM_MANUTENCAO) {
            status = StatusVeiculo.DISPONIVEL;
        }
    }

    public void atualizarQuilometragem(double km) {
        Double quilometragemAtual = 0.0;
        if (km > 0) {
            quilometragemAtual += getQuilometragem() + km;
        } else {
            throw new VeiculoException("A quilometragem não pode ser negativa.");
        }
        setQuilometragem(quilometragemAtual);
    }

    public abstract double calcularCustoManutencao();

    public boolean necessitaManutencao() {
        // Verifica se a quilometragem ultrapassa 10.000 km
        if (getQuilometragem() > 10000) {
            return true;
        }

        // Verifica se nunca houve manutenção
        if (getDataUltimaManutencao() == null) {
            return true;
        }

        // Obtém a data atual e a data da última manutenção
        Calendar calAtual = Calendar.getInstance(); // Data atual
        Calendar calUltimaManutencao = Calendar.getInstance();
        calUltimaManutencao.setTime(getDataUltimaManutencao()); // Data da última manutenção

        // Calcula a diferença em anos e meses
        int anosDiferenca = calAtual.get(Calendar.YEAR) - calUltimaManutencao.get(Calendar.YEAR);
        int mesesDiferenca = calAtual.get(Calendar.MONTH) - calUltimaManutencao.get(Calendar.MONTH);

        // Calcula o total de meses desde a última manutenção
        int totalMeses = anosDiferenca * 12 + mesesDiferenca;

        // Verifica se passaram mais de 6 meses
        if (totalMeses > 6) {
            return true;
        }

        return false;
    }
}
