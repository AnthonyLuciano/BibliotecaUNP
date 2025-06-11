---

# 📚 BibliotecaVirtual

Sistema web de biblioteca universitária desenvolvido como projeto de turma da **Universidade Potiguar (UNP)**.
Criado com **Java**, **Spring Boot**, **Thymeleaf** e **OpenLibrary API**, o sistema oferece funcionalidades completas de gerenciamento de livros, usuários, empréstimos, devoluções, notificações e recomendações personalizadas.

---

## 🧠 Visão Geral

O **BibliotecaVirtual** tem como foco facilitar a gestão de acervos físicos e virtuais, proporcionando uma experiência completa para alunos e administradores. Entre os destaques estão:

* Autenticação de usuários com confirmação de e-mail
* Diferenciação entre usuários e administradores
* Cadastro de livros locais e integração com a OpenLibrary
* Empréstimos e devoluções com controle de prazos
* Recomendações personalizadas por histórico
* Lembretes automáticos por e-mail
* Painel administrativo para gerenciamento do sistema

---

## 🗺️ Roadmap por Integrante

### 👨‍💻 Anthony – Autenticação, Controle de Usuários & Documentação

* ✅ RF1: Login para usuários e administradores
* ✅ RF2: Cadastro de novos usuários
* ✅ RF3: Edição/desativação de usuários
* 📝 Documentação geral do sistema
* 🤝 Apoio técnico aos colegas nas funcionalidades pendentes

---

### 📚 Lucas – Gerenciamento de Livros

* ✅ RF4: Cadastro de novos livros/exemplares (apenas administradores) *(parcial)*
* ✅ RF5: Busca de livros por título, autor, gênero ou ISBN
* ✅ RF6: Visualização de detalhes dos livros

---

### 📦 Tyago – Empréstimos e Devoluções

* ✅ RF7: Empréstimos virtuais com prazos definidos
* ✅ RF8: Controle de disponibilidade de exemplares *(parcial/incompleto)*
* ✅ RF9: Multas por atraso
* ✅ RF10: Lembretes por e-mail sobre devolução (via agendamento)

---

### 🧾 Maurilio – Funcionalidades Administrativas

* ✅ RF11: Relatórios de empréstimos
* ✅\* RF12: Aprovação/gestão de usuários *(parcial – falta aprovação explícita)*

---

### 🎯 Ygor – Personalização & Documentação

* ✅ RF13: Sistema de recomendações baseado em histórico de leitura
* 📝 Colaboração na documentação técnica e fluxos do usuário

---

## ✅ Requisitos Funcionais (RF) - Status Geral

| Requisito | Descrição                                         | Responsável | Status        |
| --------- | ------------------------------------------------- | ----------- | ------------- |
| RF1       | Login para usuários e administradores             | Anthony     | ✅            |
| RF2       | Cadastro de novos usuários                        | Anthony     | ✅            |
| RF3       | Edição/desativação de usuários                    | Anthony     | ✅            |
| RF4       | Cadastro de livros/exemplares                     | Lucas       | ✅            |
| RF5       | Busca de livros por título, autor, gênero ou ISBN | Lucas       | ✅            |
| RF6       | Visualização de detalhes dos livros               | Lucas       | ✅            |
| RF7       | Empréstimos virtuais com prazo                    | Tyago       | ✅            |
| RF8       | Controle de disponibilidade de exemplares         | Tyago       | ✅            |
| RF9       | Multas por atraso                                 | Tyago       | ✅            |
| RF10      | Lembretes por e-mail/notificação                  | Tyago       | ✅            |
| RF11      | Relatórios administrativos                        | Maurilio    | ✅            |
| RF12      | Aprovação/gestão de cadastro de usuários          | Maurilio    | ✅            |
| RF13      | Recomendação baseada em histórico                 | Ygor        | ✅            |

---

## 📌 Pendências por Integrante

| Integrante   | Itens Pendentes                   |
| ------------ | --------------------------------- |
| **Anthony**  | Revisando Documentação            |
| **Lucas**    | tudo feito ✅                     |
| **Tyago**    | tudo feito ✅                     |
| **Maurilio** | tudo feito ✅                     |
| **Ygor**     | tudo feito ✅                     |
| **Cosme**    | Documentação e Slides             |

---

## 🧩 Arquitetura e Tecnologias

* **Spring Boot** – Base do projeto com injeção de dependências, segurança, agendamento e integração com banco de dados.
* **Thymeleaf** – Template engine para renderização dinâmica das páginas HTML.
* **JPA / Hibernate** – ORM para persistência e consulta de dados.
* **Spring Security** – Autenticação e autorização robustas.
* **JavaMailSender** – Envio de e-mails de confirmação e lembretes.
* **Spring Scheduler** – Agendamento de tarefas automáticas.
* **OpenLibrary API** – Integração para expandir o acervo e obter metadados de livros.

---

## 🔄 Fluxo de Funcionamento

1. **Cadastro e Login**
   Usuário se registra, confirma e-mail e faz login no sistema.

2. **Busca de Livros**
   Exibe resultados do banco local e da OpenLibrary, priorizando o acervo da instituição.

3. **Empréstimos e Devoluções**
   Usuários autenticados solicitam livros com controle de prazos e devolução pelo sistema.

4. **Notificações e Lembretes**
   E-mails automáticos enviados 3 e 1 dia antes do prazo final.

5. **Recomendações Personalizadas**
   Sistema sugere livros com base em gêneros e autores lidos anteriormente.

6. **Listas de Leitura**
   Organização pessoal de leituras com possibilidade de criação de listas personalizadas.

7. **Administração**
   Administradores podem gerenciar usuários e o acervo físico da instituição.

---

## 🔐 Segurança

* **Senhas criptografadas** com BCrypt.
* **Rotas seguras** com controle de acesso por perfil.
* **Validações** para impedir duplicidades de empréstimos, ISBN inválidos, e cadastro repetido.
* **Upload de imagem seguro**, com nomes únicos e armazenamento isolado.

---

## 📄 Sobre o Projeto

Este projeto foi desenvolvido como parte da disciplina de Programação na **Universidade Potiguar (UNP)**, com o objetivo de aplicar conceitos de backend, frontend, integração de APIs e boas práticas de desenvolvimento.

---

## 👥 Desenvolvido por

* **Anthony**
* **Lucas**
* **Tyago**
* **Maurilio**
* **Ygor**
* **Cosme**

---

## 📅 Última Atualização

Junho de 2025

---
