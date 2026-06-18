package ui.produto;

import domain.categoria.Categoria;
import domain.produto.Produto;
import service.CategoriaService;
import service.ProdutoService;
import ui.categoria.CategoriaMenu;
import util.ConsoleUtils;

import java.math.BigDecimal;
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
            System.out.println("\n|*********** Produto ***********|");
            System.out.println("|1 - Cadastrar            - [] X|");
            System.out.println("|2 - Remover                    |");
            System.out.println("|3 - Atualizar                  |");
            System.out.println("|4 - Listar                     |");
            System.out.println("|0 - Sair                       |");
            System.out.println("|*******************************|");

            System.out.print("Opção: ");
            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");

            switch (opcao) {
                case 1:
                    cadastrar();
                    break;
                case 2:
//                    remover();
                    break;
                case 3:
//                    atualizar();
                    break;
                case 4:
                    int op;
                    do {
                        System.out.println("1 - Listar todas categoriras");
                        System.out.println("2 - Listar categora por descricao");
                        System.out.println("3 - Listar categora por id");

                        op = ConsoleUtils.lerInteiro(scanner, "Opção");

                    } while (op != 1 && op != 2 && op != 3);

//                    listar(op);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
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

//            System.out.print("Preço de Venda: ");
            precoVenda = ConsoleUtils.lerBigDecimal(scanner, "Preço de venda");

//            System.out.println("Custo de Venda: ");
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

    private Categoria selecionarCategoria() {
        while (true) {
            try {

                categoriaMenu.listar(1); //1 - Listar todos os produtos
                return categoriaService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
