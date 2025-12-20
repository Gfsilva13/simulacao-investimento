-- Inserindo clientes
INSERT INTO cliente (nome, perfilRisco, pontuacao) VALUES ('Joao', 'Conservador', 50);
INSERT INTO cliente (nome, perfilRisco, pontuacao) VALUES ('Jose', 'Moderado', 70);
INSERT INTO cliente (nome, perfilRisco, pontuacao) VALUES ('Silva', 'Agressivo', 90);

-- Inserindo parâmetros de produto
INSERT INTO parametro_produto (minValor, maxPrazo, riscoAceito) VALUES (100.00, 36, 'Baixo');
INSERT INTO parametro_produto (minValor, maxPrazo, riscoAceito) VALUES (500.00, 60, 'Medio');
INSERT INTO parametro_produto (minValor, maxPrazo, riscoAceito) VALUES (1000.00, 24, 'Alto');

-- Inserindo produtos de investimento
INSERT INTO produto_investimento (nomeProduto, tipoProduto, rentabilidade, parametroId) VALUES ('CDB Caixa 2026', 'CDB', 0.12, 1);
INSERT INTO produto_investimento (nomeProduto, tipoProduto, rentabilidade, parametroId) VALUES ('RF Caixa 2026', 'RF', 1.20, 2);
INSERT INTO produto_investimento (nomeProduto, tipoProduto, rentabilidade, parametroId) VALUES ('LCI Caixa 2026', 'LCI', 1.80, 3);

-- Inserindo investimentos
INSERT INTO investimento (clienteId, produtoId, tipo, valor, rentabilidade, prazoMeses, data) VALUES (2, 2, 'RF', 10000, 100, 60, GETDATE());
INSERT INTO investimento (clienteId, produtoId, tipo, valor, rentabilidade, prazoMeses, data) VALUES (1, 1, 'CDB', 5000, 5, 36, GETDATE());
INSERT INTO investimento (clienteId, produtoId, tipo, valor, rentabilidade, prazoMeses, data) VALUES (1, 3, 'LCI', 10000, 200, 24, GETDATE());
