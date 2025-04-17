import org.json.JSONObject;
import io.github.cdimascio.dotenv.Dotenv;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConversorCompleto extends JFrame {

    private JTextField campoValor;
    private JComboBox<String> comboOrigem, comboDestino;
    private JLabel labelResultado;

    // API Key carregada do .env
    private final String apiKey;

    public ConversorCompleto() {
        Dotenv dotenv = Dotenv.load();
        apiKey = dotenv.get("EXCHANGE_API_KEY");

        setTitle("Conversor de Moedas - Tempo Real");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        campoValor = new JTextField();
        add(new JLabel("Informe o valor a converter:"));
        add(campoValor);

        String[] moedas = {"BRL", "USD", "EUR", "GBP", "JPY", "ARS"};
        comboOrigem = new JComboBox<>(moedas);
        comboDestino = new JComboBox<>(moedas);

        add(new JLabel("Moeda de origem:"));
        add(comboOrigem);
        add(new JLabel("Moeda de destino:"));
        add(comboDestino);

        JButton botaoConverter = new JButton("Converter");
        add(botaoConverter);

        labelResultado = new JLabel("Resultado: ");
        labelResultado.setFont(new Font("Arial", Font.BOLD, 14));
        add(labelResultado);

        botaoConverter.addActionListener(e -> converter());

        setVisible(true);
    }

    private void converter() {
        try {
            double valor = Double.parseDouble(campoValor.getText());
            String origem = comboOrigem.getSelectedItem().toString();
            String destino = comboDestino.getSelectedItem().toString();

            if (origem.equals(destino)) {
                labelResultado.setText("Resultado: " + valor + " " + destino);
                return;
            }

            // URL com base na moeda de origem
            String urlStr = "https://api.apilayer.com/exchangerates_data/latest?base=" + origem + "&symbols=" + destino;
            URL url = new URL(urlStr);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setRequestProperty("apikey", apiKey); // cabeçalho com a chave

            BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            StringBuilder resposta = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                resposta.append(linha);
            }
            reader.close();

            JSONObject json = new JSONObject(resposta.toString());
            double taxa = json.getJSONObject("rates").getDouble(destino);

            double convertido = valor * taxa;
            labelResultado.setText(String.format("Resultado: %.2f %s = %.2f %s", valor, origem, convertido, destino));

        } catch (Exception e) {
            labelResultado.setText("Erro: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConversorCompleto::new);
    }
}
