#!/bin/bash
set -e
# Start SQL Server
/opt/mssql/bin/sqlservr &

# Wait until SQL Server is ready
echo "Waiting for SQL Server to start..."
until /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P $SA_PASSWORD -Q "SELECT 1" > /dev/null 2>&1
do
  sleep 5
done

echo "SQL Server is up. Running initialization scripts..."
for f in /init/*.sql; do
    echo "Executing $f..."
    /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P $SA_PASSWORD -i $f
done

# Keep container running
fg %1
