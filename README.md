# Projeto Spring - Desafio PicPay
## Projeto JAVA Springboot

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)

Esse repositório contém o projeto final desenvolvido para treinamento com base no desafio proposto [Desafio PicPay Backend](https://github.com/PicPay/picpay-desafio-backend?tab=readme-ov-file)

- Padrão de projeto em camadas MVC
- Tratamento de exceção com retorno customizado.
- Tratamento de validação na inserção de dados usando Spring Validation
- Utilizado diversas notations do Spring

## Techs
- Java Spring
- Junit tests - Mockito
- Banco de Dados em Memória - H2

## Collections Postman
Pode ser encontrado aqui no repo.

## Endpoints

```
GET /transactions  - Recupera a lista de todas as transactions realizadas
GET /transactions/user/{id} - Recupera a lista das transactions de um determinado usuário pelo seu ID
GET /transactions/document/{userDocument} - Recupera a lista das transactions de um determinado usuário pelo documento informado
POST /transactions - Salva uma nova transação

GET /users - Recupera todos usuarios cadastrados
GET /users/{id} - Recupera um usuario informando seu id
GET /users/document/{userDocument}  - Recupera um usuario informando seu documento
POST /users - Cadastra um novo usuário
PUT /users/{id} - Atualiza o cadastro de um usuário informando o seu id
DEL /users/{id} - Deleta o casdastro de um usuário informando o seu id

```

![image](https://github.com/Sammy192/SpringPicPaySimplificado/assets/53224915/98f9b1ed-d144-4555-9505-2045a893785b)

![image](https://github.com/Sammy192/SpringPicPaySimplificado/assets/53224915/20c13aca-ec2a-44a8-abaf-a80c7f11d311)

