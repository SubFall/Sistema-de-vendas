package dev.thalysom.pessoas.domain;

import dev.thalysom.util.DocumentoUtils;

public class PessoaJuridica extends Pessoa{
    @Override
    public void validarDocumento(String documento) {
        documento = documento.replaceAll("\\D", "");

        if (documento.length() < 14 || documento.isBlank()) {
            throw new IllegalArgumentException("CNPJ deve conter 14 caracteres");
        }

        if (!documento.matches("\\d+")) {
            throw new IllegalArgumentException("CNPJ deve conter apenas números");
        }
    }
    @Override
    public void mostrarDados() {
        System.out.println("Pessoa Jurídica: " + getNome() + " CNPJ: " + DocumentoUtils.formatarCNPJ(getDocumento()));
    }
}
