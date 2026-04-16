package dev.thalysom.pessoas.util;

public class DocumentoUtils {
    public static String formatarCPF(String cpf) {
        if (cpf == null || cpf.isBlank()) return null;

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF inválido");
        }
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static String formatarCNPJ(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) return null;

        cnpj = cnpj.replaceAll("\\D", "");

        if (cnpj.length() != 14) {
            throw new IllegalArgumentException("CNPJ Inválido");
        }

        return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }
}
