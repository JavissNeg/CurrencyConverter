import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import models.LastConversionsRes;
import models.SupportedCodesRes;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static Gson gson = new Gson();
    public static HttpClient client = HttpClient.newHttpClient();
    public static final Dotenv dotenv = Dotenv.load();
    private static final String API_URL = dotenv.get("EXCHANGE_RATE_API_URL");


    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner sn = new Scanner(System.in);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_URL +"/codes"))
                .build();

        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        SupportedCodesRes supportedCodesRes = gson.fromJson(res.body(), SupportedCodesRes.class);

        int option = 0;

        do {
            System.out.println("\n*** Conversor de Monedas ***");
            System.out.println("1. Ver lista de códigos soportados");
            System.out.println("2. Realizar una conversión");
            System.out.println("3. Salir");
            System.out.print("Selecciona una opción: ");

            String optInput = sn.nextLine().trim();
            try {
                option = Integer.parseInt(optInput);
            } catch (NumberFormatException e) {
                System.out.println("Opción no válida.");
                continue;
            }

            switch (option) {
                case 1:
                    System.out.println("\n--- Códigos soportados ---");
                    for (List<String> currency : supportedCodesRes.supported_codes()) {
                        String code = currency.get(0);
                        String name = currency.get(1);
                        System.out.println(name + " -> (" + code + ")");
                    }
                    break;

                case 2:
                    System.out.print("Escribe el monto seguido del código de origen (Ej. 538 USD): ");
                    String input = sn.nextLine().trim();
                    if (input.length() < 4) {
                        System.out.println("Entrada inválida.");
                        break;
                    }

                    double amount;
                    try {
                        amount = Double.parseDouble(input.substring(0, input.length() - 3));
                    } catch (NumberFormatException e) {
                        System.out.println("Monto inválido.");
                        break;
                    }

                    String fromCurrencyCode = input.substring(input.length() - 3).toUpperCase();

                    System.out.print("Escriba el código de la moneda a la que desea convertir (Ej. MXN): ");
                    String toCurrencyCode = sn.nextLine().trim().toUpperCase();

                    req = HttpRequest.newBuilder()
                            .uri(URI.create(API_URL +"/latest/" + fromCurrencyCode))
                            .build();

                    res = client.send(req, HttpResponse.BodyHandlers.ofString());
                    LastConversionsRes lastConversionRes = gson.fromJson(res.body(), LastConversionsRes.class);

                    if ("error".equalsIgnoreCase(lastConversionRes.getResult())) {
                        System.out.println("Error: " + lastConversionRes.getErrorType());
                        break;
                    }

                    Double rate = lastConversionRes.getConversionRates().get(toCurrencyCode);
                    if (rate == null) {
                        System.out.println("Código destino no válido.");
                        break;
                    }

                    double result = amount * rate;
                    System.out.printf("%.2f %s equivale a %.2f %s%n", amount, fromCurrencyCode, result, toCurrencyCode);
                    break;


                case 3:
                    System.out.println("Programa finalizado.");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }

        } while (option != 3);
    }
}
