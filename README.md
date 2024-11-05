```markdown
# TranslatorAppGUI - Tradutor com Interface Gráfica usando Google Cloud Translation API

Este é um projeto em Java que implementa um tradutor de textos com interface gráfica usando a **Google Cloud Translation API**. A aplicação permite que o usuário insira um texto, selecione o idioma de origem e o idioma de destino, e veja a tradução.

## Funcionalidades

- Interface gráfica em Java Swing para fácil interação.
- Tradução entre múltiplos idiomas usando a **Google Cloud Translation API**.
- Suporte para selecionar diferentes idiomas de origem e destino.
- Decodificação de entidades HTML no texto traduzido.

## Pré-requisitos

1. **Google Cloud Translation API Key**:
   - Configure uma conta no [Google Cloud Console](https://console.cloud.google.com/).
   - Crie um novo projeto e ative a **Cloud Translation API**.
   - Obtenha uma chave de API (API Key) para autenticação.

2. **Dependência Maven**:
   - Certifique-se de ter o Maven instalado no seu sistema.
   - O seguinte arquivo `pom.xml` deve ser usado para configurar as dependências do projeto:

     ```xml
     <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
         <modelVersion>4.0.0</modelVersion>

         <groupId>com.example</groupId>
         <artifactId>TranslateAPI</artifactId>
         <version>1.0-SNAPSHOT</version>

         <properties>
             <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
             <maven.compiler.source>17</maven.compiler.source>
             <maven.compiler.target>17</maven.compiler.target>
         </properties>

         <dependencies>
             <!-- Dependência para Google Cloud Translation API -->
             <dependency>
                 <groupId>com.google.cloud</groupId>
                 <artifactId>google-cloud-translate</artifactId>
                 <version>1.94.0</version>
             </dependency>
             <dependency>
                 <groupId>org.apache.commons</groupId>
                 <artifactId>commons-text</artifactId>
                 <version>1.9</version>
             </dependency>
         </dependencies>

         <build>
             <plugins>
                 <plugin>
                     <groupId>org.apache.maven.plugins</groupId>
                     <artifactId>maven-compiler-plugin</artifactId>
                     <version>3.8.1</version>
                     <configuration>
                         <source>17</source>
                         <target>17</target>
                     </configuration>
                 </plugin>
             </plugins>
         </build>
     </project>
     ```

## Como Usar

1. Clone este repositório para o seu ambiente local.
   
2. Configure a variável de ambiente `GOOGLE_APPLICATION_CREDENTIALS` para apontar para o arquivo JSON de credenciais do Google Cloud.

3. Compile e execute a aplicação. Você verá uma interface com as seguintes opções:
   - **Texto Original**: Insira o texto que deseja traduzir.
   - **Direção da Tradução**: Selecione a direção da tradução (Português para Inglês ou Inglês para Português).
   - **Botão Traduzir**: Clique para traduzir o texto inserido.

### Código Principal

O código principal da aplicação é exibido abaixo:

```java
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.apache.commons.text.StringEscapeUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TranslatorAppGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton translateButton;
    private JComboBox<String> languageDirectionCombo;
    private Translate translateService;

    public TranslatorAppGUI() {
        initializeTranslateService();

        setTitle("Tradutor PT-EN e EN-PT");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inputTextArea = new JTextArea(4, 30);
        outputTextArea = new JTextArea(4, 30);
        outputTextArea.setEditable(false);

        translateButton = new JButton("Traduzir");
        translateButton.setFont(new Font("Arial", Font.BOLD, 14));

        languageDirectionCombo = new JComboBox<>(new String[] {
            "Português para Inglês",
            "Inglês para Português"
        });

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Texto Original:"), BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JLabel("Tradução:"), BorderLayout.NORTH);
        outputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        JPanel optionsPanel = new JPanel();
        optionsPanel.add(new JLabel("Direção da tradução:"));
        optionsPanel.add(languageDirectionCombo);
        optionsPanel.add(translateButton); // Adiciona o botão de traduzir ao painel de opções

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.CENTER);
        add(outputPanel, BorderLayout.SOUTH);

        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                translateText();
            }
        });
    }

    private void initializeTranslateService() {
        translateService = TranslateOptions.getDefaultInstance().getService();
    }

    private void translateText() {
        String text = inputTextArea.getText();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira o texto para traduzir.");
            return;
        }

        String sourceLang;
        String targetLang;

        if (languageDirectionCombo.getSelectedItem().equals("Português para Inglês")) {
            sourceLang = "pt";
            targetLang = "en";
        } else {
            sourceLang = "en";
            targetLang = "pt";
        }

        Translation translation = translateService.translate(
            text,
            Translate.TranslateOption.sourceLanguage(sourceLang),
            Translate.TranslateOption.targetLanguage(targetLang)
        );

        // Decodificar entidades HTML no texto traduzido
        String translatedText = StringEscapeUtils.unescapeHtml4(translation.getTranslatedText());

        outputTextArea.setText(translatedText);
    }

    public static void main(String[] args) {
        Logger.getLogger("com.google").setLevel(Level.SEVERE);
        SwingUtilities.invokeLater(() -> new TranslatorAppGUI().setVisible(true));
    }
}
```

## Segurança

Para proteger sua chave de API:
- Evite expor a chave diretamente em repositórios públicos.
- Use variáveis de ambiente para armazenar a chave de API.
- No Google Cloud Console, limite o acesso da chave para evitar usos indevidos.

## Notas Importantes

- **Custos da API**: A Google Cloud Translation API é paga, embora ofereça uma cota gratuita para novos usuários. Certifique-se de monitorar o uso e configurar alertas para evitar cobranças inesperadas.
- **Melhorias Futuras**:
  - Adicionar suporte para mais idiomas.
  - Implementar Text-to-Speech para ouvir a tradução.
  - Armazenar o histórico de traduções para acesso rápido.
```
