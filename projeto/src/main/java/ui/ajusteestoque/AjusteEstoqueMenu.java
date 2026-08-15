package ui.ajusteestoque;

import domain.ajusteestoque.AjusteEstoque;
import domain.ajusteestoque.AjusteEstoqueItens;
import domain.ajusteestoque.Status;
import domain.movimento.Movimento;
import domain.movimento.MovimentoItem;
import domain.movimento.StatusMovimento;
import domain.movimento.Tipo;
import domain.pessoa.Pessoa;
import domain.produto.Produto;
import dto.ProdutoEstoqueDTO;
import service.AjusteEstoqueService;
import service.EstoqueService;
import service.ProdutoService;
import ui.produto.ProdutoMenu;
import util.ConsoleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ui.grid.ProdutoEstoqueGrid.exibirGridItens;
import static ui.grid.ProdutoEstoqueGrid.exibirtGirdProdutoEstoque;

public class AjusteEstoqueMenu {
    private final Scanner scanner;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;
    private final ProdutoMenu produtoMenu;
    private final AjusteEstoqueService ajusteEstoqueService;

    public AjusteEstoqueMenu(
            Scanner scanner,
            ProdutoService produtoService,
            EstoqueService estoqueService,
            AjusteEstoqueService ajusteEstoqueService,
            ProdutoMenu produtoMenu) {
        this.scanner = scanner;
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
        this.ajusteEstoqueService = ajusteEstoqueService;
        this.produtoMenu = produtoMenu;
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
//                case 2 -> removerItem(movimentoItens);
                case 3 -> {
                    if (concluirAjuste(titulo, itens)) {
                        repeticao = false;
                    }
                }
            }
        }
    }

    private void criarAjusteEstoqueItem(List<AjusteEstoqueItens> itens) {
        itens.add(criarAjusteEstoqueItem());
        exibirGridItens(itens);
    }

    private AjusteEstoqueItens criarAjusteEstoqueItem() {
        exibirtGirdProdutoEstoque(estoqueService.buscarProdutosEstoque());

        System.out.print("Digite o ID do produto: ");
        Produto produto = produtoService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));

        return AjusteEstoqueItens.builder()
                .produto(produto)
                .estoque(estoqueService.buscarPorIdProduto(produto.getId()))
                .contagem(ConsoleUtils.lerBigDecimal(scanner, "Contagem"))
                .build();
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
}
