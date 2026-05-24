package util;

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
}
