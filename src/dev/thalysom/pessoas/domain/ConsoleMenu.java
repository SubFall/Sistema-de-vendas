package dev.thalysom.pessoas.domain;

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
                    System.out.println("Sainda do programa");
                    break;
                default:
                    System.out.println("Opção inválida");
            }

        } while (opcao != 4);
    }

    private static void mostrarMenu() {
        System.out.println("####### Lua.Net #######");
        System.out.println("=======================");
        System.out.println("|       Pessoas        |");
        System.out.println("=======================");
        System.out.println("1 - Cadastrar");
        System.out.println("2 - Listar");
        System.out.println("3 - Remover");
        System.out.println("4 - Sair");
    }


}

