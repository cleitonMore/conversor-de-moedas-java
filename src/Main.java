import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    private static final String API_URL =
            "https://v6.exchangerate-api.com/v6/1f18a16c7d02329255fcca16/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=============================================");
        System.out.println(" CONVERSOR DE MOEDAS - CHALLENGE ONE ");
        System.out.println("=============================================");

        System.out.println("Criado por Cleiton moreira");

        while (true) {
            System.out.println("\nEscolha uma conversão:");
            System.out.println("1 - USD → BRL");
            System.out.println("2 - BRL → USD");
            System.out.println("3 - EUR → BRL");
            System.out.println("4 - BRL → EUR");
            System.out.println("5 - USD → EUR");
            System.out.println("6 - EUR → USD");
            System.out.println("7 - Sair");

            System.out.print("Opção: ");
            int opcao = scanner.nextInt();

            if (opcao == 7) {
                System.out.println("Encerrando o programa...");
                break;
            }

            System.out.print("Digite o valor a converter: ");
            double valor = scanner.nextDouble();

            String from = "";
            String to = "";

            switch (opcao) {
                case 1 -> { from = "USD"; to = "BRL"; }
                case 2 -> { from = "BRL"; to = "USD"; }
                case 3 -> { from = "EUR"; to = "BRL"; }
                case 4 -> { from = "BRL"; to = "EUR"; }
                case 5 -> { from = "USD"; to = "EUR"; }
                case 6 -> { from = "EUR"; to = "USD"; }
                default -> {
                    System.out.println("Opção inválida!");
                    continue;
                }
            }

            try {
                double resultado = converterMoeda(from, to, valor);
                System.out.printf("\n%.2f %s = %.2f %s\n", valor, from, resultado, to);
            } catch (Exception e) {
                System.out.println("Erro ao converter moeda: " + e.getMessage());
            }
        }
    }

    public static double converterMoeda(String from, String to, double valor)
            throws IOException, InterruptedException {

        // A URL da API usa a moeda base (from)
        String url = API_URL + from;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Erro ao acessar API! Status: " + response.statusCode());
        }

        // Parse simples do JSON para extrair a taxa
        String json = response.body();

        String busca = "\"" + to + "\":";
        int index = json.indexOf(busca);

        if (index == -1) {
            throw new RuntimeException("Moeda não encontrada na API!");
        }

        int start = index + busca.length();
        int end = json.indexOf(",", start);

        String valorString = json.substring(start, end).trim();
        double taxa = Double.parseDouble(valorString);

        return valor * taxa;
    }
}

