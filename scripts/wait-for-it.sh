#!/usr/bin/env bash
# wait-for-it.sh — espera um host e porta ficarem disponíveis antes de rodar o comando

HOST="$1"
PORT="$2"
shift 2
CMD="$@"

echo "⏳ Aguardando $HOST:$PORT ficar disponível..."

while ! nc -z "$HOST" "$PORT"; do
  sleep 1
done

echo "✅ $HOST:$PORT está disponível! Executando comando..."
exec $CMD
