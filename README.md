# API Series

API RESTful desenvolvida com Spring Boot para gerenciamento de séries asiáticas, permitindo o cadastro e acompanhamento de séries, categorias, temporadas, usuários, perfis e listas pessoais.

## Sobre o projeto

O projeto foi desenvolvido como projeto final da disciplina de Desenvolvimento de APIs com Spring Boot do curso de Sistemas para Internet.

A API permite organizar informações sobre séries asiáticas e acompanhar o status de cada série dentro das listas dos usuários.

O projeto aplica conceitos de:

- API REST
- Spring Boot
- Spring Data JPA
- Hibernate
- Bean Validation
- H2 Database
- Spring HATEOAS
- Swagger / OpenAPI
- Paginação
- Consultas personalizadas
- CRUD
- Relacionamentos entre entidades
- Enum

## Tecnologias utilizadas

- Java 17
- Spring Boot
- Maven
- Spring Web
- Spring Data JPA
- H2 Database
- Bean Validation
- Spring HATEOAS
- Springdoc OpenAPI / Swagger UI

## Funcionalidades

A API permite:

- Cadastrar, consultar, atualizar e excluir usuários
- Cadastrar e gerenciar séries
- Associar séries a categorias
- Cadastrar temporadas para as séries
- Criar listas personalizadas para usuários
- Adicionar séries às listas
- Definir o status de acompanhamento de cada série
- Criar perfis vinculados aos usuários
- Pesquisar registros por diferentes critérios
- Consultar dados utilizando paginação
- Navegar pelos recursos através de links HATEOAS
- Consultar a documentação da API através do Swagger UI

## Entidades

### Usuario

Representa o usuário da aplicação.

Principais atributos:

- `id`
- `nome`
- `email`

### Perfil

Representa o perfil de um usuário.

Principais atributos:

- `id`
- `nomeExibicao`
- `usuario`

Relacionamento:

- Um usuário possui um perfil.

### Serie

Representa uma série cadastrada na aplicação.

Principais atributos:

- `id`
- `titulo`
- `paisOrigem`
- `anoLancamento`
- `status`
- `categorias`

### Categoria

Representa uma categoria associada às séries.

Principais atributos:

- `id`
- `nome`

### Temporada

Representa uma temporada de uma série.

Principais atributos:

- `id`
- `numero`
- `quantidadeEpisodios`
- `serie`

### Lista

Representa uma lista criada por um usuário.

Principais atributos:

- `id`
- `nome`
- `usuario`

### ItemLista

Representa uma série adicionada a uma lista e o respectivo status de acompanhamento.

Principais atributos:

- `id`
- `lista`
- `serie`
- `status`

## Relacionamentos

A API utiliza diferentes tipos de relacionamentos entre as entidades.

### One-to-One

`Usuario 1:1 Perfil`

Cada usuário possui um perfil.

### One-to-Many

`Usuario 1:N Lista`

Um usuário pode possuir várias listas.

`Serie 1:N Temporada`

Uma série pode possuir várias temporadas.

`Lista 1:N ItemLista`

Uma lista pode possuir vários itens.

### Many-to-Many

`Serie N:M Categoria`

Uma série pode possuir várias categorias e uma categoria pode estar associada a várias séries.

## Status

### Status da série

As séries podem possuir os seguintes estados:

- `Em Andamento`
- `Finalizada`
- `Dropada`

### Status de acompanhamento

O status representa a situação da série dentro da lista de um usuário:

- `Aguardando`
- `Assistindo`
- `Concluída`
- `Dropada`

O status da série e o status de acompanhamento são independentes.

Por exemplo, uma série pode estar globalmente como "Em Andamento", enquanto para determinado usuário ela pode estar como "Assistindo".

## Endpoints

### Usuários

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/usuarios` | Lista usuários com paginação |
| GET | `/usuarios/{id}` | Busca um usuário pelo ID |
| GET | `/usuarios/buscar?nome=` | Pesquisa usuários pelo nome |
| POST | `/usuarios` | Cadastra um usuário |
| PUT | `/usuarios/{id}` | Atualiza um usuário |
| DELETE | `/usuarios/{id}` | Remove um usuário |

### Séries

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/series` | Lista séries com paginação |
| GET | `/series/{id}` | Busca uma série pelo ID |
| GET | `/series/buscar?titulo=` | Pesquisa séries pelo título |
| POST | `/series` | Cadastra uma série |
| PUT | `/series/{id}` | Atualiza uma série |
| DELETE | `/series/{id}` | Remove uma série |

### Categorias

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/categorias` | Lista categorias com paginação |
| GET | `/categorias/{id}` | Busca uma categoria pelo ID |
| GET | `/categorias/buscar?nome=` | Pesquisa categorias pelo nome |
| POST | `/categorias` | Cadastra uma categoria |
| PUT | `/categorias/{id}` | Atualiza uma categoria |
| DELETE | `/categorias/{id}` | Remove uma categoria |

### Temporadas

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/temporadas` | Lista temporadas com paginação |
| GET | `/temporadas/{id}` | Busca uma temporada pelo ID |
| GET | `/temporadas/buscar?serieId=` | Pesquisa temporadas de uma série |
| POST | `/temporadas` | Cadastra uma temporada |
| PUT | `/temporadas/{id}` | Atualiza uma temporada |
| DELETE | `/temporadas/{id}` | Remove uma temporada |

