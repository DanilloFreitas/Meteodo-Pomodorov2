# 🍅 PomoQuest - Gerenciador Pomodoro Full-Stack

Este projeto é uma aplicação Full-Stack desenvolvida para gerenciar o tempo utilizando a técnica Pomodoro. O sistema conta com um backend robusto em Java e uma API totalmente documentada.

Este repositório faz parte do meu portfólio de estudos no curso de **Ciência da Computação na UFG**.

## 🚀 Funcionalidades
- Controle de ciclos Pomodoro (Iniciar, Parar, Pausar).
- Persistência de histórico de produtividade em banco de dados.
- Documentação interativa via Swagger UI.

## 🛠️ Tecnologias Utilizadas
- **Backend:** Java 17 com Spring Boot 3
- **Banco de Dados:** PostgreSQL
- **Documentação da API:** SpringDoc OpenAPI (Swagger)
- **Frontend:** HTML5, CSS3 e JavaScript (Vanilla)
- **Gerenciador de Dependências:** Maven

## 📖 Como Rodar o Projeto localmente

### 1. Pré-requisitos
- JDK 17 instalado.
- PostgreSQL rodando na máquina.

### 2. Configuração do Banco de Dados
Crie um banco de dados chamado `pomodoro_db` no seu PostgreSQL. No arquivo `src/main/resources/application.properties`, ajuste as credenciais de acesso:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pomodoro_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha