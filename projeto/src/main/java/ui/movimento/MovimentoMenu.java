package ui.movimento;

import domain.movimento.Movimento;
import domain.movimento.MovimentoItem;
import domain.movimento.StatusMovimento;
import domain.pessoa.Pessoa;
import domain.pessoa.PessoaPapel;
import domain.produto.Produto;
import service.CategoriaService;
import service.MovimentoService;
import service.PessoaService;
import service.ProdutoService;
import ui.categoria.CategoriaMenu;
import ui.pessoa.PessoaMenu;
import ui.produto.ProdutoMenu;
import util.ConsoleUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovimentoMenu {
    private Scanner scanner = new Scanner(System.in);
    private PessoaService pessoaService = new PessoaService();
    private PessoaMenu pessoaMenu = new PessoaMenu(pessoaService);
    private CategoriaService categoriaService = new CategoriaService();
    private CategoriaMenu categoriaMenu = new CategoriaMenu(categoriaService);
    private ProdutoService produtoService = new ProdutoService();
    private ProdutoMenu produtoMenu = new ProdutoMenu(produtoService, categoriaService, categoriaMenu);
    private MovimentoService movimentoService;

    public MovimentoMenu(MovimentoService movimentoService) {
        this.movimentoService = movimentoService;
    }

    public void iniciar() {
        int opcao;

        do {
            System.out.println("\n|********** Movimento **********|");
            System.out.println("|1 - Nova Venda           - [] X|");
            System.out.println("|2 - Editar Movimento           |");
            System.out.println("|3 - Finalizar Movimento        |");
            System.out.println("|4 - Estornar Movimento         |");
            System.out.println("|5 - Cancelar Movimento         |");
            System.out.println("|6 - Cancelar Movimento         |");
            System.out.println("|7 - Consultar                  |");
            System.out.println("|0 - Voltar                     |");
            System.out.println("|*******************************|");

            System.out.print("Opção: ");
            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");

            switch (opcao) {
                case 1:
                    novaVenda();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }

        } while (opcao != 0);
    }

    private void novaVenda() {
        try {
            Pessoa cliente = getCliente();

            Pessoa vendedor = getVendedor();

            List<MovimentoItem> movimentoItens = new ArrayList<>();

            boolean repeticao = true;
            while (repeticao) {
                System.out.println("1 - Adicionar item");
                System.out.println("2 - remover item");
                System.out.println("3 - finalizar lançamento");
                System.out.print("Opção: ");
                int op = ConsoleUtils.lerInteiro(scanner, "Opção");

                switch (op) {
                    case 1:
                        produtoMenu.listar(4);//todos ativos
                        System.out.print("Digite o ID do produto: ");
                        Produto produto = produtoService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));
                        MovimentoItem item = MovimentoItem.builder()
                                .produto(produto)
                                .quantidade(ConsoleUtils.lerBigDecimal(scanner, "Quantidade"))
                                .valorUnitario(produto.getPrecoVenda())
                                .build();

                        movimentoItens.add(item);
                        exibirGrid(movimentoItens);
                        break;
                    case 2:
                        exibirGrid(movimentoItens);
                        System.out.print("Digite o ID do produto: ");
                        int id = ConsoleUtils.lerInteiro(scanner, "ID");
                        movimentoItens.removeIf(i -> i.getProduto().getId() == id);
                        break;
                    case 3:
                        System.out.println("Deseja finalizar a venda ou deixar com status aberto ?");
                        int opcao;
                        do {
                            System.out.println("1 - Finalizar");
                            System.out.println("2 - Aberto");

                            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");
                        } while (opcao != 1 && opcao != 2);

                        Movimento movimento = Movimento.builder()
                                .pessoa(cliente)
                                .funcionario(vendedor)
                                .movimentoItens(movimentoItens)
                                .build();
                        if (opcao == 1) {
                            movimento.setStatusMovimento(StatusMovimento.FINALIZADO);
                        }

                        movimentoService.inserirMovimento(movimento);
                        System.out.println("Venda criada com sucesso!");
                        repeticao = false;
                        break;
                }

            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }

    private Pessoa getVendedor() {
        pessoaMenu.exibirGrid(pessoaService.buscarPessoaPorPapelAtivo(PessoaPapel.FUNCIONARIO));
        System.out.print("Digite o ID do vendedor: ");
        Pessoa vendedor = pessoaService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));
        return vendedor;
    }

    private Pessoa getCliente() {
        pessoaMenu.exibirGrid(pessoaService.buscarPessoaPorPapelAtivo(PessoaPapel.CLIENTE));
        System.out.print("Digite o ID do cliente: ");
        Pessoa cliente = pessoaService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));
        return cliente;
    }

    private void exibirCabecalhoGrid() {
        System.out.printf(
                "%-3s | %-15s | %-10s | %-11s | %-11s%n",
                "ID", "PRODUTO", "QTDE", "P.UNITÁRIO", "P.TOTAL"
        );
    }

    private void exibirGrid(List<MovimentoItem> itens) {
        String id;
        String produto;
        String quantidade;
        String precoVenda;
        String precoTotal;

        if (itens.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }

        exibirCabecalhoGrid();

        for (MovimentoItem i : itens) {
            id = ConsoleUtils.formatarColuna(String.valueOf(i.getProduto().getId()), 3);
            produto = ConsoleUtils.formatarColuna(String.valueOf(i.getProduto().getDescricao()), 15);
            quantidade = ConsoleUtils.formatarColuna(i.getQuantidade().toString(), 10);
            precoVenda = ConsoleUtils.formatarColuna(String.format("%.2f", i.getValorUnitario()), 11);
            precoTotal = ConsoleUtils.formatarColuna(String.format("%.2f", i.getValorTotal()), 11);


            System.out.printf("%s | %s | %s | %s | %s %n", id, produto, quantidade, precoVenda, precoTotal);

        }
        exibirGridRodape(itens);
    }

    private void exibirGridRodape(List<MovimentoItem> itens) {
        BigDecimal totalQuantidade = itens.stream().
                map(MovimentoItem::getQuantidade)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorTotal = itens.stream().
                map(MovimentoItem::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.printf("Itens: %s%n", totalQuantidade);
        System.out.printf("Total: %s%n", valorTotal);
    }
}
