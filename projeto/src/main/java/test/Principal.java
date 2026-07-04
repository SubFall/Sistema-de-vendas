package test;

import service.CategoriaService;
import service.MovimentoService;
import service.PessoaService;
import service.ProdutoService;
import ui.categoria.CategoriaMenu;
import ui.movimento.MovimentoMenu;
import ui.pessoa.PessoaMenu;
import ui.produto.ProdutoMenu;
import util.ConsoleUtils;

import java.util.Scanner;


public class Principal {
    private final Scanner scanner = new Scanner(System.in);
    private final PessoaService pessoaService = new PessoaService();
    private final CategoriaService categoriaService = new CategoriaService();
    private final ProdutoService produtoService = new ProdutoService();
    private final MovimentoService movimentoService = new MovimentoService();

    private final PessoaMenu pessoaMenu = new PessoaMenu(pessoaService);
    private final CategoriaMenu categoriaMenu = new CategoriaMenu(categoriaService);
    private final ProdutoMenu produtoMenu = new ProdutoMenu(produtoService, categoriaService, categoriaMenu);
    private final MovimentoMenu movimentoMenu = new MovimentoMenu(movimentoService);

    static void main(String[] args) {
        Principal principal = new Principal();
        principal.iniciar();
    }

    public void iniciar() {
        while (true) {
            System.out.println("\n|** Bem vindo - Eclipse.NET **|");
            System.out.println("|1 - Pessoa             - [] X|");
            System.out.println("|2 - Produto                  |");
            System.out.println("|3 - Categoria                |");
            System.out.println("|4 - Movimento                |");
            System.out.println("|0 - Sair                     |");
            System.out.println("|*****************************|");
            System.out.print("Opção: ");

            int opcao;
            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");

            switch (opcao) {
                case 1 -> pessoaMenu.iniciar();
                case 2 -> produtoMenu.iniciar();
                case 3 -> categoriaMenu.iniciar();
                case 4 -> movimentoMenu.iniciar();
                case 0 -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida");
            }
        }
    }
}