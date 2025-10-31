# 🛍️ Trabalho Final API - E-Commerce (Grupo 4 - Serratec) 

## Visão Geral do Projeto

O **TrabalhoFinalAPI-Grupo4** é uma aplicação desenvolvida em **Java 17** com **Spring Boot 3.5.6**, criada como parte do **projeto final da disciplina de Desenvolvimento de API** do curso **Serratec - Residência em TIC - Software**.

O objetivo é desenvolver uma **API RESTful** para simular um **sistema de e-commerce completo**, permitindo gerenciar **clientes, produtos, categorias, pedidos e relatórios**, com autenticação via **JWT**, documentação com **Swagger**, integração com a **API ViaCEP** e envio de e-mails automáticos.

---

## 👥 Equipe de Desenvolvimento

>* **[Alisson Lima](https://www.linkedin.com/in/alisson-lima-de-souza-0512233a/)**  
>* **[Amanda Lisboa](https://www.linkedin.com/in/amanda-lisboa-789a42330/)**  
>* **[Guilherme Silva](https://www.linkedin.com/in/guilhermesilvaartes/)**  
>* **[Isabela Medeiros](https://www.linkedin.com/in/iamisabellams/)**  
>* **[Pedro Sant'Anna](https://www.linkedin.com/in/pedro-sant-%CC%81anna-8829752a6/)**


## ⚙️ Tecnologias Utilizadas

| Categoria | Ferramenta |
|------------|-------------|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.5.6 |
| Banco de Dados | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Autenticação | JWT (JSON Web Token) |
| Documentação | Springdoc OpenAPI (Swagger UI) |
| Relatórios PDF | Kernel |
| Envio de E-mail | Spring Boot Starter Mail |
| Dados de Teste | DataFaker |
| Ferramentas Dev | Spring Boot DevTools |

---

## Estrutura do Projeto

```
src/main/java/com/serratec/ecommerce/
├── configs/               # Configurações globais e de inicialização
│   ├── ConfigSeguranca.java
│   ├── DataInitializer.java
│   ├── DataSeederProdutosCategoria.java
│   └── OpenApiConfig.java
│
├── controllers/           # Endpoints REST
│   ├── AuthController.java
│   ├── CategoriaController.java
│   ├── ClienteController.java
│   ├── PedidoController.java
│   ├── ProdutoController.java
│   ├── RelatorioController.java
│   ├── UsuarioController.java
│   └── ValidarCupomController.java
│
├── dtos/                  # Objetos de Transferência de Dados
├── entitys/               # Entidades JPA
├── enums/                 # Enumerações do sistema
├── exceptions/            # Tratamento de exceções globais
├── repositorys/           # Repositórios (JPA)
├── securitys/             # Autenticação e JWT
└── services/              # Lógica de negócios (camada Service)
```

---

## 🧮 Modelo de Banco de Dados (DER)

O sistema utiliza **PostgreSQL** com o modelo relacional abaixo:

| Entidade | Campos principais | Relacionamentos |
|-----------|-------------------|----------------|
| **cliente** | id, nome, cpf, email, telefone, endereco_id | N:1 com endereço, 1:N com pedido |
| **usuario** | id, nome, email, senha, perfil | — |
| **endereco** | id, logradouro, bairro, cidade, uf, cep | 1:N com cliente e usuário |
| **categoria** | id, nome, descricao | 1:N com produto |
| **produto** | id, nome, preco, quantidade_estoque, categoria_id | 1:N com itens_pedido |
| **pedido** | id, data_pedido, status, id_cliente | 1:N com itens_pedido |
| **itens_pedido** | id, quantidade, desconto, valor_venda, id_pedido, id_produto | N:1 com pedido e produto |

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

Antes de começar, você precisará ter instalado:

- **Java 17**
- **Maven 3.9+**
- **PostgreSQL**
- **IDE** de sua preferência (IntelliJ, Eclipse ou VSCode)

---

### Clonar o Repositório

```bash
git clone https://github.com/pedrosgleite/TrabalhoFinalAPI-Grupo4
cd TrabalhoFinalAPI-Grupo4
```

---

### Configurar o Banco de Dados

Edite o arquivo:

📄 `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=(coloque seu username do banco aqui)
spring.datasource.password=(coloque sua senha aqui!)
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

### Rodar a Aplicação

Execute o projeto com:

```bash
mvn spring-boot:run
```

ou pela IDE:

> `Run → TrabalhoFinalApiGrupo4Application.java`  
* Para que o banco de dados seja completamente populado com os dados iniciais, é necessário executar a aplicação **duas vezes**.  
---

## Autenticação JWT

A autenticação é baseada em **tokens JWT**.

### Endpoint de Login

**POST** `/auth/login`

**Exemplo de requisição:**
```json
{
  "email": "admin@admin.com",
  "senha": "123456"
}
```

**Exemplo de resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI..."
}
```

Utilize o token nas demais requisições:
```
Authorization: Bearer <token>
```

---

## 🧾 Principais Endpoints

| Método | Endpoint | Descrição |
|---------|-----------|-----------|
| `GET` | `/clientes` | Lista todos os clientes |
| `POST` | `/clientes` | Cadastra um novo cliente |
| `GET` | `/produtos` | Lista todos os produtos |
| `POST` | `/pedidos` | Cria um novo pedido |
| `GET` | `/{clienteId}/pedidos/{pedidoId}/nota-fiscal-pdf` | Gera um relatório PDF |
| `GET` | `/validar-cupom` | Validador de cupons |

---

## Funcionalidades Implementadas

* Cadastro e edição de clientes, produtos e categorias  
* Associação entre produtos e categorias  
* Consulta de endereço via **API ViaCEP**  
* Envio automático de e-mails ao cadastrar ou atualizar cliente  
* Controle de pedidos com status dinâmico  
* Nota Fiscal em PDF
* Autenticação e autorização com **JWT**  
* População inicial de dados (seed)  
* Tratamento global de exceções  

---

## Tratamento de Exceções

As exceções são tratadas de forma centralizada por `ControllerExceptionHandler.java`, retornando mensagens amigáveis, por exemplo:

```json
{
  "timestamp": "2025-10-28T18:00:00Z",
  "status": 404,
  "error": "Entidade não encontrada",
  "message": "Produto com ID 10 não encontrado"
}
```

---

## Documentação com Swagger

Após rodar o projeto, acesse:

* **http://localhost:8080/swagger-ui.html**

Com o  **Swagger UI** consegue ver melhor e gerenciar todos os endpoints da API. 

---

## 👩‍💻 Parte Individual

Cada integrante do grupo desenvolveu uma funcionalidade própria:
| Integrante | Função |
|-------------|---------|
| **Alisson Lima** | Implementação extra de DataFaker para incluir categorias, produtos, cliente, usuario e pedido  |
| **Amanda Lisbôa** | Implementação da Native Query personalizada para retornar uma Nota Fiscal em PDF |
| **Guilherme Silva** | Implementação da Native Query personalizada para retorna os Top10 clientes que mais gastaram na aplicação  |
| **Isabela Medeiros** | Implementação da Validador de cupom  |
| **Pedro Sant'Anna** | Implementação da Native Query para listar os produtos por categoria, valor total do estoque, ranking dos produtos mais caros. |

---

## Boas Práticas e Padrões

- Injeção de dependência com `@Autowired`  
- Uso de DTOs para abstrair entidades  
- Documentação automática com OpenAPI  
- Estrutura modular para fácil expansão  
- Tratamento de erros com `@ControllerAdvice`  

---

## Próximos Passos e Melhorias

- Adicionar testes unitários e de integração  
- Implementar inativo ao invés de delete Cliente
- Automatizar deploy (Render / Railway)

---

## 📄 Licença

Projeto de uso **acadêmico**, desenvolvido para fins educacionais no programa **Serratec - Residência em TIC / Software - 2025**.

✨ Desenvolvido com dedicação pelo **Grupo 4 - Serratec Full Stack 2025**
