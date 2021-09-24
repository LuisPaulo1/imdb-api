# IMDb
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/LuisPaulo1/imdb-api/blob/master/LICENSE) 

# Sobre o projeto

IMDb é uma API desenvolvida para o usuário consultar informações sobre filmes cadastrados e poder realizar avaliações por meio de votos.

## Collection do Postman
Importar o link no Postman: https://www.getpostman.com/collections/1ee9e2f6df2d89bd8485
 
## Documentação da API
- http://localhost:8080/swagger-ui/index.html
- Necessário informar o token nos endpoints protegidos

## Modelo conceitual
![Modelo Conceitual](https://github.com/LuisPaulo1/assets/blob/master/imdb/diagrama-de-classe.png)

## Arquitetura da aplicação
![Arquitetura](https://github.com/LuisPaulo1/assets/blob/master/imdb/arquitetura.png)

## Regras de negócio
- Somente cliente com perfil de administrador poderá realizar cadastro e atualização de filmes
- Somente cliente com perfil de usuário poderá votar nos filmes
- A realização dos votos nos filmes deverá ser feito com um valor numérico de 0 a 4
- A consulta de filmes retorna uma lista ordenada por filmes mais votados e por ordem alfabética
- A remoção dos clientes é feita por exclusão lógica (desativação)
- A listagem de clientes retorna apenas clientes com perfil de usuário que estão ativo
- Os endpoints de atores não precisa informar o token
- Os endpoints GET de filmes não precisa informar o token

## Utilização
- Para realizar as consultas nos endpoints da API é preciso cadastrar primeito um cliente com perfil de ADMINISTRADOR OU USUARIO na pasta Clientes da collection do Postman
- Realizar o login do cliente cadastrado na pasta Auth para receber o token de acesso aos endpoints
- Realizar requisições nos endpoints informando o token Bearer no header Authorization quando for necessário

## Tecnologias utilizadas
- Java 11
- Spring (boot, web, data, security)
- JPA / Hibernate
- Lombok
- Maven
- Mysql
- Flyway
- Swagger OpenAPI 3.0
- Docker

## Como executar o projeto

### Pré-requisitos
Java 11, Git e Docker em execução

```bash
# clonar repositório
git clone https://github.com/LuisPaulo1/imdb-api.git

# entrar na pasta do projeto backend
cd imdb-api

# executar o projeto
docker-compose up
```

# Autor

Luis Paulo

https://www.linkedin.com/in/luis-paulo-souza-a54358134/
