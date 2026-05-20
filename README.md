# 🏛️ The Vault API

[![Java Version](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3%2B-brightgreen?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![Security](https://img.shields.io/badge/Spring_Security-JWT-blueviolet?style=for-the-badge&logo=springsecurity)](https://spring.io/projects/spring-security)
[![Database](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)](https://www.mysql.com/)

**The Vault** é uma API RESTful de nível empresarial desenvolvida para gerenciar catálogos de jogos digitais, avaliações (*reviews*) e coleções personalizadas. Este projeto foi concebido como um ecossistema de alta maturidade técnica, aplicando os padrões de arquitetura de software mais exigidos pelo mercado corporativo atual.

A aplicação conta com uma infraestrutura robusta que engloba persistência relacional avançada, histórico de migrações controlado, exclusão lógica (*soft delete*), tratamento global de exceções padronizado, paginação segura para APIs e uma camada rigorosa de segurança stateless via tokens JWT.

---

## 🛠️ Stack Tecnológica

* **Linguagem Principal:** Java 21 (LTS) com recursos modernos.
* **Framework Core:** Spring Boot 3.3+ (Spring Data JPA, Spring Validation, Spring Web).
* **Segurança:** Spring Security & JWT (Biblioteca Auth0 `java-jwt` v4.4.0).
* **Banco de Dados:** MySQL 8.0.
* **Evolução de Esquema:** Flyway Migrations.
* **Mapeamento de Objetos:** MapStruct para conversão performática entre Entidades e DTOs.
* **Documentação:** Springdoc OpenAPI v2.8.9 (Swagger UI).
* **Produtividade:** Lombok.

---

## 📐 Padrões de Arquitetura e Boas Práticas

A arquitetura do **The Vault** foi desenhada focando no isolamento de responsabilidades, manutenibilidade e desacoplamento. Abaixo estão os pilares de engenharia implementados:

### 1. Modelo de DTOs Isolados (Data Transfer Object)
Nenhuma entidade de banco de dados é exposta diretamente nos Controllers. Cada recurso possui uma separação estrita de objetos para fluxos de entrada e saída:
* `RequestDto`: Valida rigorosamente os dados de entrada usando as anotações do `jakarta.validation`.
* `ResponseDto`: Define exatamente quais dados serão expostos ao cliente da API, protegendo dados internos.
* `UpdateDto`: Isolamento de campos permitidos para alteração parcial ou total do recurso.

### 2. Soft Delete (Exclusão Lógica)
Para preservar a integridade histórica dos dados, o sistema utiliza exclusão lógica nativa via Hibernate. Utilizando as anotações `@SQLDelete` e `@SQLRestriction` ao nível de entidade, os registros deletados têm apenas uma flag alterada no banco de dados e são filtrados automaticamente em todas as consultas (`SELECT`) da aplicação.

### 3. Tratamento Global de Exceções (RFC 7807)
A API não expõe *stack traces* nativos do Java ao cliente. Foi implementado um manipulador centralizado com `@RestControllerAdvice` que captura falhas comuns (como `ResourceNotFoundException`, erros de validação de argumentos ou falhas de integridade) e envelopa as respostas no padrão de mercado **RFC 7807 (Problem Details for HTTP APIs)**.

### 4. Paginação Estável via DTO (`PagedModel`)
Evitando problemas de quebra de contrato de API devido a atualizações internas do framework, a serialização de coleções paginadas foi travada globalmente via `@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)`. Isso garante que o JSON de paginação gerado seja limpo, estável e agrupe os metadados em um nó `page` padronizado.

---

## 🔑 Segurança & Autenticação Stateless (JWT)

A API adota o modelo de segurança **Stateless**, ideal para microsserviços e aplicações modernas. O fluxo de segurança é composto por:

1. **Contrato `UserDetails`:** A entidade `User` atua como um adaptador para o motor do Spring Security, mapeando regras corporativas de perfis (`ROLE_USER`, `ROLE_ADMIN`).
2. **Criptografia Robustecida:** Armazenamento de senhas utilizando hash seguro **BCrypt**.
3. **Filtro Customizado (`OncePerRequestFilter`):** Intercepta cada requisição HTTP, extrai o token do cabeçalho `Authorization: Bearer <token>`, valida a assinatura criptográfica e estabelece o contexto de autenticação do Spring de forma limpa.
4. **Isolamento de Segredos (Profiles):** Chaves mestras e secrets de criptografia são isolados localmente através do arquivo `application-secret.properties`, o qual é permanentemente ignorado pelo Git (`.gitignore`).

---

## 🚀 Endpoints da API

Abaixo estão listadas as principais rotas expostas pela aplicação. Todas as rotas (exceto `/login` e a documentação do Swagger) exigem um token JWT válido enviado via cabeçalho HTTP Bearer.

| Módulo | Método | Endpoint | Descrição | Permissão Necessária |
| :--- | :---: | :--- | :--- | :--- |
| **Autenticação** | `POST` | `/login` | Efetua o login do usuário e retorna o Token JWT. | Pública |
| **Jogos** | `GET` | `/games` | Lista todos os jogos de forma paginada. | Usuário Logado |
| | `POST` | `/games` | Cadastra um novo jogo no sistema. | `ROLE_ADMIN` |
| | `DELETE` | `/games/{id}` | Realiza a exclusão lógica do jogo indicado. | `ROLE_ADMIN` |
| **Avaliações** | `POST` | `/games/{gameId}/reviews` | Adiciona uma avaliação técnica para um jogo. | Usuário Logado |
| **Coleções** | `GET` | `/collections` | Lista as coleções e seus respectivos jogos estruturados. | Usuário Logado |

---

## 📦 Como Configurar e Rodar o Projeto Localmente

O projeto está preparado para rodar de forma isolada e automatizada utilizando containers, o que garante que a aplicação funcionará em qualquer ambiente de desenvolvimento.

### Opção 1: Rodando com Docker (Recomendado)

**Pré-requisitos:** Docker e Docker Desktop/Engine instalados.

1. Clone o repositório para a sua máquina.
2. Abra o terminal na raiz do projeto e execute o comando:
   ```bash
   docker compose up --build
   ```
3. O Docker fará o download do MySQL, compilará a aplicação Java, rodará as migrações do Flyway e subirá a API.
4. A API estará disponível em `http://localhost:8080`.

*(Nota: O banco de dados e a API já estarão conectados através de uma rede interna do Docker, sem necessidade de configurações adicionais de senha).*

---

### Opção 2: Rodando Manualmente (Sem Docker)

**Pré-requisitos:** Java 21 JDK, Maven 3.x e servidor MySQL local rodando na porta 3306.

1. Crie um arquivo chamado `application-secret.properties` dentro do diretório `src/main/resources/`:

```properties
# Configuração de credenciais do seu banco de dados local
spring.datasource.url=jdbc:mysql://localhost:3306/the_vault_db?createDatabaseIfNotExist=true
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

# Chave mestra de assinatura do JWT
api.security.token.secret=MinhaChaveSecretaSuperProtegidaDoTheVault123!
```

2. Execute os seguintes comandos na raiz do projeto:
```bash
mvn clean package
mvn spring-boot:run
```

---

### 🔑 Criação do Usuário Inicial Administrador
Como a API é protegida por JWT, após subir a aplicação (seja via Docker ou manualmente), você precisará criar o primeiro administrador diretamente no banco de dados para conseguir fazer o login. Conecte-se ao banco `the_vault_db` e execute:

```sql
INSERT INTO users (login, password, role) 
VALUES ('admin@thevault.com', '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', 'ADMIN');
```
*(A senha para este hash é `123456`).*

---

## 📖 Documentação Interativa (Swagger UI)

A API possui documentação totalmente interativa gerada de forma dinâmica pelo Springdoc OpenAPI. Com o servidor rodando, você pode acessar a interface gráfica do Swagger diretamente pelo seu navegador web de preferência:

🔗 **URL de Acesso:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Como Testar Endpoints Protegidos no Swagger UI:
1. Localize o endpoint `POST /login` na interface gráfica.
2. Clique em **"Try it out"** e envie o JSON com o usuário administrador criado (`admin@thevault.com` e senha `123456`).
3. Copie o token JWT gerado na resposta (apenas o texto hash longo).
4. Suba até o topo da página do Swagger e clique no botão verde **"Authorize"**.
5. Cole o token no campo de texto e clique em **Authorize**.
6. Pronto! Agora você pode realizar requisições de cadastros, listagens e exclusões diretamente pela interface visual sem necessidade de ferramentas de terceiros.
