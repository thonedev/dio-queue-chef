# 🍽️ DIO Queue Chef

Projeto desenvolvido no contexto do bootcamp da **Digital Innovation One (DIO)** com foco em simular um sistema de **gerenciamento de filas de pedidos em uma cozinha**. O projeto utiliza conceitos de programação orientada a objetos, filas (queues), boas práticas com Java e simula um fluxo básico de preparação de pratos.

## 📌 Descrição

O **QueueChef** é um projeto didático desenvolvido em Java para gerenciar filas de preparação, estágios e pedidos em um sistema de cozinha ou produção. Ele utiliza uma abordagem orientada a objetos, banco de dados MySQL e o Flyway para migrações de banco de dados. Este projeto foi criado como um exercício para explorar conceitos de programação, persistência de dados e interação com o usuário via console.

---

## 🧩 Funcionalidades

- Exibir todas as filas de preparação.
- Exibir estágios de uma fila específica.
- Criar novas filas de preparação, estágios e pedidos.
- Mover pedidos entre estágios.
- Cancelar pedidos.
- Adicionar e remover bloqueios (holds) em pedidos.
- Interface de menu interativa no console.

---

## 🛠️ Tecnologias Utilizadas

- **Java**: Linguagem principal do projeto.
- **MySQL**: Banco de dados relacional para persistência.
- **Flyway**: Gerenciamento de migrações de banco de dados.
- **Gradle**: Ferramenta de automação de build.
- **Lombok**: Biblioteca para redução de código boilerplate.
  

---

## 🧱 Estrutura do Projeto  

```
queuechef/
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties  # Configuração do Gradle Wrapper
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── dio/queuechef/
│   │   │       ├── entity/           # Entidades do domínio (Order, Stage, etc.)
│   │   │       ├── persistence/      # Camada de persistência (DAOs)
│   │   │       ├── service/          # Lógica de negócios
│   │   │       └── Application.java  # Ponto de entrada da aplicação
│   │   └── resources/
│   │       └── application.properties # Configurações da aplicação
```

## ❗Pré-requisitos

- **Java 17+**: Certifique-se de ter o JDK instalado.
- **MySQL**: Um servidor MySQL rodando localmente com um banco de dados chamado `queuechef_db`.
- **Gradle**: O projeto usa o Gradle Wrapper, então não é necessário instalar o Gradle separadamente.

## ⚙️ Configuração

1. **Clone o Repositório**
```bash
git clone https://github.com/thonedev/dio-queue-chef
cd dio-queue-chef
```
2. **Configure o Banco de Dados**
  * Crie um banco de dados MySQL chamado queuechef_db
```sql
CREATE DATABASE queuechef_db;
```

* Verifique as credenciais no arquivo Application.java:
```java
Flyway.configure().dataSource("jdbc:mysql://localhost/queuechef_db", "root", "root").load();
```
Ajuste o usuário (root) e senha (root) conforme sua configuração local.

3. **Execute o Projeto**
* Use o Gradle Wrapper para compilar e rodar:
```bash
./gradlew run
```
* O Flyway aplicará as migrações automaticamente (presumindo que os scripts de migração estejam em src/main/resources/db/migration, embora não incluídos no exemplo).

## 🧠 Uso

Ao iniciar, o sistema exibe um menu interativo no console:
```bash
-------+++=== Fila de Preparo Sys ===+++-------
1 - Exibir todas as filas de preparação
2 - Exibir estágios de uma fila específica
3 - Criar nova fila de preparação
4 - Criar novo estágio
5 - Criar novo pedido
6 - Mover pedido para próximo estágio
7 - Cancelar pedido
8 - Adicionar bloqueio a um pedido
9 - Remover bloqueio de um pedido
10 - Sair
-----------------------------------------------
Escolha uma opção:
```

* Digite o número da opção desejada e siga as instruções exibidas.

* Pressione Enter para continuar após cada ação.

## 📊 Migrações do Banco de Dados
O projeto utiliza o Flyway para gerenciar as migrações do banco de dados. Os scripts estão localizados em src/main/resources/db/migration e são aplicados automaticamente ao iniciar a aplicação. Abaixo está a lista de migrações incluídas:

- **V202503211230__hold_record.sql** Cria a tabela hold_record para registrar bloqueios de pedidos.
- **V202503211740__alter_hold_record.sql** Altera os campos release_date e hold_date para o tipo DATETIME.
- **V202503231946__preparation_queue_table.sql** Cria a tabela preparation_queue para filas de preparação.
- **V202503232003__stage_tabel.sql** Cria a tabela stage com uma chave estrangeira para preparation_queue.
- **V202503232026__alter_stage.sql** Renomeia a coluna position para stage_order na tabela stage.
- **V202503232130__order_table.sql** Cria a tabela order para pedidos, com chave estrangeira para stage.
- **V202503232133__add_constraints_hold_record.sql** Adiciona a coluna order_id e uma chave estrangeira para order na tabela hold_record.
- **V202504041717__setup_stage.sql** Insere dados iniciais: uma fila "Main" e estágios padrão ("Recebimento", "Em Progresso", "Concluído", "Cancelado").


## 🗃️ Modelo de Dados
* PreparationQueue: Representa uma fila de preparação com um nome e uma lista de estágios.
* Stage: Um estágio dentro de uma fila, com nome, ordem e lista de pedidos.
* Order: Um pedido com número, itens, data de criação e status de bloqueio.
* HoldRecord: Registro de bloqueio de um pedido, com motivo e datas.

## 🚧 Limitações
* Interface limitada ao console.
* Conexão ao banco de dados hardcoded (sem suporte a variáveis de ambiente).
* Falta de tratamento avançado de erros em algumas operações.

## 🤝 Contribuição

Se desejar contribuir, sinta-se à vontade para abrir **issues** ou **pull requests**!














