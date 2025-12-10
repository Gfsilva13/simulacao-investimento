CREATE DATABASE simulacao_db;
USE simulacao_db;

CREATE TABLE cliente (
    id BIGINT IDENTITY PRIMARY KEY,
    nome NVARCHAR(255) NOT NULL,
    perfilRisco NVARCHAR(50) NOT NULL,
    pontuacao INT NOT NULL
);

CREATE TABLE parametro_produto (
    id BIGINT IDENTITY PRIMARY KEY,
    minValor FLOAT NOT NULL,
    maxPrazo INT NOT NULL,
    riscoAceito NVARCHAR(50) NOT NULL
);

CREATE TABLE produto_investimento (
    id BIGINT IDENTITY PRIMARY KEY,
    nomeProduto NVARCHAR(255) NOT NULL,
    tipoProduto NVARCHAR(50) NOT NULL,
    rentabilidade FLOAT NOT NULL,
    parametroId BIGINT NOT NULL,
    FOREIGN KEY (parametroId) REFERENCES parametro_produto(id)
);

CREATE TABLE investimento (
    id BIGINT IDENTITY PRIMARY KEY,
    clienteId BIGINT NOT NULL,
    produtoId BIGINT NOT NULL,
    tipo NVARCHAR(50) NOT NULL,
    valor FLOAT NOT NULL,
    rentabilidade FLOAT NOT NULL,
    prazoMeses INT NOT NULL,
    data DATETIME2 NOT NULL,
    FOREIGN KEY (clienteId) REFERENCES cliente(id),
    FOREIGN KEY (produtoId) REFERENCES produto_investimento(id)
);

CREATE TABLE simulacao (
    id BIGINT IDENTITY PRIMARY KEY,
    clienteId BIGINT NOT NULL,
    produtoId BIGINT NOT NULL,
    valorInvestido FLOAT NOT NULL,
    valorFinal FLOAT NOT NULL,
    prazoMeses INT NOT NULL,
    dataSimulacao DATETIME2 NOT NULL,
    FOREIGN KEY (clienteId) REFERENCES cliente(id),
    FOREIGN KEY (produtoId) REFERENCES produto_investimento(id)
);

CREATE TABLE telemetria (
    id BIGINT IDENTITY PRIMARY KEY,
    servico NVARCHAR(100) NOT NULL,
    quantidadeChamadas INT NOT NULL,
    mediaTempoRespostaMs INT NOT NULL,
    periodoInicial DATE NOT NULL,
    periodoFim DATE NOT NULL
);
