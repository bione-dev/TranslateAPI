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
        optionsPanel.add(translateButton); 

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
