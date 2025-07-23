package br.com.gestaofrota.model.veiculo;

import br.com.gestaofrota.enums.TipoCombustivel;

import java.time.Year;

public class Carro extends Veiculo{

    private Integer numeroPortas;
    private TipoCombustivel combustivel;
    private Boolean temArCondicionado;

    public Carro(String placa, String marca, String modelo, Integer ano, Integer numeroPortas, TipoCombustivel combustivel, Boolean temArCondicionado) {
        super(placa, marca, modelo, ano);
        this.numeroPortas = numeroPortas;
        this.combustivel = combustivel;
        this.temArCondicionado = temArCondicionado;
    }

    public Integer getNumeroPortas() {
        return numeroPortas;
    }

    public void setNumeroPortas(Integer numeroPortas) {
        this.numeroPortas = numeroPortas;
    }

    public TipoCombustivel getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(TipoCombustivel combustivel) {
        this.combustivel = combustivel;
    }

    public Boolean getTemArCondicionado() {
        return temArCondicionado;
    }

    public void setTemArCondicionado(Boolean temArCondicionado) {
        this.temArCondicionado = temArCondicionado;
    }

    @Override
    public double calcularCustoManutencao() {
        // 1. Ponto de Partida: Custo base da mão de obra
        double custoTotal = 300.0;

        // 2. Fator Quilometragem (LÓGICA CORRIGIDA)
        // Calcula quantas "revisões de 10.000 km" o carro já passou.
        if (getQuilometragem() > 10000) {
            int fatorQuilometragem = (int) (getQuilometragem() / 10000);
            custoTotal += fatorQuilometragem * 200.0; // Adiciona R$ 200 para cada 10.000 km
        }

        // 3. Fator Idade do Veículo
        int anoAtual = Year.now().getValue();
        int idadeDoVeiculo = anoAtual - getAno();

        if (idadeDoVeiculo > 5 && idadeDoVeiculo <= 10) {
            custoTotal += idadeDoVeiculo * 50.0;
        } else if (idadeDoVeiculo > 10) {
            custoTotal += idadeDoVeiculo * 100.0;
        }

        // 4. Fator Ar Condicionado
        if (temArCondicionado) { // Não precisa de "this." aqui, mas funciona dos dois jeitos
            custoTotal += 100.0;
        }

        // 5. Fator Tipo de Combustível
        switch (getCombustivel()) {
            case DIESEL:
                custoTotal += 150.0;
                break;
            case ELETRICO:
                custoTotal -= 100.0; // Desconto para elétrico
                break;
            case HIBRIDO:
                custoTotal += 75.0;
                break;
            case GASOLINA:
                // Custo padrão, não adiciona nada
                break;
        }

        // Retorna o custo final somando todos os fatores
        return custoTotal;
    }

    public double calcularConsumoMedio() {
        switch (getCombustivel()) {
            case GASOLINA:
                return 12.5; // km/L
            case DIESEL:
                return 10.0; // km/L
            case ELETRICO:
                return 6.5;  // km/kWh (representação)
            case HIBRIDO:
                return 18.0; // km/L
            default:
                return 0.0;  // Retorno padrão caso não seja nenhum dos tipos.
        }
    }

    @Override
    public String toString() {
        // Começa com as informações da classe pai (Veiculo) para reutilizar o código.
        // Para que isso funcione perfeitamente, a classe Veiculo também deve ter um bom toString().
        // Se não tiver, ele pegará o padrão, mas a estrutura ainda é a correta.
        String infoVeiculo = super.toString();

        // Adiciona as informações específicas do Carro
        return infoVeiculo + " | Carro {" +
                "numeroPortas=" + numeroPortas +
                ", combustivel=" + combustivel +
                ", temArCondicionado=" + (temArCondicionado ? "Sim" : "Não") +
                '}';
    }
}
