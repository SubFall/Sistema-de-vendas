package test;

import conn.ConnectionFactory;
import conn.ConnectionProvider;
import repository.AjusteEstoqueItemRepository;
import repository.AjusteEstoqueRepository;
import service.*;
import ui.ajusteestoque.AjusteEstoqueMenu;
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
    private final EstoqueService estoqueService = new EstoqueService();

    private final ConnectionFactory connectionFactory = new ConnectionFactory();
    private final AjusteEstoqueRepository estoqueRepository = new AjusteEstoqueRepository();
    private final AjusteEstoqueItemRepository ajusteEstoqueItemRepository = new AjusteEstoqueItemRepository();

    private final AjusteEstoqueService ajusteEstoqueService =
            new AjusteEstoqueService(estoqueRepository, ajusteEstoqueItemRepository, connectionFactory);

    private final PessoaMenu pessoaMenu = new PessoaMenu(scanner, pessoaService);
    private final CategoriaMenu categoriaMenu = new CategoriaMenu(scanner, categoriaService);
    private final ProdutoMenu produtoMenu = new ProdutoMenu(scanner, produtoService, categoriaService, categoriaMenu);
    private final AjusteEstoqueMenu ajusteEstoqueMenu =
            new AjusteEstoqueMenu(
                    scanner,
                    produtoService,
                    estoqueService,
                    ajusteEstoqueService,
                    produtoMenu
            );
    private final MovimentoMenu movimentoMenu =
            new MovimentoMenu(
                    scanner,
                    movimentoService,
                    pessoaService,
                    produtoService,
                    pessoaMenu,
                    produtoMenu
            );

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
            System.out.println("|5 - Ajuste Estoque           |");
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
                case 5 -> ajusteEstoqueMenu.iniciar();
                case 0 -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida");
            }
        }
    }
}