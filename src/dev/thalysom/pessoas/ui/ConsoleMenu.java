package dev.thalysom.pessoas.ui;

import dev.thalysom.pessoas.domain.Pessoa;
import dev.thalysom.pessoas.domain.PessoaFisica;
import dev.thalysom.pessoas.domain.PessoaJuridica;
import dev.thalysom.pessoas.service.PessoaService;
import dev.thalysom.pessoas.util.ConsoleUtils;

import java.time.LocalDate;
import java.util.Scanner;

public class ConsoleMenu {
    private Scanner scanner = new Scanner(System.in);
    private PessoaService service;

    public ConsoleMenu(PessoaService service) {
        this.service = service;
    }

    public void iniciar() {

        int opcao;

        do {
            System.out.println("\n===== MENU =====");
            System.out.println("1 - Cadastrar Pessoa Física");
            System.out.println("2 - Cadastrar Pessoa Jurídica");
            System.out.println("3 - Listar");
            System.out.println("0 - Sair");

            System.out.print("Escolha: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarPessoaFisica();
                    break;
                case 2:
                    cadastrarPessoaJuridica();
                    break;
                case 3:
                    service.listar().forEach(Pessoa::mostrarDados);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }

        } while (opcao != 0);
    }

    private void cadastrarPessoaFisica() {
        PessoaFisica p = new PessoaFisica();

        System.out.print("Nome: ");
        p.setNome(scanner.nextLine());

        boolean documentoValido = false;

        while (!documentoValido) {
            try {
                System.out.print("CPF: ");
                p.setDocumento(scanner.nextLine());

                System.out.print("Digite a data de nascimento (DD/MM/YYYY): ");
                LocalDate data = ConsoleUtils.lerData(scanner);
                p.setAnoNascimento(data);

                documentoValido = true;

            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }

        service.cadastrar(p);
        System.out.println("Pessoa cadastrada com sucesso!");;
    }

    private void cadastrarPessoaJuridica() {
        PessoaJuridica p = new PessoaJuridica();

        System.out.print("Nome: ");
        p.setNome(scanner.nextLine());

        boolean documentoValido = false;

        while (!documentoValido) {
            try {
                System.out.print("CNPJ: ");
                p.setDocumento(scanner.nextLine());

                documentoValido = true;

            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }

        service.cadastrar(p);
        System.out.println("Pessoa cadastrada com sucesso!");
    }

    //    public void iniciar() {
//        int opcao;
//        do {
//            mostrarMenu();
//            opcao = scanner.nextInt();
//
//            switch (opcao) {
//                case 1:
//                    pessoaService.cadastrarPessoa(scanner, pessoaRepository);
//                    break;
//                case 2:
//                    pessoaRepository.listar();
//                    break;
//                case 3:
//                    pessoaService.removerPessoa(scanner, pessoaRepository);
//                    break;
//                case 4:
//                    mostrarFiltro();
//                    break;
//                case 5:
//                    System.out.println("Sainda do programa");
//                    break;
//                default:
//                    System.out.println("Opção inválida");
//            }
//
//        } while (opcao != 5);
//    }
//
//    private static void mostrarMenu() {
//        System.out.println("####### Lua.Net #######");
//        System.out.println("=======================");
//        System.out.println("|       Pessoas        |");
//        System.out.println("=======================");
//        System.out.println("1 - Cadastrar");
//        System.out.println("2 - Listar");
//        System.out.println("3 - Remover");
//        System.out.println("4 - Filtrar");
//        System.out.println("5 - Sair");
//    }
//
//    private void mostrarFiltro() {
//        int opcao;
//        System.out.println("1 - Filtrar por Nome:");
//        System.out.println("2 - Filtrar por Documento:");
//        opcao = scanner.nextInt();
//        scanner.nextLine();
//
//        switch (opcao) {
//            case 1:
//                System.out.println("2 - Digite o Nome:");
//                String nome = scanner.nextLine();
//                List<Pessoa> pessoasNomes = pessoaRepository.buscar(p -> p.getNome().contains(nome));
//
//                for (Pessoa pessoa : pessoasNomes) {
//                    pessoa.mostrarDados();
//                }
//                break;
//            case 2:
//                System.out.println("2 - Digite o Documento:");
//                String documento = scanner.nextLine();
//
//                String documentoFormatado = documento.replaceAll("\\D", "");
//                List<Pessoa> pessoasDocumentos = pessoaRepository.buscar(p -> p.getDocumento().contains(documentoFormatado));
//
//                for (Pessoa pessoa : pessoasDocumentos) {
//                    pessoa.mostrarDados();
//                }
//        }
//    }
}

