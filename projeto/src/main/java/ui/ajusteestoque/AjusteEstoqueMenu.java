package ui.ajusteestoque;

import domain.produto.Produto;
import service.ProdutoService;
import ui.produto.ProdutoMenu;
import util.ConsoleUtils;

import java.util.Scanner;

public class AjusteEstoqueMenu {
    private final Scanner scanner;
    private final ProdutoService produtoService;
    private final ProdutoMenu produtoMenu;

    public AjusteEstoqueMenu(Scanner scanner, ProdutoMenu produtoMenu, ProdutoService produtoService) {
        this.scanner = scanner;
        this.produtoMenu = produtoMenu;
        this.produtoService = produtoService;
    }

    public void iniciar() {
        int opcao;

        do {
            System.out.println("\n|****** Ajuste  Estoque ******|");
            System.out.println("|1 - Novo               - [] X|");
            System.out.println("|2 - Remover                  |");
            System.out.println("|3 - Atualizar                |");
            System.out.println("|4 - Listar                   |");
            System.out.println("|0 - Voltar                   |");
            System.out.println("|*****************************|");

            System.out.print("Opção: ");
            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");

            switch (opcao) {
                case 1 -> novo();
//                case 2 -> remover();
//                case 3 -> atualizar();
                case 4 -> {
                    int op;
                    do {
                        System.out.println("1 - Listar todas categoriras");
                        System.out.println("2 - Listar categora por descricao");
                        System.out.println("3 - Listar categora por id");

                        op = ConsoleUtils.lerInteiro(scanner, "Opção");

                    } while (op != 1 && op != 2 && op != 3);

//                    listar(op);
                }
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
            }
        } while (opcao != 0);
    }

    private void novo() {
        produtoMenu.listar(4);
        System.out.print("Digite o ID do produto: ");
        Produto produto = produtoService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));
    }
}
