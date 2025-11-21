#!/bin/bash
# Aguarda o SQL Server iniciar
/opt/mssql/bin/sqlservr &

echo "Aguardando SQL Server iniciar..."
sleep 20

echo "Executando script de criação do banco..."
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P "StrongPassword123" -i /docker-entrypoint-initdb.d/init.sql

wait