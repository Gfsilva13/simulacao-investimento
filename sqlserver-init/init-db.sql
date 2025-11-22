CREATE DATABASE investimentos;
GO

CREATE LOGIN appuser WITH PASSWORD = 'StrongPass123!';
GO

USE investimentos;
CREATE USER appuser FOR LOGIN appuser;
ALTER ROLE db_owner ADD MEMBER appuser;
GO
