CREATE DATABASE IF NOT EXISTS loginapp;
USE loginapp;

-- Criação da tabela usuario
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    admin BOOLEAN NOT NULL
);
CREATE TABLE livro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    genero VARCHAR(100),
    dataLancamento VARCHAR(20),
    isbn VARCHAR(30),
    disponivel BOOLEAN NOT NULL,
  	sinopse TEXT
);
-- Funcionários (admin = 1)
INSERT INTO usuario (username, password, admin) VALUES
('func1', 'senha123', 1),
('func2', 'senha456', 1),
('bibliotecario', 'admin2025', 1),
('gerente', 'supervisor', 1);

-- Clientes (admin = 0)
INSERT INTO usuario (username, password, admin) VALUES
('joao', 'joao123', 0),
('maria', 'maria456', 0),
('ana', 'ana789', 0),
('carlos', 'carlos321', 0),
('lucas', 'lucas654', 0);


INSERT INTO livro (titulo, autor, genero, datalancamento, isbn, disponivel, sinopse) VALUES
('Dom Casmurro', 'Machado de Assis', 'Romance', '1899', '9788572329795', 1, 'Narrado por Bentinho, o romance explora dúvidas sobre a fidelidade de sua esposa Capitu, marcada por sua ambiguidade e ciúmes.'),
('O Pequeno Príncipe', 'Antoine de Saint-Exupéry', 'Fábula', '1943', '9788522005237', 1, 'Um jovem príncipe viaja por planetas e compartilha reflexões sobre a vida, o amor e a amizade, em uma fábula poética e filosófica.'),
('1984', 'George Orwell', 'Distopia', '1949', '9788535902774', 1, 'Em um regime totalitário liderado pelo Grande Irmão, Winston Smith luta contra a opressão e manipulação da verdade.'),
('Capitães da Areia', 'Jorge Amado', 'Romance', '1937', '9788520932306', 1, 'Conta a história de um grupo de meninos de rua em Salvador e suas lutas por sobrevivência e dignidade.'),
('Harry Potter e a Pedra Filosofal', 'J.K. Rowling', 'Fantasia', '1997', '9788532511016', 1, 'O jovem Harry descobre ser um bruxo e inicia sua jornada em Hogwarts, onde enfrenta perigos e descobre seu passado.'),
('A Revolução dos Bichos', 'George Orwell', 'Sátira', '1945', '9788535909551', 0, 'Os animais de uma fazenda se rebelam contra seus donos humanos, mas acabam recriando uma tirania semelhante entre eles.'),
('Memórias Póstumas de Brás Cubas', 'Machado de Assis', 'Romance', '1881', '9788535914845', 1, 'Brás Cubas narra sua vida após a morte, com ironia e crítica à sociedade brasileira do século XIX.'),
('O Hobbit', 'J.R.R. Tolkien', 'Fantasia', '1937', '9788595084742', 1, 'Bilbo Bolseiro embarca em uma aventura com anões para recuperar um tesouro guardado por um dragão.'),
('A Moreninha', 'Joaquim Manuel de Macedo', 'Romance', '1844', '9788579026062', 1, 'Augusto, um estudante de medicina, se apaixona por Carolina durante um fim de semana em uma ilha, cumprindo uma antiga promessa.'),
('Senhora', 'José de Alencar', 'Romance', '1875', '9788535914846', 0, 'Aurélia Camargo, enriquecida repentinamente, compra o casamento com seu antigo amor para se vingar da rejeição que sofreu.');
