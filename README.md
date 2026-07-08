# Meu App

Aplicativo Android com 3 funcionalidades principais, acessíveis a partir da tela inicial:

1. **Bloco de Notas** — crie, edite e exclua anotações rápidas.
2. **Agenda de Contatos** — cadastre nome, e-mail, telefone e observações, com busca por nome.
3. **Controle de Gastos** — informe um salário/receita mensal (opcional), lance despesas e veja o total gasto e o saldo do mês (o saldo só é exibido se o salário for informado).

Todos os dados ficam salvos localmente no próprio celular (banco de dados SQLite + preferências), não é necessário internet nem servidor.

---

## Como gerar o APK usando o GitHub (sem precisar instalar Android Studio)

### Passo 1 — Criar um repositório no GitHub
1. Acesse [github.com](https://github.com) e faça login (ou crie uma conta).
2. Clique em **New repository** (Novo repositório).
3. Dê um nome, por exemplo `meu-app-android`, marque como **Public** ou **Private** e clique em **Create repository**.

### Passo 2 — Enviar (upload) os arquivos deste projeto
**Opção mais simples (pelo navegador, sem usar comandos):**
1. Descompacte o arquivo `.zip` que você recebeu no seu computador.
2. No repositório recém-criado no GitHub, clique em **Add file > Upload files**.
3. Arraste **todos** os arquivos e pastas do projeto descompactado (incluindo a pasta oculta `.github`) para a área de upload.
   - ⚠️ Importante: certifique-se de que a pasta `.github/workflows/build-apk.yml` também foi enviada — é ela que compila o app automaticamente.
4. Clique em **Commit changes**.

**Opção via linha de comando (Git):**
```bash
cd MeuApp
git init
git add .
git commit -m "Primeira versão do app"
git branch -M main
git remote add origin https://github.com/SEU-USUARIO/meu-app-android.git
git push -u origin main
```

### Passo 3 — Acompanhar a compilação automática
1. No repositório, clique na aba **Actions**.
2. Você verá um workflow chamado **Build APK** rodando (ou você pode clicar em **Run workflow** para iniciar manualmente).
3. Aguarde alguns minutos até aparecer o ícone verde ✅ de sucesso.

### Passo 4 — Baixar o APK
1. Ainda na aba **Actions**, clique na execução concluída (com ✅).
2. Role até a seção **Artifacts** (Artefatos) no final da página.
3. Clique em **app-debug-apk** para baixar um arquivo `.zip` contendo o `app-debug.apk`.
4. Descompacte e transfira o `app-debug.apk` para o seu celular Android (por e-mail, Google Drive, cabo USB, etc).
5. No celular, abra o arquivo APK para instalar (pode ser necessário permitir "instalar de fontes desconhecidas" nas configurações do Android).

---

## Estrutura do projeto
```
MeuApp/
├── app/
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/meuapp/
│       │   ├── MainActivity.kt          -> Tela inicial com os 3 ícones
│       │   ├── db/DBHelper.kt           -> Banco de dados SQLite
│       │   ├── notas/                   -> Bloco de notas
│       │   ├── agenda/                  -> Agenda de contatos
│       │   └── despesas/                -> Controle de gastos
│       └── res/                         -> Layouts, cores, ícones
├── .github/workflows/build-apk.yml      -> Compilação automática do APK
├── build.gradle
├── settings.gradle
└── gradle.properties
```

## Personalizar o app
- **Nome do app**: altere em `app/src/main/res/values/strings.xml` (`app_name`).
- **Cores**: altere em `app/src/main/res/values/colors.xml`.
- **Ícone do app**: substitua o conteúdo de `app/src/main/res/drawable/ic_launcher.xml`.

## Observações técnicas
- Linguagem: Kotlin
- Banco de dados: SQLite nativo do Android (via `SQLiteOpenHelper`)
- minSdk: 21 (Android 5.0+) — compatível com praticamente qualquer celular Android atual
- O workflow gera um **APK de debug**, ideal para testar no seu próprio celular. Caso queira publicar na Play Store futuramente, será necessário gerar uma versão assinada (release).
