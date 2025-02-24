import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import java.util.Scanner;

public class Dolar2Real {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Solicita a conversão desejada
        System.out.print("Escolha a conversão: \n1. Dólar para Real \n2. Real para Dólar\nOpção: ");
        String input = scanner.nextLine().trim();
        
        // Solicita o valor a ser convertido
        System.out.print("Digite o valor a converter: ");
        String valorInput = scanner.nextLine().trim();
        double valor;
        try {
            valor = Double.parseDouble(valorInput);
        } catch(NumberFormatException e) {
            System.out.println("Valor inválido!");
            scanner.close();
            return;
        }
        
        // URL da API para a cotação do dólar
        String url = "https://economia.awesomeapi.com.br/json/last/USD-BRL";
        
        // Cria o HttpClient e a requisição
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        
        try {
            // Envia a requisição e obtém a resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Faz o parse do JSON usando org.json
            JSONObject json = new JSONObject(response.body());
            JSONObject usdbrl = json.getJSONObject("USDBRL");
            
            // O campo "bid" contém a cotação do dólar no JSON
            String cotacaoStr = usdbrl.getString("bid");
            double cotacao = Double.parseDouble(cotacaoStr);
            
            // Verifica qual conversão o usuário deseja fazer
            if(input.equals("1")){
                // Converter de Dólar para Real
                double resultado = valor * cotacao;
                System.out.printf("R$ %.2f equivalem a R$ %.2f (cotação: %.4f)%n", valor, resultado, cotacao);
            } else if(input.equals("2")){
                // Converter de Real para Dólar
                double resultado = valor / cotacao;
                System.out.printf("R$ %.2f equivalem a US$ %.2f (cotação: %.4f)%n", valor, resultado, cotacao);
            } else {
                System.out.println("Opção inválida!");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao consultar a API: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
