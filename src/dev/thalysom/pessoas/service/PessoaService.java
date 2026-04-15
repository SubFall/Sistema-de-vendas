package dev.thalysom.pessoas.service;

import dev.thalysom.pessoas.domain.Endereco;
import dev.thalysom.pessoas.domain.PessoaFisica;
import dev.thalysom.pessoas.domain.PessoaJuridica;
import dev.thalysom.util.ConsoleUtils;

import java.time.LocalDate;
import java.util.Scanner;

public class PessoaService {

    public void cadastrarPessoa(Scanner scanner, CadastroPessoa cadastroPessoa) {
        Endereco endereco = new Endereco();

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

                System.out.println("*****Deseja Adicionar o Cep ?*****");
                System.out.println("1 - Sim");
                System.out.println("2 - Não");

                op = scanner.nextInt();
                scanner.nextLine();


                if (op == 1) {
                    endereco = cadastrarEndereco(endereco, scanner);

                }
                cadastroPessoa.adicionarEndereco(pessoaFisica, endereco);

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
                System.out.println("*****Deseja Adicionar o Cep ?*****");
                System.out.println("1 - Sim");
                System.out.println("2 - Não");

                op = scanner.nextInt();
                scanner.nextLine();

                if (op == 1) {
                    endereco = cadastrarEndereco(endereco, scanner);

                }
                cadastroPessoa.adicionarEndereco(pessoaJuridica, endereco);
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

    public Endereco cadastrarEndereco(Endereco endereco, Scanner scanner) {
        System.out.println("Digite o CEP:");
        System.out.print("R:");

        endereco.setCep(scanner.nextLine());

        System.out.println("Digite o Logradouro:");
        System.out.print("R:");

        endereco.setLogradouro(scanner.nextLine());

        System.out.println("Digite a Cidade:");
        System.out.print("R:");

        endereco.setCidade(scanner.nextLine());

        System.out.println("Digite a UF:");
        System.out.print("R:");

        endereco.setUf(scanner.nextLine());

        System.out.println("Digite o Número:");
        System.out.print("R:");

        endereco.setNumero(scanner.nextLine());

        return endereco;
    }
}
