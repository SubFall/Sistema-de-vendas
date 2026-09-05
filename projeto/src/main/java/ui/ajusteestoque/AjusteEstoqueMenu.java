package ui.ajusteestoque;

import domain.ajusteestoque.AjusteEstoque;
import domain.ajusteestoque.AjusteEstoqueItens;
import domain.ajusteestoque.Status;
import domain.produto.Produto;
import service.AjusteEstoqueService;
import service.EstoqueService;
import service.ProdutoService;
import util.ConsoleUtils;

import java.util.ArrayList;
import java.util.List;
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

            System.out.print("Opção: ");
            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");

            switch (opcao) {
                case 1 -> novoAjuste();
                case 2 -> removerAjuste();
//                case 3 -> atualizar();
                case 4 -> criarMovimento();
                case 5 -> {
                    int op;
                    do {
                        System.out.println("1 - Listar todas categoriras");
                        System.out.println("2 - Listar categora por descricao");
                        System.out.println("3 - Listar categora por id");

                        op = ConsoleUtils.lerInteiro(scanner, "Opção");

                    } while (op != 0);

//                    listar(op);
                }
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
            }
        } while (opcao != 0);
    }

    private void novoAjuste() {

        System.out.println("Título:");
        String titulo = scanner.nextLine();

        List<AjusteEstoqueItens> itens = new ArrayList<>();

        boolean repeticao = true;
        while (repeticao) {
            System.out.println("1 - Adicionar item");
            System.out.println("2 - remover item");
            System.out.println("3 - finalizar ajuste");
            System.out.print("Opção: ");

            switch (ConsoleUtils.lerInteiro(scanner, "Opção")) {
                case 1 -> criarAjusteEstoqueItem(itens);
                case 2 -> removerAjusteEstoqueItem(itens);
                case 3 -> {
                    if (concluirAjuste(titulo, itens)) {
                        repeticao = false;
                    }
                }
            }
        }
    }

    private void criarAjusteEstoqueItem(List<AjusteEstoqueItens> ajusteEstoqueItens) {
        exibirtGirdProdutoEstoque(estoqueService.buscarProdutosEstoque());

        try {
            System.out.print("Digite o ID do produto: ");
            Produto produto = produtoService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));

            AjusteEstoqueItens item = AjusteEstoqueItens.builder()
                    .produto(produto)
                    .estoque(estoqueService.buscarPorIdProduto(produto.getId()))
                    .contagem(ConsoleUtils.lerBigDecimal(scanner, "Contagem"))
                    .build();

            ajusteEstoqueItens.add(item);
            exibirGridItens(ajusteEstoqueItens);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removerAjusteEstoqueItem(List<AjusteEstoqueItens> ajusteEstoqueItens) {
        exibirGridItens(ajusteEstoqueItens);

        System.out.print("Digite o ID do produto: ");
        AjusteEstoqueItens estoqueItem = ajusteEstoqueService.buscarAjusteEstoqueItemPorId(ConsoleUtils.lerLong(scanner, "ID"));

        ajusteEstoqueItens.remove(estoqueItem);

        System.out.println("Item removido");
        exibirGridItens(ajusteEstoqueItens);
    }

    private boolean concluirAjuste(String titulo, List<AjusteEstoqueItens> itens) {
        int opcao;
        do {
            System.out.println("1 - Salvar ajuste em aberto");
            System.out.println("2 - Salvar ajuste finalizado");
            System.out.println("3 - Cancelar operação");

            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");
        } while (opcao != 1 && opcao != 2 && opcao != 3);

        if (itens.isEmpty()) {
            System.out.println("Adicione pelo menos um item antes de concluir a venda.");
            return false;
        }

        AjusteEstoque ajuste = AjusteEstoque.builder()
                .titulo(titulo)
                .status(Status.ABERTO)
                .ajusteEstoqueItens(itens)
                .build();

        if (opcao == 2) {
            ajuste.setStatus(Status.FINALIZADO);
        } else if (opcao == 3) {
            System.out.println("Ajuste cancelado!");
            return true;
        }

        ajusteEstoqueService.inserirAjusteEstoque(ajuste);
        System.out.println("Ajuste criado com sucesso!");
        return true;
    }

    private void criarMovimento() {
        List<AjusteEstoque> ajusteEstoqueList = ajusteEstoqueService.buscarAjustePorStatus(Status.FINALIZADO);
        exibirGridAjusteEstoque(ajusteEstoqueList);

        if (ajusteEstoqueList.isEmpty()) {
            return;
        }

        try {
            System.out.print("Digite o ID do Ajuste: ");
            AjusteEstoque ajuste = ajusteEstoqueService.buscarAjustePorId(ConsoleUtils.lerInteiro(scanner, "ID"));

            ajusteEstoqueService.criarMovimentoAjusteEstoque(ajuste.getId());

            System.out.println("Movimento criado com Sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removerAjuste() {
        List<AjusteEstoque> ajusteEstoqueList = ajusteEstoqueService.buscarTodosAjuste();
        exibirGridAjusteEstoque(ajusteEstoqueList);

        if (ajusteEstoqueList.isEmpty()) {
            return;
        }

        try {
            System.out.print("Digite o ID do Ajuste: ");
            AjusteEstoque ajusteEstoque = ajusteEstoqueService.buscarAjustePorId(ConsoleUtils.lerInteiro(scanner, "ID"));

            ajusteEstoqueService.removerAjusteEstoque(ajusteEstoque);

            System.out.println("Ajuste Estoque removido com Sucesso!");
        } catch (IllegalAccessError e) {
            System.out.println(e.getMessage());
        }
    }

}
