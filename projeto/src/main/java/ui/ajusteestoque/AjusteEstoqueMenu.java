package ui.ajusteestoque;

import lombok.RequiredArgsConstructor;
import service.AjusteEstoqueService;
import util.console.ConsoleUtils;
import util.mensagens.MensagensGlobais;

import java.util.Scanner;

import static ui.grid.ProdutoEstoqueGrid.*;

@RequiredArgsConstructor
public class AjusteEstoqueMenu {
    private final Scanner scanner;
    private final AjusteEstoqueService ajusteEstoqueService;

    public void iniciar() {
        int opcao;

        do {
            menu();

            System.out.print(MensagensGlobais.OPCAO);
            opcao = ConsoleUtils.lerInteiro(scanner, MensagensGlobais.OPCAO);

            switch (opcao) {
                case 1 -> ajusteEstoqueService.novoAjuste();
                case 2 -> ajusteEstoqueService.remover();
                case 3 -> ajusteEstoqueService.atualizarAjusteEstoque();
                case 4 -> ajusteEstoqueService.criarMovimento();
                case 5 -> {
                    int op;
                    do {
                        System.out.println("1 - Listar todos Ajustes de Estoque");
                        System.out.println("2 - Listar categora por descricao");
                        System.out.println("3 - Listar Itens do Ajuste de Estoque");

                        op = ConsoleUtils.lerInteiro(scanner, MensagensGlobais.OPCAO);

                        switch (op) {
                            case 1 -> ajusteEstoqueService.buscarTodosAjusteEstoque();
                            case 2 -> ajusteEstoqueService.buscarAjusteEstoquePorTitulo();
                            case 3 -> ajusteEstoqueService.buscarAjusteEstoqueItens();
                        }
                    } while (op != 0);
                }
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
            }
        } while (opcao != 0);
    }
}