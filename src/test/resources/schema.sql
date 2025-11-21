CREATE TABLE parametro_produto (
    id INT IDENTITY PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    minValor FLOAT NOT NULL,
    maxPrazo INT NOT NULL,
    riscoAceito VARCHAR(50) NOT NULL
);

CREATE TABLE produto_investimento(
    id INT IDENTITY PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipoProduto VARCHAR(50) NOT NULL,
    rentabilidade FLOAT NOT NULL,
    risco VARCHAR(50) NOT NULL
);

CREATE TABLE Simulacao (
    id INT IDENTITY PRIMARY KEY,
    clienteId BIGINT NOT NULL,
    tipoProduto VARCHAR(50) NOT NULL,
    valorInicial FLOAT NOT NULL,
    prazoMeses INT NOT NULL,
    valorFinal FLOAT NOT NULL
);