### Listas

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/listas` | Lista listas com paginação |
| GET | `/listas/{id}` | Busca uma lista pelo ID |
| GET | `/listas/buscar?usuarioId=` | Pesquisa listas de um usuário |
| POST | `/listas` | Cadastra uma lista |
| PUT | `/listas/{id}` | Atualiza uma lista |
| DELETE | `/listas/{id}` | Remove uma lista |

### Itens de lista

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/itens-lista` | Lista itens com paginação |
| GET | `/itens-lista/{id}` | Busca um item pelo ID |
| GET | `/itens-lista/buscar?status=` | Pesquisa itens pelo status |
| POST | `/itens-lista` | Adiciona uma série a uma lista |
| PUT | `/itens-lista/{id}` | Atualiza um item da lista |
| DELETE | `/itens-lista/{id}` | Remove um item da lista |

### Perfis

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/perfis` | Lista perfis com paginação |
| GET | `/perfis/{id}` | Busca um perfil pelo ID |
| GET | `/perfis/buscar?nome=` | Pesquisa perfis pelo nome |
| POST | `/perfis` | Cadastra um perfil |
| PUT | `/perfis/{id}` | Atualiza um perfil |
| DELETE | `/perfis/{id}` | Remove um perfil |

## Paginação

As rotas de listagem utilizam `Pageable`.

Exemplo:

`GET /series?page=0&size=10`

Onde:

- `page` representa o número da página;
- `size` representa a quantidade de registros por página.

As respostas paginadas também utilizam os recursos do Spring HATEOAS.

## Validação

A API utiliza Bean Validation para validar os dados recebidos nas requisições.

Entre as validações utilizadas estão:

- `@NotBlank`
- `@NotNull`
- `@Email`

Exemplo de dados inválidos para um usuário:

```json
{
  "nome": "",
  "email": "email-invalido"
}
````

Essas validações impedem o cadastro de informações que não atendam às regras definidas para as entidades.

## Códigos HTTP

A API utiliza códigos de status HTTP de acordo com o resultado das operações.

Principais códigos utilizados:

* `200 OK` — operação realizada com sucesso
* `201 Created` — recurso criado com sucesso
* `204 No Content` — recurso removido com sucesso
* `400 Bad Request` — dados enviados são inválidos
* `404 Not Found` — recurso não encontrado

## HATEOAS

A API utiliza Spring HATEOAS para adicionar links relacionados aos recursos retornados.

Os links permitem navegar entre operações e recursos da API, como:

* visualizar o próprio recurso;
* atualizar;
* excluir;
* acessar recursos relacionados.

As respostas utilizam recursos como `EntityModel`, `CollectionModel` e `PagedModel` quando aplicável.

## Swagger / OpenAPI

A documentação da API é disponibilizada através do Swagger UI.

Após iniciar a aplicação, acesse:

`http://localhost:8081/swagger-ui.html`

O Swagger permite visualizar e testar os endpoints da API diretamente pelo navegador.

A documentação contém:

* Descrição dos endpoints
* Métodos HTTP
* Parâmetros
* Exemplos de requisição
* Possíveis respostas
* Códigos HTTP
* Modelos utilizados pela API

A interface do Swagger também possui uma personalização visual própria do projeto.

## Banco de dados

O projeto utiliza o banco de dados H2 em memória para persistência durante a execução da aplicação.

Como o banco é configurado em memória, os dados cadastrados são perdidos quando a aplicação é encerrada.

## Como executar o projeto

### Pré-requisitos

É necessário possuir instalado:

* Java 17 ou superior
* Maven
* IntelliJ IDEA ou outra IDE compatível

### Executando pela IDE

1. Clone ou baixe o projeto.
2. Abra o projeto na IDE.
3. Aguarde o Maven carregar as dependências.
4. Execute a classe:

`ApiSeriesApplication`

5. A aplicação será iniciada na porta:

`8081`

### Acessando a API

Base URL:

`http://localhost:8081`

Swagger:

`http://localhost:8081/swagger-ui.html`

Documentação OpenAPI:

`http://localhost:8081/v3/api-docs`

## Estrutura do projeto

```text
src
└── main
    ├── java
    │   └── br.com.senac.apiseries
    │       ├── assembler
    │       ├── config
    │       ├── controller
    │       ├── entity
    │       └── repository
    │
    └── resources
        ├── static
        │   └── swagger-custom.css
        └── application.properties
```

## Organização da aplicação

A aplicação utiliza uma organização baseada em responsabilidades:

* **Controller:** responsável pelas requisições HTTP e endpoints da API.
* **Repository:** responsável pelo acesso aos dados utilizando Spring Data JPA.
* **Entity:** representa as entidades e relacionamentos do domínio.
* **Assembler:** responsável pela construção dos recursos HATEOAS.
* **Config:** contém configurações da aplicação e da documentação OpenAPI/Swagger.

## Projeto

**API Séries**

Projeto desenvolvido para a disciplina de Desenvolvimento de APIs com Spring Boot.

Tecnologias principais: Java, Spring Boot, Spring Data JPA, H2, Spring HATEOAS e Springdoc OpenAPI.