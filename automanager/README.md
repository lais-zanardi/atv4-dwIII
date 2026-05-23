# 🤖 Projeto Automanager (Autobots)

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0.13-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![FATEC SJC](https://img.shields.io/badge/FATEC-SJC-red?style=for-the-badge)](#)

Repositório destinado à **ATV4** da disciplina de **Desenvolvimento Web III**, do curso de Desenvolvimento de Software Multiplataforma (DSM) da **FATEC São José dos Campos**. 

O projeto consiste em uma API RESTful desenvolvida com Spring Boot para o gerenciamento de uma loja de manutenção veicular e venda de autopeças. 

---

## 🔒 Segurança e Autenticação (JWT)

Foi implementado uma arquitetura de segurança stateless baseada em **JSON Web Token (JWT)**. Todas as requisições (exceto a raiz `/` e o console do banco de dados) necessitam de um token do tipo `Bearer` no cabeçalho `Authorization` da requisição.

### 👥 Perfis de Acesso (RBAC)
O controle de acesso foi implementado em nível de método (`@EnableMethodSecurity`) respeitando a matriz de direitos sugerida pelos investidores:

* **ADMINISTRADOR**: Acesso total a todas as operações de CRUD do sistema, incluindo manipulação de outros administradores.
* **GERENTE**: Autorização para operações de CRUD sobre usuários (gerentes, vendedores e clientes), além de controle completo de serviços, vendas e mercadorias.
* **VENDEDOR**: Autorização para criar vendas associadas a si mesmo, realizar leitura dessas informações e ler dados sobre serviços e mercadorias.
* **CLIENTE**: Autorização restrita à leitura de informações sobre o **seu próprio cadastro** (*Owner-based access control*) e consulta ao histórico de vendas nas quais figurou como consumidor.

---

## 🚀 Como Executar

### Pré-requisitos

- [Java Development Kit (JDK) 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) instalado.
- Git para clonar o repositório.

### Configuração de Propriedades

Antes de executar, certifique-se de que o arquivo `src/main/resources/application.properties` contenha a assinatura da chave secreta e tempo de expiração para o correto funcionamento do ecossistema JWT:

```properties
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000

```

### Passo a Passo

1. Clone o repositório:

```bash
git clone https://github.com/lais-zanardi/atv4-dwiii.git

```

2. Acesse a pasta raiz da aplicação:

```bash
cd atv4-dwiii/automanager

```

3. Instale as dependências e execute a aplicação (o Maven baixará tudo automaticamente):

**No Windows:**

```cmd
mvnw.cmd spring-boot:run

```

**No Linux/Mac:**

```bash
./mvnw spring-boot:run

```

4. A aplicação estará rodando localmente. Você pode realizar o processo de autenticação enviando as credenciais via `POST` para o endpoint de login para obter o token:

```
http://localhost:8080/login

```

5. O console do banco de dados em memória H2 está disponível de forma pública para auditoria em:

```
http://localhost:8080/h2-console
