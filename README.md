# Desafio Técnico – Desenvolvedor Android Especialista 

Demo:
https://github.com/user-attachments/assets/7123275a-6022-4870-a830-ccddaa63b25f

Download APK:
https://github.com/dilersik/applicationtest/blob/main/app-debug.apk

Objetivo: Criar um aplicativo Android nativo em Kotlin que implemente segurança, uso de sensores, animações e integração com uma API REST. O projeto deve ser bem arquitetado e seguir boas práticas de desenvolvimento.

Requisitos do Desafio

1. Sensores e Algoritmo – Geração de UUID Personalizado
Criar um identificador único baseado em sensores do dispositivo.
Utilizar pelo menos 2 sensores disponíveis (Exemplo: acelerômetro, giroscópio, luz, proximidade) para compor a geração deste identificador.
Complementar o UUID com alguma informação específica do hardware (Exemplo: fabricante, modelo, versão do SO).
O identificador deve ser único para cada dispositivo e persistente entre execuções do app.
O algoritmo de geração deve estar presente na aplicação (não usar os valores obtidos como seed de bibliotecas geradoras de UUID)

2. Segurança – Autenticação com Biometria
O app deve exibir uma tela de login com um botão para autenticação por biometria (digital, reconhecimento facial ou outro, conforme suportado pelo dispositivo).
Armazenar o estado do login para evitar necessidade de autenticação repetitiva na mesma sessão.
Usar o UUID gerado na etapa anterior como identificador do usuário

3. Layout – Animação estilo "DVD Screensaver"
Este trecho deve ser exibido para o usuário após o login.
Criar um layout onde uma mensagem flutua na tela, mudando de direção ao colidir com as bordas, simulando o famoso efeito de logo de DVD.
A mensagem deve se movimentar de forma fluida e mudar de cor ao atingir um canto da tela.
A cada X segundos a mensagem deve ser trocada

3.1 - Extra - Eventos
Quando o usuário clicar na mensagem, uma nova mensagem deve ser exibida.
3.2 - Extra - Abstração/arquitetura
As ultimas 256 mensagens devem ser persistidas na sessão, mesmo que repetidas. Ao fechar o app elas devem ser descartadas;
Na persistencia, usar uma estrutura para mensagem obtidas no mock e outra para obtidas na requisição REST. Na estrutura
"REST" incluir o endereço utilizado para cada mensagem. Na estrutura "Mock", a propriedade endereço não deve existir. Como no exemplo:

data class Mensagem(
@SerializedName("Texto")
val Texto: String
)

data class Mensagem(
@SerializedName("Texto")
val Texto: String,

@SerializedName("Endereco")
val Endereco: String
)

4. Integração – API REST com Mock Option
Integrar o aplicativo com qualquer API REST pública (Exemplo: https://jsonplaceholder.typicode.com).
Criar um botão para alternar entre requisições reais e um mock de dados local.
As mensagens exibidas no item "3. Layout – Animação estilo "DVD Screensaver"" devem ser obtidas de valores devolvidos na requisição.

5. PopOver Genérico para Seleção de Data e Hora
Criar um componente reutilizável para exibição de um popOver contendo um DatePicker e/ou um TimePicker, permitindo personalização e flexibilidade para diferentes usos no app.

Requisitos do PopOver:

•	Personalizável: Deve permitir configurar se exibirá apenas o DatePicker, apenas o TimePicker ou ambos (pelo menos um deve ser obrigatório).
•	Valores Padrão: Deve aceitar valores default de data e hora.
•	Range de Seleção: Caso o DatePicker seja ativado, permitir definir um intervalo mínimo e máximo de datas selecionáveis.
•	Cores Customizáveis: Deve ser possível personalizar cores principais do componente.
•	Retorno dos Dados: O Fragment que chamar o popOver deve receber os valores selecionados via um callback ou ViewModel compartilhado.
•	Suporte a Valores Nulos: Se o TimePicker ou DatePicker não for exibido, o valor retornado para esse campo deve ser null.
