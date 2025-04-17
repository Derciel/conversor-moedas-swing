#!/bin/bash

# === CONFIGURAÇÕES ===
EMAIL="dercieljr2@gmail.com"               # Altere para seu e-mail do GitHub
PROJETO="conversorswing" Nome da pasta do projeto
REPO_SSH="git@github.com:derciel/$PROJETO.git"  # Substitua seu-usuario

echo "🚀 Gerando nova chave SSH..."
ssh-keygen -t ed25519 -C "$EMAIL" -f ~/.ssh/id_ed25519 -N ""

echo "🔐 Adicionando chave ao ssh-agent..."
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519

echo "📋 Copie a chave abaixo e cole no GitHub:"
echo "-----------------------------------------"
cat ~/.ssh/id_ed25519.pub
echo "-----------------------------------------"
read -p "❗Pressione ENTER após adicionar a chave no GitHub..."

echo "📁 Criando projeto local: $PROJETO"
mkdir "$PROJETO"
cd "$PROJETO"
git init

echo "# $PROJETO" > README.md
git add README.md
git commit -m "Commit inicial com SSH configurado"

echo "🔗 Conectando ao repositório remoto..."
git remote add origin "$REPO_SSH"

echo "⬆️ Fazendo push para o GitHub..."
git branch -M main
git push -u origin main

echo "✅ Tudo pronto! Projeto '$PROJETO' foi enviado para o GitHub via SSH"
