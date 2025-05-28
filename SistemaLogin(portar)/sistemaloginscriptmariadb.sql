CREATE DATABASE IF NOT EXISTS loginapp;
USE loginapp;

-- Criação da tabela usuario
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    email_verificado BOOLEAN,
    codigo_verificacao VARCHAR(255),
    admin BOOLEAN NOT NULL
);

-- Atualização da tabela livro para usar edition_key
CREATE TABLE livro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    genero VARCHAR(100),
    dataLancamento VARCHAR(20),
    edition_key VARCHAR(40), -- novo campo para editionKey
    disponivel BOOLEAN NOT NULL,
    sinopse TEXT
);

-- Exemplo de inserção (edition_key fictício, ajuste conforme necessário)
INSERT INTO livro (titulo, autor, genero, datalancamento, edition_key, disponivel, sinopse) VALUES
('Dom Casmurro', 'Machado de Assis', 'Romance', '1899', 'OL12345M', 1, 'Narrado por Bentinho, o romance explora dúvidas sobre a fidelidade de sua esposa Capitu, marcada por sua ambiguidade e ciúmes.'),
('O Pequeno Príncipe', 'Antoine de Saint-Exupéry', 'Fábula', '1943', 'OL23456M', 1, 'Um jovem príncipe viaja por planetas e compartilha reflexões sobre a vida, o amor e a amizade, em uma fábula poética e filosófica.'),
('1984', 'George Orwell', 'Distopia', '1949', 'OL34567M', 1, 'Em um regime totalitário liderado pelo Grande Irmão, Winston Smith luta contra a opressão e manipulação da verdade.'),
('Capitães da Areia', 'Jorge Amado', 'Romance', '1937', 'OL45678M', 1, 'Conta a história de um grupo de meninos de rua em Salvador e suas lutas por sobrevivência e dignidade.'),
('Harry Potter e a Pedra Filosofal', 'J.K. Rowling', 'Fantasia', '1997', 'OL56789M', 1, 'O jovem Harry descobre ser um bruxo e inicia sua jornada em Hogwarts, onde enfrenta perigos e descobre seu passado.'),
('A Revolução dos Bichos', 'George Orwell', 'Sátira', '1945', 'OL67890M', 0, 'Os animais de uma fazenda se rebelam contra seus donos humanos, mas acabam recriando uma tirania semelhante entre eles.'),
('Memórias Póstumas de Brás Cubas', 'Machado de Assis', 'Romance', '1881', 'OL78901M', 1, 'Brás Cubas narra sua vida após a morte, com ironia e crítica à sociedade brasileira do século XIX.'),
('O Hobbit', 'J.R.R. Tolkien', 'Fantasia', '1937', 'OL89012M', 1, 'Bilbo Bolseiro embarca em uma aventura com anões para recuperar um tesouro guardado por um dragão.'),
('A Moreninha', 'Joaquim Manuel de Macedo', 'Romance', '1844', 'OL90123M', 1, 'Augusto, um estudante de medicina, se apaixona por Carolina durante um fim de semana em uma ilha, cumprindo uma antiga promessa.'),
('Senhora', 'José de Alencar', 'Romance', '1875', 'OL01234M', 0, 'Aurélia Camargo, enriquecida repentinamente, compra o casamento com seu antigo amor para se vingar da rejeição que sofreu.');

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
--('func1', 'senha123', 1),
--('func2', 'senha456', 1),
--('bibliotecario', 'admin2025', 1),
--('gerente', 'supervisor', 1);

-- Clientes (admin = 0)
--'joao', 'joao123',
--'maria', 'maria456'
--'ana', 'ana789'
--'carlos', 'carlos321',
-- 'lucas', 'lucas654'
