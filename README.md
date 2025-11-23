# 📘 API Simulação de Investimento

API desenvolvida em **Quarkus** para simulação de investimentos, utilizando **SQL Server** como banco de dados e **Keycloak** para autenticação via OIDC.  
Inclui documentação interativa via **Swagger UI**.

![Java](https://img.shields.io/badge/Java-21-blue?logo=java)  
![Quarkus](https://img.shields.io/badge/Quarkus-3.29.3-red?logo=quarkus)  
![Docker](https://img.shields.io/badge/Docker-Ready-blue?logo=docker)  
![Keycloak](https://img.shields.io/badge/Keycloak-22.0.1-green?logo=keycloak)  
![SQLServer](https://img.shields.io/badge/SQLServer-2022-lightgrey?logo=microsoftsqlserver)

---

## 🚀 Pré-requisitos

- [Git](https://git-scm.com/downloads)  
- [Docker Desktop](https://www.docker.com/products/docker-desktop)  
- [Maven](https://maven.apache.org/download.cgi) (ou use o wrapper `./mvnw`)  
- Java 21 (JDK)  

---

## 📥 Clonar o repositório

```bash
git clone https://github.com/seu-usuario/simulacao-investimento.git
cd simulacao-investimento
```

---

## ▶️ Subir os containers

```bash
docker-compose up --build
```

Isso irá:

- Criar o banco `investimentos` no SQL Server.  
- Subir o Keycloak em `http://localhost:8180`.  
- Subir a API em `http://localhost:8081`.  

---

## 🔑 Configuração do Keycloak

1. Acesse 👉 [http://localhost:8180](http://localhost:8180)  
   Login inicial: `admin / admin` (realm `master`).  

2. Ao subir a aplicação será importado o arquivo `invest-api-realm.json` que irá criar o Realm.  
   - Realm: `invest-api`  
   - Client: `api-client` (secret: `secret`)  
   - Usuário de teste: `testuser / password`  

3. Após importar, o Quarkus conseguirá autenticar com o Keycloak.  

---

## ⚙️ Configuração da API

O `application.properties` já está preparado para conectar ao SQL Server e ao Keycloak.  

---

## 🧪 Testar a API

- Gere o token. Veja exemplo com o Postman: http://localhost:8180/realms/invest-api/protocol/openid-connect/token
<img width="620" height="240" alt="image" src="https://github.com/user-attachments/assets/b66388a4-6c0b-4290-ac27-c92cc14e2a8f" />

- Swagger UI 👉 [http://localhost:8081/q/swagger-ui](http://localhost:8081/q/swagger-ui)  
- OpenAPI 👉 [http://localhost:8081/q/openapi](http://localhost:8081/q/openapi)  

No Swagger UI:
1. Clique em **Authorize**.
2. Faça login com `testuser / password`.
3. Utilize o Token gerado na ferramenta.
4. Execute os endpoints protegidos.  

---

## 📌 Exemplos de requisições

### 🔐 Obter token no Keycloak
```bash
curl -X POST "http://localhost:8180/realms/invest-api/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=api-client" \
  -d "client_secret=secret" \
  -d "grant_type=password" \
  -d "username=testuser" \
  -d "password=password"
```

### 📊 Chamar endpoint protegido
```bash
curl -X GET "http://localhost:8081/investimentos" \
  -H "Authorization: Bearer <TOKEN>"
```

---

## 🏗️ Arquitetura

A arquitetura da solução é composta por três principais componentes:

- **API Quarkus**: exposta em `http://localhost:8081`, responsável pela lógica de negócios.  
- **Keycloak**: gerencia autenticação e autorização via OIDC, exposto em `http://localhost:8180`.  
- **SQL Server**: banco de dados relacional para persistência dos investimentos.  

Fluxo simplificado:

```
[ Usuário ] → [ Swagger UI / API Quarkus ] → [ Keycloak (OIDC) ]
                                   ↓
                             [ SQL Server ]
```

## 📌 Comandos úteis

- **Parar containers**:
  ```bash
  docker-compose down
  ```

- **Rebuild completo**:
  ```bash
  ./mvnw clean package -DskipTests
  docker-compose up --build
  ```

---

## ✅ Resumo

- Clone o projeto.  
- Suba os containers com `docker-compose up --build`.  
- Importe o realm `invest-api` no Keycloak.  
- Acesse a API em `http://localhost:8081/q/swagger-ui`.  
