CREATE DATABASE IF NOT EXISTS bibliotecavirtual;
USE bibliotecavirtual;

CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255),
    email_verificado BOOLEAN,
    codigo_verificacao VARCHAR(255),
    admin BOOLEAN,
    foto_url VARCHAR(255),
    ativo BOOLEAN
);

CREATE TABLE livro_local (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255),
    autor VARCHAR(255),
    isbn10 VARCHAR(20),
    isbn13 VARCHAR(20),
    capa_url VARCHAR(255),
    disponivel BOOLEAN,
    quantidade INT
);

CREATE TABLE lista_livro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT,
    nome_lista VARCHAR(255),
    isbn VARCHAR(20),
    titulo VARCHAR(255),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE emprestimo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT,
    isbn VARCHAR(20),
    titulo VARCHAR(255),
    data_emprestimo DATETIME,
    data_devolucao_prevista DATETIME,
    data_devolucao_real DATETIME,
    edition_key VARCHAR(255),
    multa DECIMAL(10,2),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

INSERT INTO emprestimo (id, data_devolucao_prevista, data_devolucao_real, data_emprestimo, isbn, titulo, usuario_id, edition_key, multa)
VALUES 
(5, '2025-06-13 14:53:19.696347', '2025-06-03 16:45:46.221033', '2025-05-30 14:53:19.696136', '-', 'Harry Potter and the Philosopher\'s Stone', 5, 'OL22856696M', NULL),
(6, '2025-06-13 15:08:07.814929', NULL, '2025-05-30 15:08:07.814895', '-', 'Harry Potter and the Philosopher\'s Stone', 6, 'OL22856696M', NULL),
(7, '2025-06-13 15:16:28.456661', NULL, '2025-05-30 15:16:28.456603', '-', 'Harry Potter and the Order of the Phoenix', 6, 'OL25662116M', NULL),
(8, '2025-06-16 09:33:07.801001', NULL, '2025-06-02 09:33:07.800916', '-', 'Jurassic Park', 5, 'OL38582593M', NULL),
(9, '2025-06-16 15:00:12.088532', NULL, '2025-06-02 15:00:12.088456', '-', 'Dom Casmurro (Classicos Da Literatura Brasileira)', 5, 'OL8265063M', NULL);

insert into `lista_livro` (`id`, `isbn`, `nome_lista`, `titulo`, `usuario_id`) values
(1, NULL, 'joaozinho', '', 5),
(2, NULL, 'pedrinho', '', 5),
(3, NULL, 'gustavinho', '', 5),
(4, NULL, 'livros legais', '', 5),
(5, NULL, 'pedrinho2', NULL, 5);

-- Tabela usuario permanece igual
INSERT INTO usuario (username, password, admin, email, email_verificado, codigo_verificacao) VALUES
('func1','$2b$12$OP6vPkq/HofO/kMp.J9Qhu3ci8YMXFaXtPo/iA8bhZUkheZYaFhBq',1,'func1@empresa.com',1,785501),
('func2','$2b$12$TTvPAWlF0rcGdYSfN8rbEOJiVPVsLvpm86VgEmlZnJUqsOO4L0w8O',1,'func2@empresa.com',1,014417),
('bibliotecario','$2b$12$00wqS2lZrDLnJe/XoduffumOl6.F/HOhTJX1ghcWC2qD/Gk1O/DLq',1,'bibliotecario@empresa.com',1,437271),
('gerente','$2b$12$UyuLiwKqJY2RAjSjP2xdcelbRGuHdGKRNNCPHhUEsKbZ2w51Jdgea',1,'gerente@empresa.com',1,423135);

-- Clientes (admin = 0)
INSERT INTO usuario (username, password, admin, email, email_verificado, codigo_verificacao) VALUES
('joao','$2b$12$7zdqVBakbgOubiNf6XZbDefNetLQBuoZu9/BbefeyQAk7bKrLFmF.',0,'joao@email.com',1,377529),
('maria','$2b$12$YHk8XEJ4IiHJRTb0OJRNiO0t3JktWQZ7VIoX8.MDroNzCcjfwIyOa',0,'maria@email.com',1,200848),
('ana','$2b$12$Blb2o0IXNMn9WmlBEpka0OE2f2ZZnrFhyfJA9mr/KQEMjtXULdB0u',0,'ana@email.com',0,637845),
('carlos','$2b$12$L6gvIIJD/yMzB7iF3Pp7VOZ8SAojdeON/qOC5FeiPSUalIJYgjKYW',0,'carlos@email.com',0,096841),
('lucas','$2b$12$Shl77K6tMjUT3A44mAPPbu/WNzEVRu2bzJjER2UzEo.l.nae2gRhq',0,'lucas@email.com',0,541102);

-- Funcionários (admin = 1)
-- ('func1', 'senha123', 1),
-- ('func2', 'senha456', 1),
-- ('bibliotecario', 'admin2025', 1),
-- ('gerente', 'supervisor', 1);

-- Clientes (admin = 0)
-- 'joao', 'joao123',
-- 'maria', 'maria456'
-- 'ana', 'ana789'
-- 'carlos', 'carlos321',
-- 'lucas', 'lucas654'
