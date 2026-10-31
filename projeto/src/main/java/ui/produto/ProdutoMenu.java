package ui.produto;

import domain.categoria.Categoria;
import domain.produto.Produto;
import lombok.RequiredArgsConstructor;
import service.CategoriaService;
import service.ProdutoService;
import ui.categoria.CategoriaMenu;
import ui.grid.ProdutosGrid;
import util.console.ConsoleUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class ProdutoMenu {
    private final Scanner scanner;
    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;
    private final CategoriaMenu categoriaMenu;

    public void iniciar() {
        int opcao;

        do {
            ProdutosGrid.menu();

            System.out.print("Opção: ");
            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> inativar();
                case 3 -> ativar();
                case 4 -> atualizar();
                case 5 -> detalhes();
                case 6 -> {
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

            if (ConsoleUtils.confirmar(scanner, "Deseja cadastrar a categoria ?")) {
                categoria = selecionarCategoria();
            }

            var produto = Produto.builder()
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
            System.out.print("Digite o ID do produto para inativar: ");
            var produto = produtoService.buscarPorIdAtivo(ConsoleUtils.lerLong(scanner, "ID"));

            if (!ConsoleUtils.confirmar(scanner, "Tem certeza que deseja inativar " + produto.getDescricao() + " ?")) {
                return;
            }

            produto.inativar();

            var isAtualizado = produtoService.atualizarStatusProduto(produto);

            if (isAtualizado) {
                System.out.println("Produto " + produto.getDescricao() + " inativado com sucesso!");
            }

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void ativar() {
        listar(5); //somente produtos inativos
        try {
            System.out.print("Digite o ID do produto para ativar: ");
            var produto = produtoService.buscarPorIdInativo(ConsoleUtils.lerLong(scanner, "ID"));

            if (!ConsoleUtils.confirmar(scanner, "Tem certeza que deseja ativar " + produto.getDescricao() + " ?")) {
                return;
            }

            produto.ativar();

            var isAtualizado = produtoService.atualizarStatusProduto(produto);

            if (isAtualizado) {
                System.out.println("Produto " + produto.getDescricao() + " ativado com sucesso!");
            }

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void atualizar() {
        listar(1); //busca todos os produtos

        try {
            System.out.print("Selecione um ID para atualizar: ");
            var produto = produtoService.buscarPorId(ConsoleUtils.lerLong(scanner, "ID"));

            atualizarDescricao(produto);

            atualizarPreco(produto);

            atualizarCusto(produto);

            while(true){
                if (atualizarCategoria(produto)) break;
            }

            atualizarStatus(produto);

            if (produtoService.atualizarProduto(produto)) {
                System.out.println("Produto " + produto.getDescricao() + " atualizado com sucesso!");
            }

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void atualizarDescricao(Produto produto) {

        System.out.println("Caso queira deixar o valor antigo, aperta apenas ENTER");
        System.out.println("Descricao Atual: " + produto.getDescricao());
        System.out.print("Nova Descricao: ");

        var descricao = scanner.nextLine();
        produto.setDescricao(descricao.isBlank() ? produto.getDescricao() : descricao);
    }

    private void atualizarPreco(Produto produto) {

        System.out.println("Preco Atual: " + produto.getPrecoVenda());

        var precoVenda = ConsoleUtils.lerDecimal(scanner, "Novo valor");

        if (precoVenda != null) {
            produto.setPrecoVenda(precoVenda);
        }
    }

    private void atualizarCusto(Produto produto) {

        System.out.println("Custo Atual: " + produto.getPrecoCusto());

        var precoCusto = ConsoleUtils.lerDecimal(scanner, "Novo valor");

        if (precoCusto != null) {
            produto.setPrecoCusto(precoCusto);
        }
    }

    private boolean atualizarCategoria(Produto produto) {

        try {
            System.out.println("Categoria Atual: " + produto.getCategoria().getDescricao());

            if (ConsoleUtils.confirmar(scanner, "Deseja atualizar a Categoria ?")) {
                categoriaMenu.listar(1);

                var categoria = categoriaService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));

                produto.setCategoria(categoria);
            }

            return true;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    private void atualizarStatus(Produto produto) {
        int op;

        System.out.println("Status Atual: " + (produto.isAtivo() ? "Ativo" : "Inativo"));

        if (ConsoleUtils.confirmar(scanner, "Deseja atualizar o Status ?")) {
            do {
                System.out.println("1 - Ativo");
                System.out.println("2 - Inativo");
                System.out.println("3 - Cancelar");

                op = ConsoleUtils.lerInteiro(scanner, "Opcao");
            } while (op != 1 && op != 2 && op != 3);

            if (op == 1) {
                produto.ativar();
            } else if (op == 2) {
                produto.inativar();
            }
        }

    }

    public void detalhes() {
        listar(1);

        try {
            System.out.println("Selecione um ID");
            Produto produto = produtoService.buscarPorId(ConsoleUtils.lerLong(scanner, "ID"));
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

    /**
     * @param idLista 1 Lista todos, 2 Lista por Descrição, 3 Lista por ID e 4 Lista todos ativos
     */
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
                    produtos.add(produtoService.buscarPorId(ConsoleUtils.lerLong(scanner, "ID")));
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 4 -> produtos = produtoService.buscarTodosAtivos();
            case 5 -> produtos = produtoService.buscarTodosInativos();
            default -> System.out.println("opção inválida!");
        }
        ProdutosGrid.exibirGridProdutos(produtos);
    }

    private Categoria selecionarCategoria() {
        int contador = 0;
        while (true) {
            try {
                if (contador > 0) {
                    if (!ConsoleUtils.confirmar(scanner, "Deseja cadastrar a categoria ?")) {
                        return null;
                    }
                }
                contador++;
                categoriaMenu.listar(1); //1 - Listar todas as categorias

                return categoriaService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
