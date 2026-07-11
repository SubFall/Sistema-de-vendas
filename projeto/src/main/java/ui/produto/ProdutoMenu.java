package ui.produto;

import domain.categoria.Categoria;
import domain.produto.Produto;
import service.CategoriaService;
import service.ProdutoService;
import ui.categoria.CategoriaMenu;
import util.ConsoleUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProdutoMenu {
    private Scanner scanner = new Scanner(System.in);
    private ProdutoService produtoService;
    private CategoriaService categoriaService;
    private CategoriaMenu categoriaMenu;

    public ProdutoMenu(ProdutoService produtoService, CategoriaService categoriaService, CategoriaMenu categoriaMenu) {
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;
        this.categoriaMenu = categoriaMenu;
    }


    public void iniciar() {
        int opcao;

        do {
            System.out.println("\n|********** Produto **********|");
            System.out.println("|1 - Cadastrar          - [] X|");
            System.out.println("|2 - Inativar                 |");
            System.out.println("|3 - Atualizar                |");
            System.out.println("|4 - Detalhes                 |");
            System.out.println("|5 - Listar                   |");
            System.out.println("|0 - Voltar                   |");
            System.out.println("|*****************************|");

            System.out.print("Opção: ");
            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> inativar();
                case 3 -> atualizar();
                case 4 -> detalhes();
                case 5 -> {
                    int op;
                    do {
                        System.out.println("1 - Listar todos produtos");
                        System.out.println("2 - Listar produtos por descricao");
                        System.out.println("3 - Listar produto por id");
                        System.out.println("4 - Listar somente ativos");

                        op = ConsoleUtils.lerInteiro(scanner, "Opção");

                    } while (op != 1 && op != 2 && op != 3 && op != 4);

                    listar(op);
                }
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
            }
        } while (opcao != 0);
    }

    private void cadastrar() {
        String descricao;
        BigDecimal precoVenda;
        BigDecimal precoCusto;
        Categoria categoria = null;
        try {
            System.out.print("Descricao: ");
            descricao = scanner.nextLine();

            precoVenda = ConsoleUtils.lerBigDecimal(scanner, "Preço de venda");

            precoCusto = ConsoleUtils.lerBigDecimal(scanner, "Preço de custo");

            int op;
            do {
                System.out.println("Deseja cadastrar a categoria ?");
                System.out.println("1 - SIM");
                System.out.println("2 - Não");
                op = ConsoleUtils.lerInteiro(scanner, "Opção");
            } while (op != 1 && op != 2);

            if (op == 1) {
                categoria = selecionarCategoria();
            }

            Produto produto = Produto.builder()
                    .descricao(descricao)
                    .precoVenda(precoVenda)
                    .precoCusto(precoCusto)
                    .categoria(categoria)
                    .build();

            produtoService.inserirProduto(produto);

            System.out.println("Produto " + produto.getDescricao() + " cadastrado com sucesso!");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void inativar() {
        listar(4); //somente produtos ativos
        try {
            System.out.print("Digite o ID do produto para inativar:");
            Produto produto = produtoService.buscarPorIdAtivo(ConsoleUtils.lerInteiro(scanner, "ID"));

            if (!ConsoleUtils.confirmar(scanner, "Tem certeza que deseja inativar " + produto.getDescricao() + " ?")) {
                return;
            }
            boolean atualizado = produtoService.atualizarStatusProduto(produto.getId());

            if (atualizado) {
                System.out.println("Produto " + produto.getDescricao() + " inativado com sucesso!");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void atualizar() {
        listar(1);

        try {
            System.out.println("Selecione um ID para atualizar");
            Produto produto = produtoService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));

            atualizarDescricao(produto);
            atualizarPreco(produto);
            atualizarCusto(produto);
            atualizarCategoria(produto);
            atualizarStatus(produto);

            if (produtoService.atualizarProduto(produto)) {
                System.out.println("Produto " + produto.getDescricao() + " atualizado com sucesso!");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void atualizarDescricao(Produto produto) {
        System.out.println("Caso queira deixar o valor antigo, aperta apenas ENTER");
        System.out.println("Descricao Atual: " + produto.getDescricao());
        System.out.println("Nova Descricao:");

        String descricao = scanner.nextLine();
        produto.setDescricao(descricao.isBlank() ? produto.getDescricao() : descricao);
    }

    private void atualizarPreco(Produto produto) {
        System.out.println("Preco Atual: " + produto.getPrecoVenda());
        BigDecimal precoVenda = ConsoleUtils.lerDecimal(scanner, "Novo valor");

        if (precoVenda != null) {
            produto.setPrecoVenda(precoVenda);
        }
    }

    private void atualizarCusto(Produto produto) {
        System.out.println("Custo Atual: " + produto.getPrecoCusto());
        BigDecimal precoCusto = ConsoleUtils.lerDecimal(scanner, "Novo valor");

        if (precoCusto != null) {
            produto.setPrecoCusto(precoCusto);
        }
    }

    private void atualizarCategoria(Produto produto) {
        System.out.println("Categoria Atual: " + produto.getCategoria().getDescricao());

        if (ConsoleUtils.confirmar(scanner, "Deseja atualizar a Categoria ?")) {
            categoriaMenu.listar(1);
            Categoria categoria = categoriaService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));
            produto.setCategoria(categoria);
        }
    }

    private void atualizarStatus(Produto produto) {
        int op;
        do {
            System.out.println("Status Atual: " + (produto.isAtivo() ? "Ativo" : "Inativo"));
            System.out.println("\n1 - Manter Ativo");
            System.out.println("2 - Alterar para Inativo");
            System.out.println("3 - Não alterar");

            op = ConsoleUtils.lerInteiro(scanner, "Opção");
        } while (op != 1 && op != 2 && op != 3);

        if (op == 1) {
            produto.setAtivo(true);
        } else if (op == 2) {
            produto.setAtivo(false);
        }
    }

    public void detalhes() {
        listar(1);

        try {
            System.out.println("Selecione um ID");
            Produto produto = produtoService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));
            System.out.println("ID: " + produto.getId());
            System.out.println("Descricao: " + produto.getDescricao());
            System.out.println("Preco venda: " + produto.getPrecoVenda());
            System.out.println("Preco custo: " + produto.getPrecoCusto());
            System.out.println("Categoria: " + produto.getCategoria().getDescricao());
            System.out.println("Ativo: " + (produto.isAtivo() ? "SIM" : "NÃO"));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void listar(int idLista) {
        List<Produto> produtos = new ArrayList<>();
        switch (idLista) {
            case 1 -> produtos = produtoService.buscarTodos();
            case 2 -> {
                System.out.print("Digite a descrição: ");
                produtos = produtoService.buscarPorDescricao(scanner.nextLine());
            }
            case 3 -> {
                System.out.print("Digite o ID: ");
                try {
                    produtos.add(produtoService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID")));
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 4 -> produtos = produtoService.buscarTodosAtivo();
            default -> System.out.println("opção inválida!");
        }
        exibirGrid(produtos);
    }

    private Categoria selecionarCategoria() {
        while (true) {
            try {

                categoriaMenu.listar(1); //1 - Listar todas as categorias
                return categoriaService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void exibirCabecalhoGrid() {
        System.out.printf(
                "%-5s | %-20s | %-10s | %-10s | %-10s%n",
                "ID", "DESCRICAO", "PRECO VENDA", "PRECO CUSTO", "ATIVO"
        );
    }

    private void exibirGrid(List<Produto> produtos) {
        String id;
        String descricao;
        String precoVenda;
        String precoCusto;
        String ativo;

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }

        exibirCabecalhoGrid();

        for (Produto p : produtos) {
            id = ConsoleUtils.formatarColuna(String.valueOf(p.getId()), 5);
            descricao = ConsoleUtils.formatarColuna(p.getDescricao(), 20);
            precoVenda = ConsoleUtils.formatarColuna(ConsoleUtils.formatarMoeda(p.getPrecoVenda()), 11);
            precoCusto = ConsoleUtils.formatarColuna(ConsoleUtils.formatarMoeda(p.getPrecoCusto()), 11);
            ativo = p.isAtivo() ? "SIM" : "NÃO";

            System.out.printf("%s | %s | %s | %s | %s %n", id, descricao, precoVenda, precoCusto, ativo);
        }
    }
}
