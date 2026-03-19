package dev.thalysom.pessoas.domain;

import dev.thalysom.util.ConsoleUtils;

import java.time.LocalDate;
import java.util.Scanner;

public class ConsoleMenu {
    public Scanner scanner = new Scanner(System.in);
    public CadastroPessoa cadastroPessoa = new CadastroPessoa();

    public void iniciar() {
        int opcao;
        do {
            mostrarMenu();
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    cadastrarPessoa(scanner, cadastroPessoa);
                    break;
                case 2:
                    cadastroPessoa.listar();
                    break;
                case 3:
                    removerPessoa(scanner, cadastroPessoa);
                    break;
                case 4:
                    System.out.println("Sainda do programa");
                    break;
                default:
                    System.out.println("Opção inválida");
            }

        } while (opcao != 4);
    }

    private static void mostrarMenu() {
        System.out.println("1 - Cadastrar");
        System.out.println("2 - Listar");
        System.out.println("3 - Remover");
        System.out.println("4 - Sair");
    }

    private static void cadastrarPessoa(Scanner scanner, CadastroPessoa cadastroPessoa) {
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

                System.out.print("Digite a data de nascimento (AAAA-MM-DD): ");
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

    private static void removerPessoa(Scanner scanner, CadastroPessoa cadastroPessoa) {
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

