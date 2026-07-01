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
import java.time.format.DateTimeFormatter;
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
            System.out.println("|6 - excluir Movimento          |");
            System.out.println("|7 - Consultar                  |");
            System.out.println("|0 - Voltar                     |");
            System.out.println("|*******************************|");

            System.out.print("Opção: ");
            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");

            switch (opcao) {
                case 1:
                    novoMovimento();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 7:
                    consultarMovimento();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }

        } while (opcao != 0);
    }

    private void novoMovimento() {
        try {
            Pessoa cliente = selecionarCliente();

            Pessoa vendedor = selecionarVendedor();

            List<MovimentoItem> movimentoItens = new ArrayList<>();

            boolean repeticao = true;
            while (repeticao) {
                System.out.println("1 - Adicionar item");
                System.out.println("2 - remover item");
                System.out.println("3 - finalizar lançamento");
                System.out.print("Opção: ");

                switch (ConsoleUtils.lerInteiro(scanner, "Opção")) {
                    case 1 -> criarMovimentoItem(movimentoItens);
                    case 2 -> removerItem(movimentoItens);
                    case 3 -> {
                        if (concluirMovimento(cliente, vendedor, movimentoItens)) {
                            repeticao = false;
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void editarMovimento() {

    }

    private void consultarMovimento() {
        exibirGridMovimento(movimentoService.buscarTodos());
    }

    private void criarMovimentoItem(List<MovimentoItem> movimentoItens) {
        movimentoItens.add(criarMovimentoItem());
        exibirGridItens(movimentoItens);
    }

    private boolean concluirMovimento(Pessoa cliente, Pessoa vendedor, List<MovimentoItem> movimentoItens) {
        int opcao;
        do {
            System.out.println("1 - Salvar venda em aberto");
            System.out.println("2 - Finalizar venda");
            System.out.println("3 - Cancelar operação");

            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");
        } while (opcao != 1 && opcao != 2 && opcao != 3);

        if (movimentoItens.isEmpty()) {
            System.out.println("Adicione pelo menos um item antes de concluir a venda.");
            return false;
        }

        Movimento movimento = Movimento.builder()
                .pessoa(cliente)
                .funcionario(vendedor)
                .movimentoItens(movimentoItens)
                .build();
        if (opcao == 2) {
            movimento.setStatusMovimento(StatusMovimento.FINALIZADO);
        } else if (opcao == 3) {
            System.out.println("Venda cancelada!");
            return true;
        }

        movimentoService.inserirMovimento(movimento);
        System.out.println("Venda criada com sucesso!");
        return true;
    }

    private void removerItem(List<MovimentoItem> movimentoItens) {
        exibirGridItens(movimentoItens);
        System.out.print("Digite o ID do produto: ");
        int id = ConsoleUtils.lerInteiro(scanner, "ID");
        movimentoItens.removeIf(i -> i.getProduto().getId() == id);
    }

    private MovimentoItem criarMovimentoItem() {
        produtoMenu.listar(4);//todos ativos
        System.out.print("Digite o ID do produto: ");
        Produto produto = produtoService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));
        return MovimentoItem.builder()
                .produto(produto)
                .quantidade(ConsoleUtils.lerBigDecimal(scanner, "Quantidade"))
                .valorUnitario(produto.getPrecoVenda())
                .build();
    }

    private Pessoa selecionarVendedor() {
        pessoaMenu.exibirGrid(pessoaService.buscarPessoaPorPapelAtivo(PessoaPapel.FUNCIONARIO));
        System.out.print("Digite o ID do vendedor: ");
        return pessoaService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));
    }

    private Pessoa selecionarCliente() {
        pessoaMenu.exibirGrid(pessoaService.buscarPessoaPorPapelAtivo(PessoaPapel.CLIENTE));
        System.out.print("Digite o ID do cliente: ");
        return pessoaService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));
    }

    private void exibirCabecalhoGridMovimento() {
        System.out.printf(
                "%-3s | %-15s | %-15s | %-10s | %-16s | %-5s | %-10s%n",
                "ID", "CLIENTE", "VENDEDOR", "STATUS", "DATA", "ITENS", "VALOR"
        );
    }

    private void exibirGridMovimento(List<Movimento> movimentos) {
        String id;
        String nomeCliente;
        String nomeVendedor;
        String status;
        String dataMovimento;
        String qtdeItens;
        String valorTotal;

        if (movimentos.isEmpty()) {
            System.out.println("Nenhum movimento encontrado");
            return;
        }

        exibirCabecalhoGridMovimento();

        for (Movimento movimento : movimentos) {
            id = ConsoleUtils.formatarColuna(String.valueOf(movimento.getId()), 3);
            nomeCliente = ConsoleUtils.formatarColuna(movimento.getPessoa().getNome(), 15);
            nomeVendedor = ConsoleUtils.formatarColuna(movimento.getFuncionario().getNome(), 15);
            status = ConsoleUtils.formatarColuna(movimento.getStatusMovimento().getDescricao(), 10);
            dataMovimento = ConsoleUtils.formatarColuna(
                    movimento.getDataMovimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), 16);
            qtdeItens = ConsoleUtils.formatarColuna(String.valueOf(movimento.getQuantidadeTotal()), 5);
            valorTotal = ConsoleUtils.formatarColuna(ConsoleUtils.formatarMoeda(movimento.getValorTotal()), 10);

            System.out.printf("%s | %s | %s | %s | %s | %s | %s %n",
                    id, nomeCliente, nomeVendedor, status, dataMovimento, qtdeItens, valorTotal);
        }


    }

    private void exibirCabecalhoGridItens() {
        System.out.printf(
                "%-3s | %-15s | %-10s | %-11s | %-11s%n",
                "ID", "PRODUTO", "QTDE", "P.UNITÁRIO", "P.TOTAL"
        );
    }

    private void exibirGridItens(List<MovimentoItem> itens) {
        String id;
        String produto;
        String quantidade;
        String precoVenda;
        String precoTotal;

        if (itens.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }

        exibirCabecalhoGridItens();

        for (MovimentoItem i : itens) {
            id = ConsoleUtils.formatarColuna(String.valueOf(i.getProduto().getId()), 3);
            produto = ConsoleUtils.formatarColuna(String.valueOf(i.getProduto().getDescricao()), 15);
            quantidade = ConsoleUtils.formatarColuna(i.getQuantidade().toString(), 10);
            precoVenda = ConsoleUtils.formatarColuna(ConsoleUtils.formatarMoeda(i.getValorUnitario()), 11);
            precoTotal = ConsoleUtils.formatarColuna(ConsoleUtils.formatarMoeda(i.getValorTotal()), 11);


            System.out.printf("%s | %s | %s | %s | %s %n", id, produto, quantidade, precoVenda, precoTotal);

        }
        exibirGridRodapeItens(itens);
    }

    private void exibirGridRodapeItens(List<MovimentoItem> itens) {
        BigDecimal totalQuantidade = itens.stream().
                map(MovimentoItem::getQuantidade)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorTotal = itens.stream().
                map(MovimentoItem::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("------------------------------");
        System.out.printf("Quantidade : %s%n", totalQuantidade);
        System.out.printf("Valor Total: %s%n", valorTotal);
    }
}
