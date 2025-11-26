-- Cria o banco de dados
CREATE DATABASE investimentos;
GO

-- Cria o login do usuário
CREATE LOGIN appuser WITH PASSWORD = 'Senhaforte123456';
GO

-- Usa o banco de dados investimentos
USE investimentos;
GO

-- Cria o usuário do banco vinculado ao login
CREATE USER appuser FOR LOGIN appuser;
GO

-- Dá permissão de db_owner ao usuário
ALTER ROLE db_owner ADD MEMBER appuser;
GO
