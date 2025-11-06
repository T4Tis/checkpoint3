#!/bin/bash
set -e

echo ">>> Instalando dependências (jq)..."
apt update
apt install -y jq

echo ">>> AGUARDANDO APLICAÇÃO INICIAR (sleep 30s)..."
sleep 30

# --- 1. Teste POST (Create) ---
echo ">>> Testando POST /pacientes"
HTTP_STATUS=$(
    curl -X 'POST' \
    'http://localhost:9000/pacientes' \
    -H 'accept: */*' \
    -H 'Content-Type: application/json' \
    -w "%{http_code}" \
    -o paciente_create.json \
    -d '{"nome": "Paciente Teste CI", "endereco": "Rua Fiap, 123", "bairro": "Vila Olimpia", "email": "teste.ci@fiap.com.br", "telefoneCompleto": "+5511987654321", "dataNascimento": "25-10-1990"}'
)
echo "Status HTTP (POST): $HTTP_STATUS"
if [ "$HTTP_STATUS" -ne 201 ]; then
    echo "Erro ao criar (POST)"
    cat paciente_create.json
    exit 1  
fi

# Extrai o ID do paciente que acabamos de criar
PACIENTE_ID=$(jq '.id' paciente_create.json)
echo "Paciente criado com ID: $PACIENTE_ID"


# --- 2. Teste PUT (Update) ---
echo ">>> Testando PUT /pacientes/$PACIENTE_ID"
HTTP_STATUS=$(
    curl -X 'PUT' \
    "http://localhost:9000/pacientes/$PACIENTE_ID" \
    -H 'accept: */*' \
    -H 'Content-Type: application/json' \
    -w "%{http_code}" \
    -o paciente_update.json \
    -d '{"nome": "Paciente ATUALIZADO", "email": "updated@fiap.com.br", "dataNascimento": "15-02-1985", "endereco": "Rua Nova, 999", "bairro": "Bairro Novo", "telefoneCompleto": "+5511999999999"}'
)
echo "Status HTTP (PUT): $HTTP_STATUS"
if [ "$HTTP_STATUS" -ne 200 ]; then
    echo "Erro ao atualizar (PUT)"
    cat paciente_update.json
    exit 1
fi


# --- 3. Teste DELETE (Delete) ---
echo ">>> Testando DELETE /pacientes/$PACIENTE_ID"
HTTP_STATUS=$(
    curl -X 'DELETE' \
    "http://localhost:9000/pacientes/$PACIENTE_ID" \
    -H 'accept: */*' \
    -w "%{http_code}"
)
echo "Status HTTP (DELETE): $HTTP_STATUS"
if [ "$HTTP_STATUS" -ne 204 ]; then
    echo "Erro ao deletar (DELETE)"
    exit 1
fi


# --- 4. Teste GET (Read All) ---
echo ">>> Testando GET /pacientes"
HTTP_STATUS=$(curl -X GET 'http://localhost:9000/pacientes' -o paciente_list.json -w "%{http_code}" -H 'accept: */*')
echo "Status HTTP (GET): $HTTP_STATUS"
if [ "$HTTP_STATUS" -ne 200 ]; then
    echo "Erro ao acessar (GET)"
    exit 1  
fi

echo ">>> TODOS OS TESTES DO CRUD PASSARAM <<<"