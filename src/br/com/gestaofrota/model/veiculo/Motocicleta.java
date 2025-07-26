package br.com.gestaofrota.model.veiculo;

import br.com.gestaofrota.enums.TipoMotocicleta;

import java.time.Year;

public class Motocicleta extends Veiculo {

    private int cilindrada;
    private boolean temBagageiro;
    private TipoMotocicleta tipo;

    public Motocicleta(String placa, String marca, String modelo, Integer ano, int cilindrada, boolean temBagageiro, TipoMotocicleta tipo) {
        super(placa, marca, modelo, ano);
        this.cilindrada = cilindrada;
        this.temBagageiro = temBagageiro;
        this.tipo = tipo;
    }

    public int getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }

    public boolean isTemBagageiro() {
        return temBagageiro;
    }

    public void setTemBagageiro(boolean temBagageiro) {
        this.temBagageiro = temBagageiro;
    }

    public TipoMotocicleta getTipo() {
        return tipo;
    }

    public void setTipo(TipoMotocicleta tipo) {
        this.tipo = tipo;
    }

    @Override
    public double calcularCustoManutencao() {
        // 1. PONTO DE PARTIDA: CUSTO BASE
        // Define um custo inicial fixo que representa a mão de obra básica e a inspeção inicial para qualquer motocicleta.
        double custoTotal = 150.0;

        // 2. FATOR QUILOMETRAGEM: DESGASTE POR USO
        // Verifica se a moto já rodou o suficiente para justificar uma manutenção mais aprofundada.
        if (getQuilometragem() != null && getQuilometragem() > 8000) {
            // Calcula quantas "revisões de 8.000 km" a moto já passou.
            // A conversão para (int) ignora as frações, nos dando o número exato de blocos completos de 8.000 km.
            int fatorQuilometragem = (int) (getQuilometragem() / 8000);
            // Adiciona um valor fixo (R$ 120) para cada bloco de 8.000 km, representando o desgaste de peças como pneus, relação e óleo.
            custoTotal += fatorQuilometragem * 120.0;
        }

        // 3. FATOR CILINDRADA: COMPLEXIDADE DO MOTOR
        // Motores maiores (alta cilindrada) geralmente usam peças mais caras e tecnologia mais complexa.
        if (this.cilindrada > 600) {
            // Adiciona um custo fixo para cobrir o valor mais elevado de peças e fluidos para motores de alta performance.
            custoTotal += 150.0;
        }

        // 4. FATOR TIPO DE MOTOCICLETA: ESPECIFICIDADE DE USO
        // O custo da manutenção varia significativamente com a finalidade e o design da motocicleta.
        switch (this.tipo) {
            case ESPORTIVA:
                // Custo adicional elevado devido a peças de performance, pneus especiais e carenagens frágeis.
                custoTotal += 300.0;
                break;
            case TRAIL:
                // Custo adicional para inspecionar e manter sistemas de suspensão de longo curso e componentes reforçados.
                custoTotal += 180.0;
                break;
            case CARGO:
                // Custo adicional devido ao desgaste acentuado em freios, embreagem e estrutura por ser um veículo de trabalho pesado.
                custoTotal += 100.0;
                break;
            case SCOOTER:
                // Custo adicional menor, pois a mecânica é geralmente mais simples e as peças de reposição mais baratas.
                custoTotal += 50.0;
                break;
            case STREET:
                // Considerado o tipo padrão. O custo de sua manutenção já está refletido no valor base e nos outros fatores.
                // Nenhuma ação adicional é necessária.
                break;
        }

        // 5. FATOR IDADE: DESGASTE NATURAL
        int anoAtual = Year.now().getValue(); // Obtém o ano corrente.
        int idadeDaMotocicleta = anoAtual - getAno(); // Calcula a idade do veículo.

        // Se a motocicleta tem mais de 5 anos, adiciona um custo progressivo.
        if (idadeDaMotocicleta > 5) {
            // Multiplica a idade por um fator (R$ 30), tornando a manutenção mais cara à medida que a moto envelhece.
            custoTotal += idadeDaMotocicleta * 30.0;
        }

        // 6. RETORNO DO CUSTO FINAL
        // Retorna a soma de todos os fatores calculados.
        return custoTotal;
    }

    public boolean necessitaInspecaoTecnica() {
        // 1. CALCULAR A IDADE DO VEÍCULO
        // Obtém o ano atual para calcular a idade do veículo.
        int anoAtual = java.time.Year.now().getValue();
        int idadeDoVeiculo = anoAtual - getAno();

        // 2. REGRA PARA VEÍCULOS ISENTOS (NOVOS)
        if (idadeDoVeiculo <= 3) {
            return false;
        }

        // 3. REGRA PARA VEÍCULOS MAIS ANTIGOS (INSPEÇÃO ANUAL)
        if (idadeDoVeiculo > 10) {
            return true;
        }
        // 4. REGRA PARA VEÍCULOS INTERMEDIÁRIOS (INSPEÇÃO BIENAL)
        // Veículos entre 4 e 10 anos devem fazer a inspeção a cada dois anos.
        // A lógica (idadeDoVeiculo % 2 == 0) verifica se a idade do veículo é um número par.
        // Por exemplo, com 4, 6, 8, 10 anos ele precisará de inspeção.
        // Com 5, 7, 9 anos, não.
        if (idadeDoVeiculo > 3 && idadeDoVeiculo <= 10) {
            // Retorna true se a idade for um número par, implementando a verificação a cada 2 anos.
            return idadeDoVeiculo % 2 == 0;
        }
        // Se nenhuma das condições acima for atendida, retorna false por padrão.
        return false;
    }
}
