package ui.produto;

import domain.categoria.Categoria;
import domain.exception.produto.*;
import domain.produto.Produto;
import lombok.RequiredArgsConstructor;
import service.CategoriaService;
import service.ProdutoService;
import ui.categoria.CategoriaMenu;
import ui.grid.ProdutosGrid;
import util.console.ConsoleUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

            System.out.print("Opcao: ");
            opcao = ConsoleUtils.lerInteiro(scanner, "Opcao");

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
                        System.out.println("5 - Listar somente inativos");
                        System.out.println();
                        System.out.println("0 - Voltar");

                        System.out.print("Opcao: ");
                        op = ConsoleUtils.lerInteiro(scanner, "Opcao");

                    } while (op != 0 && op != 1 && op != 2 && op != 3 && op != 4 && op != 5);

                    if (op != 0) listar(op);
                }
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opcao invalida");
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
            descricao = scanner.nextLine().trim();
            if (descricao.isBlank()) throw new FalhaValorObrigatorioException("Descricao Obrigatoria.");

            precoVenda = ConsoleUtils.lerBigDecimal(scanner, "Preco de venda");
            if (precoVenda.compareTo(BigDecimal.ZERO) <= 0) throw new FalhaValorNaoPodeSerNegativoException("Preco de venda deve ser maior que zero");

            precoCusto = ConsoleUtils.lerBigDecimal(scanner, "Preco de custo");
            if (precoCusto.compareTo(BigDecimal.ZERO) < 0) throw new FalhaValorNaoPodeSerNegativoException("Preco de custo nao deve ser menor que zero");

            if (ConsoleUtils.confirmar(scanner, "Deseja cadastrar a categoria ?")) {
                categoria = selecionarCategoria();
            }

            var produto = Produto.builder().descricao(descricao).precoVenda(precoVenda)
                    .precoCusto(precoCusto).ativo(true).createdAt(LocalDateTime.now())
                    .categoria(categoria).build();

            var isInseriu = produtoService.inserirProduto(produto);

            if (isInseriu) {
                System.out.println("Produto " + produto.getDescricao() + " cadastrado com sucesso!");
            } else {
                System.out.println("Erro ao inserir Produto!");
            }
        } catch (IllegalArgumentException | FalhaAoInserirProdutoException | FalhaValorObrigatorioException |
                 FalhaValorNaoPodeSerNegativoException | FalhaValorJaRegistradoException e) {
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

        } catch (FalhaAoAtualizarProdutoException | FalhaProdutoNaoExisteException | FalhaProdutoEstaInativoException e) {
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

        } catch (FalhaAoAtualizarProdutoException | FalhaProdutoNaoExisteException | FalhaProdutoEstaAtivoException e) {
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

            while (true) {
                if (atualizarCategoria(produto)) break;
            }

            atualizarStatus(produto);

            if (produtoService.atualizarProduto(produto)) {
                System.out.println("Produto " + produto.getDescricao() + " atualizado com sucesso!");
            }

        } catch (FalhaAoAtualizarProdutoException |
                 FalhaProdutoNaoExisteException |
                 FalhaValorNaoPodeSerNegativoException e) {
            System.out.println(e.getMessage());
        }
    }

    private void atualizarDescricao(Produto produto) {

        System.out.println("Caso queira deixar o valor antigo, aperta apenas ENTER");
        System.out.println("Descricao Atual: " + produto.getDescricao());
        System.out.print("Nova Descricao: ");

        var descricao = scanner.nextLine().trim();
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
                System.out.print("Digite ID da Categoria: ");

                var categoria = categoriaService.buscarPorId(ConsoleUtils.lerLong(scanner, "ID"));

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
            System.out.print("Selecione um ID: ");
            var produto = produtoService.buscarPorId(ConsoleUtils.lerLong(scanner, "ID"));

            ProdutosGrid.exibirDetalhesProduto(produto);
        } catch (FalhaAoBuscarProdutoException | FalhaProdutoNaoExisteException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param idLista 1 Lista todos, 2 Lista por Descrição, 3 Lista por ID e 4 Lista todos ativos
     */
    public void listar(int idLista) {
        try {
            List<Produto> produtos = new ArrayList<>();

            switch (idLista) {
                case 1 -> produtos = produtoService.buscarTodos();
                case 2 -> {
                    System.out.print("Digite a descricao: ");
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
                default -> System.out.println("opcao invalida!");
            }
            ProdutosGrid.exibirGridProdutos(produtos);
        } catch (FalhaAoBuscarProdutoException | FalhaProdutoEstaInativoException | FalhaProdutoEstaAtivoException |
                FalhaProdutoNaoExisteException e) {
            System.out.println(e.getMessage());
        }
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
                return categoriaService.buscarPorId(ConsoleUtils.lerLong(scanner, "ID"));
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
