-- Criação do banco de dados
CREATE DATABASE IF NOT EXISTS springbootdb;
USE springbootdb;

-- Tabela Autor
CREATE TABLE IF NOT EXISTS autor (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela Funcionario
CREATE TABLE IF NOT EXISTS funcionario (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cargo VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela Livro
CREATE TABLE IF NOT EXISTS livro (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(200) NOT NULL,
    ano_publicacao INT NOT NULL,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,
    autor_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_livro_autor FOREIGN KEY (autor_id) REFERENCES autor (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Inserções iniciais de autores
INSERT INTO autor (nome) VALUES
('Machado de Assis'),
('Clarice Lispector'),
('Jorge Amado'),
('Rachel de Queiroz'),
('Carlos Drummond de Andrade');

-- Inserções iniciais de funcionários
INSERT INTO funcionario (nome, cargo) VALUES
('João Silva', 'Bibliotecário Chefe'),
('Maria Oliveira', 'Assistente de Biblioteca'),
('Carlos Pereira', 'Atendente');

-- Inserções iniciais de livros
INSERT INTO livro (titulo, ano_publicacao, autor_id, disponivel) VALUES
('Dom Casmurro', 1899, 1, TRUE),
('Memórias Póstumas de Brás Cubas', 1881, 1, TRUE),
('A Hora da Estrela', 1977, 2, TRUE),
('Capitães da Areia', 1937, 3, TRUE),
('O Quinze', 1930, 4, TRUE),
('Claro Enigma', 1951, 5, FALSE);
