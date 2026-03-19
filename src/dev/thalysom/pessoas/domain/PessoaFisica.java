package dev.thalysom.pessoas.domain;

import java.time.LocalDate;
import java.time.Period;

public class PessoaFisica extends Pessoa {
    private LocalDate anoNascimento;

    public void setAnoNascimento(LocalDate anoNascimento) {
        LocalDate hoje = LocalDate.now();

        if (anoNascimento.isAfter(hoje)) {
            throw new IllegalArgumentException("Não pode inserir Data Futura!");
        }

        int idade = Period.between(anoNascimento, hoje).getYears();

        if (idade > 130) {
            throw new IllegalArgumentException("Idade inválida");
        }

        this.anoNascimento = anoNascimento;
    }

    public LocalDate getAnoNascimento() {
        return this.anoNascimento;
    }

    public int getIdade() { return Period.between(this.anoNascimento, LocalDate.now()).getYears(); }

    @Override
    public void validarDocumento(String documento) {
        documento = documento.replaceAll("\\D", "");

        if (documento.length() != 11 || documento.isBlank()) {
            throw new IllegalArgumentException("CPF deve conter 11 caracteres");
        }

        if (!documento.matches("\\d+")) {
            throw new IllegalArgumentException("CPF deve conter apenas números");
        }
    }

    @Override
    public void mostrarDados() {
        System.out.println();
        System.out.println("####### Listando Pessoas #######");
        System.out.println();
        System.out.println("Nome: " + getNome());
        System.out.println("Ano de Nascimento: " + getAnoNascimento());
        System.out.println("CPF: " + getDocumento());
        System.out.println();
    }
}
