# Auto Center Silva - Sistema de Gestão Desktop (Swing GUI)

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-4A90D9?style=for-the-badge&logo=java&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-6.4-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-12.3-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![BCrypt](https://img.shields.io/badge/BCrypt-Senhas_Seguras-16A34A?style=for-the-badge)

> Sistema desktop completo de gestão interna para a Auto Center Silva, desenvolvido em Java com interface gráfica Swing. Conta com autenticação por perfis (Admin, Atendente, Mecânico), senhas criptografadas com BCrypt, persistência em PostgreSQL via Hibernate ORM, migrações automáticas com Flyway e telas dedicadas para gerenciar clientes, automóveis, produtos, serviços, ordens de serviço e usuários.

---

## Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Novidades em relação à versão CLI](#-novidades-em-relação-à-versão-cli)
- [Controle de Acesso por Perfil](#-controle-de-acesso-por-perfil)
- [Telas e Funcionalidades](#-telas-e-funcionalidades)
- [Arquitetura](#-arquitetura)
- [Modelo de Dados](#-modelo-de-dados)
- [Design System](#-design-system)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Configuração do Banco de Dados](#-configuração-do-banco-de-dados)
- [Como Executar](#-como-executar)
- [Credenciais Padrão](#-credenciais-padrão)
- [Equipe](#-equipe)

---

## Sobre o Projeto

O **Auto Center Silva Sistema Desktop** é a terceira geração do sistema de gestão da oficina mecânica, evoluindo da interface de terminal (CLI) para uma **aplicação desktop completa com interface gráfica Swing**. O sistema gerencia o ciclo operacional completo da oficina: do cadastro de clientes e veículos até a abertura, edição e finalização de ordens de serviço — tudo por meio de janelas, formulários, tabelas e botões padronizados com um design system próprio.

A arquitetura segue o padrão em camadas com entidades JPA, repositórios, serviços de negócio e telas Swing completamente separadas. As senhas são armazenadas com hash **BCrypt** e o banco é gerenciado via Flyway.

---

## Novidades em relação à versão CLI

| Aspecto | Versão CLI (v2) | Versão Desktop (v3) |
|---|---|---|
| **Interface** | Terminal (Scanner) | GUI Swing com janelas, botões e tabelas |
| **Autenticação** | Texto plano | Hash **BCrypt** (fator 12) |
| **Perfis de usuário** | Somente Admin | Admin, Atendente e Mecânico |
| **Controle de acesso** | Nenhum | Menu dinâmico por perfil |
| **Tela de Login** | Terminal | `JDialog` modal com 3 tentativas e bloqueio |
| **Gerenciamento de usuários** | Básico | CRUD completo com proteção anti-auto-exclusão |
| **Herança de usuários** | Simples | `SINGLE_TABLE` com `@DiscriminatorColumn` |
| **Design** | Cores ANSI | Design System (`EstiloApp`) com paleta, fontes e fábrica de botões |
| **Feedback** | `System.out` | `JOptionPane` com sucesso ✅, aviso ⚠️, erro ❌ e confirmação |
| **Laudo técnico** | Presente | Removido (simplificação do escopo) |

---

## Controle de Acesso por Perfil

O menu principal exibe botões dinamicamente de acordo com o perfil do usuário logado:

| Perfil | Menu disponível |
|---|---|
| **Admin** | Clientes · Automóveis · Produtos · Serviços · Ordens de Serviço · **Gerenciar Usuários** · Sair |
| **Atendente** | Clientes · Automóveis · Produtos · Serviços · Ordens de Serviço · Sair |
| **Mecânico** | **Somente** Ordens de Serviço · Sair |

O perfil é exibido no cabeçalho da tela principal no formato `Logado como: [ ADMIN ] Nome | email`.

---

## Telas e Funcionalidades

### Tela de Login (`TelaLogin`)
- `JDialog` modal, 420×300px, não redimensionável
- Campos de e-mail e senha; login também acionado com **Enter**
- **3 tentativas** — erro exibe contador regressivo; ao esgotar, encerra o sistema
- Validação de campos vazios antes da requisição ao banco
- Senha verificada com BCrypt via `AuthService`

### Tela Principal (`TelaPrincipal`)
- `JFrame` 720×540px (mínimo 640×480), centralizado na tela
- Cabeçalho azul com título e perfil do usuário logado
- Grade de botões gerada dinamicamente por perfil (`GridLayout` adaptativo)
- Cada botão abre a tela correspondente como janela filho centralizada

### Tela de Clientes (`TelaCliente`)
| Ação | Detalhe |
|---|---|
| Cadastrar | Nome, telefone, CPF com validação completa dos dois dígitos verificadores |
| Listar | Tabela com ID, nome, CPF formatado e telefone |
| Atualizar | Formulário pré-preenchido com dados atuais; valida CPF e verifica duplicata |
| Remover | Bloqueado se o cliente possuir OS em aberto (lista os IDs das OS na mensagem de erro) |
| Buscar por nome | Filtro em tempo real na tabela |

### Tela de Automóveis (`TelaAutomovel`)
| Ação | Detalhe |
|---|---|
| Cadastrar | Modelo, placa e ano de fabricação |
| Listar | Tabela com ID, modelo, placa, ano e cliente vinculado |
| Atualizar | Edição dos campos individualmente |
| Vincular cliente | ComboBox com lista de clientes para associação |
| Remover vínculo | Desvincula o automóvel do cliente sem excluí-lo |
| Remover | Exclui o automóvel do sistema |

### Tela de Produtos (`TelaProduto`)
| Ação | Detalhe |
|---|---|
| Cadastrar | Nome, preço, estoque, categoria e marca |
| Listar | Tabela com todos os campos; exibe alerta se estoque < 5 |
| Atualizar | Campos individuais por categoria (pneus têm submenu de medida e marca) |
| Remover | Remove pelo ID selecionado na tabela |
| Buscar por nome | Filtro na tabela |
| Buscar por categoria | Filtro na tabela |
| Relatório | Totais, preço médio, estoque total e valor total do inventário |

### Tela de Serviços (`TelaServico`)
| Ação | Detalhe |
|---|---|
| Cadastrar | Nome, preço, duração em minutos e tipo |
| Listar | Tabela completa |
| Atualizar | Edição campo a campo |
| Remover | Remove pelo ID |
| Buscar por nome / tipo | Filtros na tabela |
| Relatório | Total, preço médio, duração média |

### Tela de Ordens de Serviço (`TelaOrdemServico`)
| Ação | Detalhe |
|---|---|
| Criar OS | Seleciona cliente e automóvel via ComboBox; bloqueia se já há OS ABERTA para o veículo |
| Listar OS | Tabela com ID, cliente, veículo, status, data de abertura e valor total calculado |
| Adicionar serviço | Seleciona serviço da lista; bloqueado para OS FINALIZADA/CANCELADA |
| Adicionar produto | Seleciona produto; verifica estoque > 0 e **decrementa automaticamente** ao adicionar |
| Finalizar OS | Muda status para FINALIZADA e registra `dataFinalizacao = hoje` |
| Cancelar OS | Muda status para CANCELADA com confirmação |
| Remover OS | Exclui a OS e desvincula itens |

### Tela de Usuários (`TelaUsuario`) — *somente Admin*
| Ação | Detalhe |
|---|---|
| Cadastrar | Nome, e-mail, senha e perfil (Admin / Atendente / Mecânico) |
| Listar | Tabela com ID, nome, e-mail e tipo de perfil |
| Atualizar | Nome, e-mail e nova senha (opcional); valida duplicata de e-mail |
| Remover | Bloqueado para o próprio usuário logado (anti-auto-exclusão) |

---

## Arquitetura

O projeto segue o padrão **em camadas** com 6 pacotes bem definidos:

```
┌──────────────────────────────────────────────────────┐
│  view/telas/  →  Telas Swing (JFrame / JDialog)      │
│  Componentes: EstiloApp (design), Mensagens (alerts)  │
├──────────────────────────────────────────────────────┤
│  iniciadorSistema/  →  Bootstrap: Flyway → Login →   │
│                         injeção de serviços → JFrame  │
├──────────────────────────────────────────────────────┤
│  service/  →  Lógica de negócio + regras de validação│
│  auth · automovel · camerasDeAr · cliente ·          │
│  ordemservico · produto · servico · usuario           │
├──────────────────────────────────────────────────────┤
│  repositories/  →  Acesso ao banco via Hibernate     │
│  Automovel · Cliente · OS · Produto · Servico ·      │
│  Usuario                                              │
├──────────────────────────────────────────────────────┤
│  entity/  →  Entidades JPA (mapeadas para PostgreSQL) │
│  Item* · Produto · Servico · Cliente · Automovel ·   │
│  OrdemServico · Usuario* · Admin · Atendente ·       │
│  Mecanico  (* = classe abstrata / MappedSuperclass)   │
├──────────────────────────────────────────────────────┤
│  config/  →  Infraestrutura                          │
│  EntityManagerConfiguracao · FlyWayConfiguracao      │
└──────────────────────────────────────────────────────┘
         util/  →  ValidadorCpf (validação + formatação + limpeza)
      interfaces/  →  CamerasDeAr (contrato para câmaras de ar)
```

### Fluxo de Inicialização

```
Main.main()
  └── IniciarSistemaSwing.iniciar()
        ├── Aplica LookAndFeel Nimbus
        ├── testarConexao() → SELECT 1 no banco (erro = diálogo + saída)
        ├── FlyWayConfiguracao.migrate() → executa V1 e V2
        ├── Instancia todos os serviços
        └── SwingUtilities.invokeLater()
              ├── TelaLogin.exibir() → modal bloqueante
              │     └── usuário == null → System.exit(0)
              └── TelaPrincipal(serviços, usuárioLogado).setVisible(true)
```

---

## Modelo de Dados

### Diagrama de Entidades

```
usuario (SINGLE_TABLE)
  ├── Admin          (tipo_usuario = 'ADMIN')
  ├── Atendente      (tipo_usuario = 'ATENDENTE')
  └── Mecanico       (tipo_usuario = 'MECANICO')

cliente (1) ──── @OneToMany ──── (N) automovel
   │                                    │
   │ @ManyToOne                         │ @ManyToOne
   │                                    │
ordem_servico (N) ──────────────────────┘
   │
   ├── @ManyToMany ── os_servico  ── servico
   └── @ManyToMany ── os_produto  ── produto
```

### Herança de Usuários — `SINGLE_TABLE`

```
Tabela: usuario
┌────┬─────────────────┬──────┬──────────────────────┬───────────────┐
│ id │ tipo_usuario    │ nome │ email                 │ senha (BCrypt)│
├────┼─────────────────┼──────┼──────────────────────┼───────────────┤
│  1 │ ADMIN           │ ...  │ admin@oficina.com     │ $2a$12$...    │
│  2 │ ATENDENTE       │ ...  │ atendente@...         │ $2a$12$...    │
│  3 │ MECANICO        │ ...  │ mecanico@...          │ $2a$12$...    │
└────┴─────────────────┴──────┴──────────────────────┴───────────────┘
```

### Tabelas Principais

| Tabela | Campos-chave |
|---|---|
| `usuario` | `id`, `tipo_usuario` (discriminador), `nome`, `email` UNIQUE, `senha` (BCrypt) |
| `cliente` | `id`, `nome`, `telefone`, `cpf` |
| `automovel` | `id`, `modelo`, `placa`, `ano_fabricacao`, `cliente_id` (FK nullable) |
| `produto` | `id`, `nome`, `preco`, `quantidade_estoque`, `categoria`, `marca` |
| `servico` | `id`, `nome`, `preco`, `duracao_minutos`, `tipo` |
| `ordem_servico` | `id`, `data_abertura`, `data_finalizacao`, `status`, `cliente_id`, `automovel_id` |
| `os_servico` | `ordem_servico_id`, `servico_id` (junção N:N) |
| `os_produto` | `ordem_servico_id`, `produto_id` (junção N:N — com decremento de estoque) |

---

## Design System

A classe `EstiloApp` centraliza toda a identidade visual da aplicação, evitando "magic colors" espalhadas pelo código:

### Paleta de Cores

| Constante | Hex | Uso |
|---|---|---|
| `AZUL_PRIMARIO` | `#1E3A8A` | Cabeçalhos, botões primários, títulos |
| `AZUL_CLARO` | `#3B82F6` | Destaques e hover |
| `VERDE` | `#16A34A` | Botões de sucesso/salvar |
| `VERMELHO` | `#DC2626` | Botões de perigo/sair/excluir |
| `AMARELO` | `#CA8A04` | Botões secundários (Gerenciar Usuários) |
| `CINZA_FUNDO` | `#F3F4F6` | Fundo de todos os painéis |
| `CINZA_BORDA` | `#D1D5DB` | Bordas de campos de texto |
| `BRANCO` | `#FFFFFF` | Texto sobre fundo escuro |
| `PRETO_TEXTO` | `#111827` | Texto principal |

### Fábrica de Componentes

| Método | Resultado |
|---|---|
| `criarBotaoPrimario(texto)` | Botão azul marinho com cursor pointer |
| `criarBotaoSucesso(texto)` | Botão verde |
| `criarBotaoPerigo(texto)` | Botão vermelho |
| `criarBotaoSecundario(texto)` | Botão amarelo |
| `criarTitulo(texto)` | `JLabel` bold 22px azul |
| `criarSubtitulo(texto)` | `JLabel` bold 16px preto |
| `criarLabel(texto)` | `JLabel` 14px padrão |
| `criarCampoTexto()` | `JTextField` com borda cinza arredondada |
| `bordaPainel(titulo)` | `TitledBorder` com título azul |

### Mensagens Padronizadas (`Mensagens`)

| Método | `JOptionPane` |
|---|---|
| `sucesso(parent, msg)` | INFORMATION — "Sucesso" |
| `aviso(parent, msg)` | WARNING — "Atencao" |
| `erro(parent, titulo, ex)` | ERROR com causa raiz extraída |
| `erro(parent, msg)` | ERROR — "Erro" |
| `confirmar(parent, msg)` | YES_NO_OPTION → `boolean` |

---

## Estrutura do Projeto

```
ProjetoAutoCenterSilva/
│
├── pom.xml                                      # Maven — Java 17, Hibernate 6, Flyway 12, BCrypt
├── LICENSE                                      # Licença MIT
│
└── src/main/
    ├── java/
    │   ├── Main.java                            # Ponto de entrada
    │   │
    │   ├── config/
    │   │   ├── EntityManagerConfiguracao.java   # EntityManager factory (singleton)
    │   │   └── FlyWayConfiguracao.java          # Executa as migrations Flyway
    │   │
    │   ├── entity/
    │   │   ├── Item.java                        # @MappedSuperclass — base de Produto e Servico
    │   │   ├── Produto.java                     # @Entity produto (herda Item) + estoque + marca
    │   │   ├── Servico.java                     # @Entity serviço (herda Item) + duração
    │   │   ├── Cliente.java                     # @Entity + @OneToMany automóveis
    │   │   ├── Automovel.java                   # @Entity + @ManyToOne cliente
    │   │   ├── OrdemServico.java                # @Entity + @ManyToMany serviços e produtos
    │   │   ├── Usuario.java                     # @Entity abstrata — SINGLE_TABLE
    │   │   ├── Admin.java                       # @DiscriminatorValue("ADMIN")
    │   │   ├── Atendente.java                   # @DiscriminatorValue("ATENDENTE")
    │   │   └── Mecanico.java                    # @DiscriminatorValue("MECANICO")
    │   │
    │   ├── repositories/
    │   │   ├── ClienteRepository.java           # CRUD + busca por nome + verifica CPF duplicado
    │   │   ├── AutomovelRepository.java         # CRUD + busca por ID
    │   │   ├── ProdutoRepository.java           # CRUD + busca por nome/categoria
    │   │   ├── ServicoRepository.java           # CRUD + busca por nome/tipo
    │   │   ├── OrdemServicoRepository.java      # CRUD + contar OS abertas por cliente/veículo
    │   │   └── UsuarioRepository.java           # CRUD + busca por e-mail + contagem
    │   │
    │   ├── service/
    │   │   ├── auth/AuthService.java            # Login com BCrypt + criação do admin padrão
    │   │   ├── automovel/AutomovelService.java  # Lógica de negócio dos automóveis
    │   │   ├── camerasDeAr/                     # Seletores de câmara de ar por tipo de veículo
    │   │   ├── cliente/ClienteService.java      # Negócio + validação CPF + bloqueio de exclusão
    │   │   ├── ordemservico/OrdemServicoService # Negócio OS + decremento de estoque ao adicionar produto
    │   │   ├── produto/ProdutoService.java      # CRUD + relatório de produtos
    │   │   ├── servico/ServicoService.java      # CRUD + relatório de serviços
    │   │   └── usuario/GerenciadorUsuarioService# CRUD + hash BCrypt + anti-auto-exclusão
    │   │
    │   ├── util/
    │   │   └── ValidadorCpf.java                # validar() + formatar() + limpar()
    │   │
    │   ├── interfaces/
    │   │   └── CamerasDeAr.java                 # Interface: escolherCamera()
    │   │
    │   ├── iniciadorSistema/
    │   │   └── IniciarSistemaSwing.java         # Bootstrap: testar conexão → Flyway → Login → Main
    │   │
    │   └── view/telas/
    │       ├── componentes/
    │       │   ├── EstiloApp.java               # Design system: paleta, fontes, fábrica de componentes
    │       │   └── Mensagens.java               # JOptionPane centralizado: sucesso/aviso/erro/confirmar
    │       ├── TelaLogin.java                   # JDialog modal, 3 tentativas, bloqueio de sistema
    │       ├── TelaPrincipal.java               # JFrame principal com menu dinâmico por perfil
    │       ├── TelaCliente.java                 # CRUD completo de clientes
    │       ├── TelaAutomovel.java               # CRUD + vínculo com cliente
    │       ├── TelaProduto.java                 # CRUD + categorias especializadas + relatório
    │       ├── TelaServico.java                 # CRUD + relatório
    │       ├── TelaOrdemServico.java            # CRUD + serviços/produtos + finalizar/cancelar
    │       └── TelaUsuario.java                 # CRUD de usuários (somente Admin)
    │
    └── resources/
        ├── hibernate.cfg.xml                    # Conexão PostgreSQL + mapeamento de entidades
        └── db.migration/
            ├── V1__creat_database.sql           # (vazio — schema gerado pelo hbm2ddl.auto=update)
            └── V2__create_usuario.sql           # CREATE TABLE usuario com SINGLE_TABLE
```

---

## Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|---|---|---|
| **Java** | 17 | Linguagem principal |
| **Java Swing** | JDK built-in | Interface gráfica (JFrame, JDialog, JTable, GridBagLayout...) |
| **Nimbus LookAndFeel** | JDK built-in | Aparência moderna do Swing |
| **Maven** | — | Build e dependências |
| **Hibernate ORM** | 6.4.4 | Mapeamento objeto-relacional + sessões |
| **Jakarta Persistence API** | 3.1.0 | Anotações JPA |
| **Flyway** | 12.3.0 | Migrações versionadas do schema |
| **PostgreSQL JDBC** | 42.7.3 | Driver de conexão |
| **BCrypt (favre-lib)** | 0.10.2 | Hash de senhas com fator de custo 12 |
| **PostgreSQL** | — | Banco de dados relacional |

---

## Pré-requisitos

- **Java JDK 17** ou superior
- **Maven** instalado
- **PostgreSQL** instalado e em execução

Verifique:
```bash
java -version
mvn -version
psql --version
```

---

## Configuração do Banco de Dados

### 1. Criar o banco

```sql
CREATE DATABASE dbautocentersilva;
```

### 2. Ajustar as credenciais

Edite `src/main/resources/hibernate.cfg.xml`:

```xml
<property name="hibernate.connection.url">
    jdbc:postgresql://localhost:5432/dbautocentersilva
</property>
<property name="hibernate.connection.username">postgres</property>
<property name="hibernate.connection.password">SUA_SENHA</property>
```

E `src/main/java/config/FlyWayConfiguracao.java` com as mesmas credenciais.

> O Flyway executará as migrations automaticamente ao iniciar o sistema. O Hibernate criará/atualizará as demais tabelas via `hbm2ddl.auto=update`.

---

## Como Executar

### Opção 1 — Via IntelliJ IDEA (recomendado)

1. Abra o IntelliJ e selecione **File → Open** → pasta do projeto
2. Aguarde o Maven carregar o `pom.xml`
3. Confirme o JDK 17 em **File → Project Structure**
4. Abra `Main.java` e clique em ▶️ ao lado do método `main`

### Opção 2 — Via Maven

```bash
git clone https://github.com/seu-usuario/ProjetoAutoCenterSilva.git
cd ProjetoAutoCenterSilva
mvn compile
mvn exec:java -Dexec.mainClass="Main"
```

> Certifique-se de que o PostgreSQL está em execução e o banco `dbautocentersilva` foi criado antes de iniciar.

---

## Credenciais Padrão

Na primeira execução, o sistema cria automaticamente um administrador padrão se não houver nenhum usuário cadastrado:

| Campo | Valor |
|---|---|
| **E-mail** | `admin@oficina.com` |
| **Senha** | `admin123` |
| **Perfil** | Admin |

> A senha é armazenada como hash BCrypt — nunca em texto plano.

---

## Equipe

| Membro |
|---|
| Eduardo Florenciano dos Santos |
| João Victor Navarqui de Mello |
| Lucas Eduardo Zarza de Barros |
| Vittor Eduardo Flores Moreno |
