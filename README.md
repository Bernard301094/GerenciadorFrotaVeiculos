# Gerenciador de Frota de Veículos

![Java](https://img.shields.io/badge/Java-21-orange)
![Status](https://img.shields.io/badge/Status-Completo-green)
![License](https://img.shields.io/badge/License-MIT-blue)

Sistema completo para gerenciamento de frota de veículos desenvolvido em Java 21, utilizando recursos modernos da linguagem como pattern matching e programação orientada a objetos avançada.

## 📋 Índice

- [Características](#-características)
- [Funcionalidades](#-funcionalidades)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Como Executar](#-como-executar)
- [Exemplos de Uso](#-exemplos-de-uso)
- [Documentação Técnica](#-documentação-técnica)
- [Contribuindo](#-contribuindo)

## ✨ Características

- **Java 21**: Utiliza recursos modernos como pattern matching em switch expressions
- **Orientação a Objetos**: Implementação completa com herança, polimorfismo e encapsulamento
- **Tratamento de Exceções**: Sistema robusto com exceções customizadas
- **Arquitetura Limpa**: Separação clara entre modelo, serviços e apresentação
- **Sistema de Relatórios**: Geração e exportação de relatórios em múltiplos formatos

## 🚀 Funcionalidades

### Gerenciamento de Veículos
- ✅ **Cadastro Completo**: Suporte para Carros, Motocicletas e Caminhões
- ✅ **Controle de Status**: DISPONÍVEL, EM_USO, EM_MANUTENÇÃO, FORA_DE_SERVIÇO
- ✅ **Gestão de Quilometragem**: Atualização e monitoramento automático
- ✅ **Busca e Filtros**: Localização por placa e filtros por status

### Sistema de Manutenção Inteligente
- 🔧 **Cálculo Automático**: Próxima manutenção baseada em tempo e quilometragem
- 🔧 **Validação de Operação**: Verificação se veículo pode ser usado
- 🔧 **Agendamento Preventivo**: Sistema de agendamento automático
- 🔧 **Alertas**: Notificações para manutenções vencidas ou próximas

### Relatórios Avançados
- 📊 **Relatório de Status**: Distribuição da frota por status
- 📊 **Relatório de Custos**: Análise financeira de manutenção
- 📊 **Relatório de Quilometragem**: Estatísticas de uso da frota
- 📊 **Exportação**: Suporte para TXT, CSV e JSON

### Funcionalidades Específicas por Tipo

#### 🚗 Carros
- Cálculo de consumo médio por tipo de combustível
- Custos baseados em idade, quilometragem e equipamentos
- Suporte para: Gasolina, Diesel, Elétrico, Híbrido

#### 🏍️ Motocicletas
- Sistema de inspeção técnica por idade
- Custos baseados em cilindrada e tipo de uso
- Tipos: Esportiva, Street, Scooter, Cargo, Trail

#### 🚛 Caminhões
- Validação de capacidade de carga
- Cálculo de custo operacional diário
- Tipos: Baú Seco, Frigorífico, Tanque, Basculante, Guincho, Prancha

## 📂 Estrutura do Projeto

```
src/br/com/gestaofrota/
├── model/
│   ├── veiculo/
│   │   ├── Veiculo.java          # Classe abstrata base
│   │   ├── Carro.java            # Implementação para carros
│   │   ├── Motocicleta.java      # Implementação para motos
│   │   └── Caminhao.java         # Implementação para caminhões
│   └── frota/
│       └── FrotaVeiculos.java    # Gerenciador da coleção
├── enums/
│   ├── StatusVeiculo.java        # Estados dos veículos
│   ├── TipoCombustivel.java      # Tipos de combustível
│   ├── TipoCaminhao.java         # Categorias de caminhão
│   └── TipoMotocicleta.java      # Categorias de moto
├── service/
│   ├── ServicoManutencao.java    # Lógica de manutenção
│   └── RelatorioFrota.java       # Geração de relatórios
├── exception/
│   └── VeiculoException.java     # Exceções do domínio
└── main/
    └── Main.java                 # Demonstração completa
```

## 🔧 Como Executar

### Pré-requisitos
- **Java 21** ou superior
- IDE compatível (IntelliJ IDEA, Eclipse, VS Code)

### Passos
1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/GerenciadorFrotaVeiculos.git
cd GerenciadorFrotaVeiculos
```

2. Compile o projeto:
```bash
javac -d out src/br/com/gestaofrota/main/Main.java
```

3. Execute a demonstração:
```bash
java -cp out br.com.gestaofrota.main.Main
```

## 💡 Exemplos de Uso

### Criando e Gerenciando Veículos

```java
// Criar diferentes tipos de veículos
Carro carro = new Carro("ABC1234", "Honda", "Civic", 2023, 
    4, TipoCombustivel.GASOLINA, true);
Motocicleta moto = new Motocicleta("MTO5678", "Yamaha", "MT-07", 2024, 
    689, false, TipoMotocicleta.STREET);
Caminhao caminhao = new Caminhao("CAM9012", "Volvo", "FH", 2022, 
    25000.0, 3, false, TipoCaminhao.BAU_SECO);

// Criar frota e adicionar veículos
List<Veiculo> lista = List.of(carro, moto, caminhao);
FrotaVeiculos frota = new FrotaVeiculos(new ArrayList<>(lista));

// Operações básicas
carro.setStatus(StatusVeiculo.DISPONIVEL);
carro.atribuirTarefa(); // Status muda para EM_USO
carro.atualizarQuilometragem(150.0);
```

### Sistema de Manutenção

```java
ServicoManutencao servico = new ServicoManutencao();

// Calcular próxima manutenção
Date proximaManutencao = servico.calcularProximaManutencao(carro);

// Validar se pode operar
boolean podeOperar = servico.validarStatusParaOperacao(carro);

// Agendar manutenção preventiva
servico.agendarManutencaoPreventiva(carro);

// Gerar relatório completo
String relatorio = servico.gerarRelatorioManutencao(frota.getVeiculos());
```

### Geração de Relatórios

```java
RelatorioFrota relatorio = new RelatorioFrota();

// Diferentes tipos de relatório
String statusReport = relatorio.gerarRelatorioStatus(frota);
String costReport = relatorio.gerarRelatorioCustos(frota);
String kmReport = relatorio.gerarRelatorioQuilometragem(frota);

// Exportar para arquivo
relatorio.exportarRelatorios(statusReport, "CSV");
relatorio.exportarRelatorios(costReport, "JSON");
```

## 📋 Documentação Técnica

### Algoritmo de Cálculo de Manutenção

O sistema utiliza um algoritmo duplo que considera:

1. **Critério de Tempo**: Baseado na data da última manutenção
    - Carros: 6 meses
    - Motocicletas: 4 meses
    - Caminhões: 3 meses

2. **Critério de Quilometragem**: Baseado no uso
    - Carros: 10.000 km
    - Motocicletas: 8.000 km
    - Caminhões: 15.000 km

A próxima manutenção é sempre calculada pelo critério que resultar na data mais próxima.

### Cálculo de Custos

Cada tipo de veículo implementa sua própria lógica:

- **Carros**: Custo base + fator quilometragem + idade + combustível + ar condicionado
- **Motocicletas**: Custo base + quilometragem + cilindrada + tipo + idade
- **Caminhões**: Custo base + quilometragem + capacidade + tipo + idade

### Pattern Matching (Java 21)

O projeto utiliza recursos modernos do Java 21:

```java
switch (veiculo) {
    case Carro c -> processarCarro(c);
    case Motocicleta m -> processarMoto(m);
    case Caminhao c -> processarCaminhao(c);
    default -> throw new VeiculoException("Tipo não suportado");
}
```

## 🎯 Funcionalidades Demonstradas

A classe `Main.java` contém uma demonstração completa com **31 testes** cobrindo:

- ✅ Operações CRUD de veículos
- ✅ Mudanças de status e validações
- ✅ Cálculos específicos por tipo
- ✅ Sistema de manutenção completo
- ✅ Geração e exportação de relatórios
- ✅ Tratamento de exceções
- ✅ Estatísticas da frota

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 👨‍💻 Autor

Desenvolvido com ❤️ por [Bernard301094](https://github.com/Bernard301094)

---

**⚠️ Nota**: Este projeto foi desenvolvido para fins educacionais e demonstra conceitos avançados de programação orientada a objetos em Java 21.