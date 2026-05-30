package util;

import domain.pessoa.PessoaPapel;

import java.util.List;
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
}
