package ui.ajusteestoque;

import service.AjusteEstoqueService;
import service.EstoqueService;
import service.ProdutoService;
import util.console.ConsoleUtils;
import util.mensagens.MensagensGlobais;

import java.util.Scanner;

import static ui.grid.ProdutoEstoqueGrid.*;

public class AjusteEstoqueMenu {
    private final Scanner scanner;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;
    private final AjusteEstoqueService ajusteEstoqueService;

    public AjusteEstoqueMenu(
            Scanner scanner,
            ProdutoService produtoService,
            EstoqueService estoqueService,
            AjusteEstoqueService ajusteEstoqueService) {
        this.scanner = scanner;
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
        this.ajusteEstoqueService = ajusteEstoqueService;
    }

    public void iniciar() {
        int opcao;

        do {
            menu();

            System.out.print(MensagensGlobais.OPCAO);
            opcao = ConsoleUtils.lerInteiro(scanner, MensagensGlobais.OPCAO);

            switch (opcao) {
                case 1 -> ajusteEstoqueService.novoAjuste();
                case 2 -> ajusteEstoqueService.removerAjuste();
                case 3 -> ajusteEstoqueService.atualizar();
                case 4 -> ajusteEstoqueService.criarMovimento();
                case 5 -> {
                    int op;
                    do {
                        System.out.println("1 - Listar todos Ajustes de Estoque");
                        System.out.println("2 - Listar categora por descricao");
                        System.out.println("3 - Listar categora por id");

                        op = ConsoleUtils.lerInteiro(scanner, MensagensGlobais.OPCAO);

                        switch (op) {
                            case 1 -> ajusteEstoqueService.buscarTodosAjusteEstoque();
                            case 2 -> ajusteEstoqueService.buscarAjusteEstoquePorTitulo();

                        }

                    } while (op != 0);

                }
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
            }
        } while (opcao != 0);
    }
}