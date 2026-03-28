package dev.thalysom.pessoas.domain;

import dev.thalysom.util.ConsoleUtils;

import java.time.LocalDate;
import java.util.Scanner;

public class PessoaService {

    public void cadastrarPessoa(Scanner scanner, CadastroPessoa cadastroPessoa) {
        System.out.println("1 - Pessoa Física");
        System.out.println("2 - Pessoa Jurídica");

        int op;
        op = scanner.nextInt();
        scanner.nextLine();

        switch (op) {
            case 1:
                PessoaFisica pessoaFisica = new PessoaFisica();

                System.out.print("Digite seu nome:");
                pessoaFisica.setNome(scanner.nextLine());

                System.out.print("Digite a data de nascimento (DD/MM/YYYY): ");
                LocalDate data = ConsoleUtils.lerData(scanner);
                pessoaFisica.setAnoNascimento(data);

                System.out.print("Digite seu CPF:");

                try {
                    pessoaFisica.setDocumento(scanner.nextLine());
                    cadastroPessoa.adicionar(pessoaFisica);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }

                break;
            case 2:
                PessoaJuridica pessoaJuridica = new PessoaJuridica();

                System.out.print("Digite seu nome:");
                pessoaJuridica.setNome(scanner.nextLine());

                System.out.print("Digite seu CNPJ:");

                try {
                    pessoaJuridica.setDocumento(scanner.nextLine());
                    cadastroPessoa.adicionar(pessoaJuridica);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }

                break;
        }

    }

    public void removerPessoa(Scanner scanner, CadastroPessoa cadastroPessoa) {
        System.out.println("1 - Pessoa Física");
        System.out.println("2 - Pessoa Jurídica");

        int op;
        op = scanner.nextInt();
        scanner.nextLine();

        switch (op) {
            case 1:
                System.out.print("Digite seu CPF:");
                cadastroPessoa.remover(scanner.nextLine());
                break;
            case 2:
                System.out.print("Digite seu CNPJ:");
                cadastroPessoa.remover(scanner.nextLine());
        }
    }
}
