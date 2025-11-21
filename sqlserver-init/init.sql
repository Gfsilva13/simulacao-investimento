IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'investimentos')
BEGIN
    CREATE DATABASE investimentos;
END