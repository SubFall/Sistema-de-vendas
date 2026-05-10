package domain;

import util.DocumentoUtils;

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

    public int getIdade() {
        return Period.between(this.anoNascimento, LocalDate.now()).getYears();
    }

    @Override
    public void validarDocumento(String documento) {
        documento = documento.replaceAll("\\D", "");

        if (documento.length() != 11) {
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
        System.out.println("Nome:              " + getNome());
        System.out.println("Ano de Nascimento: " + getAnoNascimento());
        System.out.println("Idade:             " + getIdade());
        System.out.println("CPF:               " + DocumentoUtils.formatarCPF(getDocumento()));
        if (!getEnderecos().isEmpty()) {

            for (Endereco endereco : getEnderecos()) {
                System.out.println();
                System.out.println("******Endereço******");
                System.out.println("CEP:               " + endereco.getCep());
                System.out.println("Logradouro:        " + endereco.getLogradouro());
                System.out.println("Cidade:            " + endereco.getCidade());
                System.out.println("UF:                " + endereco.getUf());
                System.out.println("Nº:                " + endereco.getNumero());
                System.out.println();
            }
        }
    }
}
