CREATE DATABASE IF NOT EXISTS loginapp;
USE loginapp;

-- Criação da tabela usuario
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    email_verificado BOOLEAN DEFAULT FALSE,
    codigo_verificacao VARCHAR(10),
    admin BOOLEAN NOT NULL
);

-- Criação da tabela livro
CREATE TABLE livro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    genero VARCHAR(100),
    datalancamento VARCHAR(20),
    isbn VARCHAR(30),
    disponivel BOOLEAN NOT NULL
);

-- Funcionários (admin = 1)
INSERT INTO usuario (username, password, email, email_verificado, admin) VALUES
('func1', 'senha123', 'func1@exemplo.com', TRUE, 1),
('func2', 'senha456', 'func2@exemplo.com', TRUE, 1),
('bibliotecario', 'admin2025', 'bibliotecario@exemplo.com', TRUE, 1),
('gerente', 'supervisor', 'gerente@exemplo.com', TRUE, 1);

-- Clientes (admin = 0)
INSERT INTO usuario (username, password, email, email_verificado, admin) VALUES
('joao', 'joao123', 'joao@exemplo.com', TRUE, 0),
('maria', 'maria456', 'maria@exemplo.com', TRUE, 0),
('ana', 'ana789', 'ana@exemplo.com', TRUE, 0),
('carlos', 'carlos321', 'carlos@exemplo.com', TRUE, 0),
('lucas', 'lucas654', 'lucas@exemplo.com', TRUE, 0);

-- Livros
INSERT INTO livro (titulo, autor, genero, datalancamento, isbn, disponivel) VALUES
('Dom Casmurro', 'Machado de Assis', 'Romance', '1899', '9788572329795', 1),
('O Pequeno Príncipe', 'Antoine de Saint-Exupéry', 'Fábula', '1943', '9788522005237', 1),
('1984', 'George Orwell', 'Distopia', '1949', '9788535902774', 1),
('Capitães da Areia', 'Jorge Amado', 'Romance', '1937', '9788520932306', 1),
('Harry Potter e a Pedra Filosofal', 'J.K. Rowling', 'Fantasia', '1997', '9788532511016', 1),
('A Revolução dos Bichos', 'George Orwell', 'Sátira', '1945', '9788535909551', 0),
('Memórias Póstumas de Brás Cubas', 'Machado de Assis', 'Romance', '1881', '9788535914845', 1),
('O Hobbit', 'J.R.R. Tolkien', 'Fantasia', '1937', '9788595084742', 1),
('A Moreninha', 'Joaquim Manuel de Macedo', 'Romance', '1844', '9788579026062', 1),
('Senhora', 'José de Alencar', 'Romance', '1875', '9788535914846', 0);
