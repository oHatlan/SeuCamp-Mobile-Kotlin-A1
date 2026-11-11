# SeuCamp Mobile

Aplicativo Android para organizar torneios presenciais e cadastrar as equipes inscritas. O projeto segue a estrutura do Oficina do Tião da Aula 23: Compose, ViewModel com estado simples, repositórios, Room e Retrofit.

## Rodar

1. No Android Studio, escolha **Open** e selecione esta pasta (a que contém `settings.gradle.kts`).
2. Aguarde a sincronização do Gradle. O projeto usa a configuração da Aula 23, incluindo JDK 21 para o Gradle e Android SDK 36.1 para compilar.
3. Selecione um aparelho ou emulador e clique em **Run**. O app aceita Android 7.0 (API 24) ou superior.

Pelo terminal no Windows: `.\gradlew.bat :app:assembleDebug`. Com um emulador conectado, `.\gradlew.bat :app:connectedDebugAndroidTest` executa os testes de interface.

## Roteiro da A1

1. Abra **Jogos**, escolha um jogo e clique em **Novo torneio**. Informe nome, descrição e, se houver, premiação. Em **Adicionar local por CEP**, consulte um CEP válido, como `01001000`, salve e confira o endereço no detalhe.
2. No detalhe, inscreva uma equipe com cinco nicks. Edite a inscrição e confira a alteração na aba **Equipes**.
3. Use **Compartilhar convite** e escolha outro aplicativo para abrir o texto.
4. Feche e reabra o SeuCamp para conferir que torneio e inscrição persistiram. Cancele a inscrição, edite o torneio e depois exclua o torneio.

O menu segue o PHP: **Início**, **Jogos**, **Sobre**, **Torneios** e **Equipes**. O banco é local e tem duas tabelas: `torneio` e `inscricao`. Excluir um torneio também remove suas inscrições. A busca de CEP usa internet e pode ser pulada ao cadastrar um torneio.

## Onde está cada parte

- `MainActivity.kt`: constrói o banco e liga as telas aos ViewModels com as factories.
- `model/` e `data/`: entidades, DAOs, banco, repositórios e configuração do ViaCEP.
- `viewmodel/`: estado, validação, gravação e coroutines com `viewModelScope.launch`.
- `view/`: telas Compose. O compartilhamento por Intent está em `DetalheTorneio.kt`.
