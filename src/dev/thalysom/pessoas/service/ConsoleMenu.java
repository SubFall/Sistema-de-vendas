package dev.thalysom.pessoas.service;

import dev.thalysom.pessoas.domain.Pessoa;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    public Scanner scanner = new Scanner(System.in);
    public CadastroPessoa cadastroPessoa = new CadastroPessoa();
    public PessoaService pessoaService = new PessoaService();

    public void iniciar() {
        int opcao;
        do {
            mostrarMenu();
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    pessoaService.cadastrarPessoa(scanner, cadastroPessoa);
                    break;
                case 2:
                    cadastroPessoa.listar();
                    break;
                case 3:
                    pessoaService.removerPessoa(scanner, cadastroPessoa);
                    break;
                case 4:
                    mostrarFiltro();
                    break;
                case 5:
                    System.out.println("Sainda do programa");
                    break;
                default:
                    System.out.println("Opção inválida");
            }

        } while (opcao != 5);
    }

    private static void mostrarMenu() {
        System.out.println("####### Lua.Net #######");
        System.out.println("=======================");
        System.out.println("|       Pessoas        |");
        System.out.println("=======================");
        System.out.println("1 - Cadastrar");
        System.out.println("2 - Listar");
        System.out.println("3 - Remover");
        System.out.println("4 - Filtrar");
        System.out.println("5 - Sair");
    }

    private void mostrarFiltro() {
        int opcao;
        System.out.println("1 - Filtrar por Nome:");
        System.out.println("2 - Filtrar por Documento:");
        opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
                System.out.println("2 - Digite o Nome:");
                String nome = scanner.nextLine();
                List<Pessoa> pessoasNomes = cadastroPessoa.buscar(p -> p.getNome().contains(nome));

                for (Pessoa pessoa : pessoasNomes) {
                    pessoa.mostrarDados();
                }
                break;
            case 2:
                System.out.println("2 - Digite o Documento:");
                String documento = scanner.nextLine();

                String documentoFormatado = documento.replaceAll("\\D", "");
                List<Pessoa> pessoasDocumentos = cadastroPessoa.buscar(p -> p.getDocumento().contains(documentoFormatado));

                for (Pessoa pessoa : pessoasDocumentos) {
                    pessoa.mostrarDados();
                }
        }
    }


}

