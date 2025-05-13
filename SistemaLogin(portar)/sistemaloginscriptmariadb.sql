CREATE DATABASE IF NOT EXISTS loginapp;
USE loginapp;

-- Criação da tabela usuario
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    funcionario BOOLEAN NOT NULL
);

-- Inserção de funcionários (funcionario = 1)
INSERT INTO usuario (username, password, funcionario) VALUES
('func1', 'senha123', 1),
('func2', 'senha456', 1);
