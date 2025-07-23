# Gerenciador de Frota de Veículos

Este é um projeto em Java para o gerenciamento de uma frota de veículos. Ele permite cadastrar diferentes tipos de veículos (carros, motocicletas e caminhões), controlar seus status, registrar quilometragem e gerenciar a manutenção.

## 📈 Diagrama de Classes

## ✨ Funcionalidades

* **Cadastro de Veículos**: Permite a criação de diferentes tipos de veículos, herdando atributos comuns da classe abstrata `Veiculo`.
* **Gerenciamento de Status**: Controla o ciclo de vida de um veículo através de status bem definidos:
    * `DISPONIVEL`
    * `EM_USO`
    * `EM_MANUTENCAO`
    * `FORA_DE_SERVICO`
* **Controle de Manutenção**: O sistema identifica a necessidade de manutenção com base em duas regras:
    1.  Quilometragem superior a 10.000 km.
    2.  Última manutenção realizada há mais de 6 meses.
* **Polimorfismo**: Utiliza uma classe `Veiculo` abstrata com o método `calcularCustoManutencao()`, permitindo que cada tipo de veículo (Carro, Caminhão, Motocicleta) implemente seu próprio cálculo de custo.
* **Tratamento de Exceções**: Usa uma exceção customizada, `VeiculoException`, para lidar com erros de regras de negócio, como tentar usar um veículo indisponível.

## 📂 Estrutura do Projeto

O projeto é organizado nos seguintes pacotes:

* `br.com.gestaofrota.model`: Contém as classes de modelo que representam as entidades do sistema.
    * `veiculo`: Classes `Veiculo` (abstrata), `Carro`, `Motocicleta` e `Caminhao`.
    * `frota`: Classe `FrotaVeiculos` para gerenciar a coleção de veículos.
* `br.com.gestaofrota.enums`: Enumerações para garantir a consistência de dados, como `StatusVeiculo`, `TipoCombustivel`, `TipoCaminhao` e `TipoMotocicleta`.
* `br.com.gestaofrota.service`: Pacote para as classes de serviço que contêm a lógica de negócio, como `ServicoManutencao` e `RelatorioFrota`.
* `br.com.gestaofrota.exception`: Define exceções personalizadas para o projeto, como `VeiculoException`.
* `br.com.gestaofrota.main`: Contém a classe `Main` para a execução da aplicação.

## 🚀 Como Usar

Para utilizar as classes do projeto, você pode instanciar um veículo (como `Carro`, por exemplo) e utilizar os métodos da classe base `Veiculo` para gerenciar seu estado.

**Exemplo:**

```java
// É necessário implementar a classe Carro para que este código funcione
import br.com.gestaofrota.model.veiculo.Carro;
import br.com.gestaofrota.enums.StatusVeiculo;

public class Main {
    public static void main(String[] args) {
        // Cria uma instância de Carro (supondo que a classe já foi implementada)
        Carro meuCarro = new Carro("ABC-1234", "Fiat", "Uno", 2020);

        // Define o status inicial
        meuCarro.setStatus(StatusVeiculo.DISPONIVEL);
        System.out.println("Status atual: " + meuCarro.getStatus()); // Saída: DISPONIVEL

        // Atribui uma tarefa ao veículo
        meuCarro.atribuirTarefa();
        System.out.println("Status após atribuir tarefa: " + meuCarro.getStatus()); // Saída: EM_USO

        // Atualiza a quilometragem
        meuCarro.setQuilometragem(5000.0);
        meuCarro.atualizarQuilometragem(200.0);
        System.out.println("Quilometragem atual: " + meuCarro.getQuilometragem()); // Saída: 5200.0

        // Libera o veículo
        meuCarro.liberarVeiculo();
        System.out.println("Status final: " + meuCarro.getStatus()); // Saída: DISPONIVEL
    }
}