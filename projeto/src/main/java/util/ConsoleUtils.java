package util;

import java.math.BigDecimal;
import java.util.Scanner;

public final class ConsoleUtils {
    public static String formatarColuna(String valor, int tamanho) {

        if (valor == null) {
            valor = "";
        }

        if (valor.length() > tamanho) {
            return valor.substring(0, tamanho);
        }
        return String.format("%-" + tamanho + "s", valor);

    }

    public static int lerInteiro(Scanner scanner, String label) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Digite um " + label + " válido");
            }
        }
    }

    public static BigDecimal lerBigDecimal(Scanner scanner, String label) {
        while (true) {
            try {

                System.out.print(label + ": ");

                return new BigDecimal(scanner.nextLine().replace(",", "."));

            } catch (NumberFormatException e) {
                System.out.println("Valor inválido");
            }
        }
    }

    public static BigDecimal lerDecimal(Scanner scanner, String label) {
        while (true) {
            try {

                System.out.println(label + ": ");
                String valor = scanner.nextLine();

                if (valor.isBlank()) {
                    return null;
                }
                return new BigDecimal(valor.replace(",", "."));

            } catch (NumberFormatException e) {
                System.out.println("Valor inválido");
            }
        }
    }

    public static boolean confirmar(Scanner scanner, String mensagem) {
        int opcao;

        System.out.println(mensagem);

        do {
            System.out.println("1 - SIM");
            System.out.println("2 - NÃO");

            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");
        } while (opcao != 1 && opcao != 2);

        return opcao == 1;

    }
}
