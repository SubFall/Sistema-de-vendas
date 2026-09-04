package test;

import conn.ConnectionFactory;
import repository.*;
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

    private final ConnectionFactory connectionFactory = new ConnectionFactory();
    private final CategoriaRepository categoriaRepository = new CategoriaRepository(connectionFactory);
    private final ProdutoRepository produtoRepository = new ProdutoRepository(connectionFactory);
    private final PessoaPapelRepository pessoaPapelRepository = new PessoaPapelRepository(connectionFactory);
    private final EnderecoRepository enderecoRepository = new EnderecoRepository(connectionFactory);
    private final PessoaRepository pessoaRepository = new PessoaRepository(connectionFactory, pessoaPapelRepository);
    private final MovimentoItemRepository movimentoItemRepository = new MovimentoItemRepository(connectionFactory, produtoRepository);
    private final MovimentoRepository movimentoRepository =
            new MovimentoRepository(
                    connectionFactory,
                    pessoaRepository,
                    movimentoItemRepository
            );
    private final HistoricoEstoqueRepository historicoEstoqueRepository =
            new HistoricoEstoqueRepository(
                    produtoRepository,
                    movimentoRepository
            );
    private final EstoqueRepository estoqueRepository = new EstoqueRepository(connectionFactory);
    private final AjusteEstoqueRepository ajusteEstoqueRepository =
            new AjusteEstoqueRepository(
                    connectionFactory,
                    produtoRepository,
                    estoqueRepository
            );
    private final AjusteEstoqueItemRepository ajusteEstoqueItemRepository = new AjusteEstoqueItemRepository();


    private final EstoqueService estoqueService = new EstoqueService(estoqueRepository);
    private final HistoricoEstoqueService historicoEstoqueService = new HistoricoEstoqueService(historicoEstoqueRepository, estoqueService);
    private final PessoaService pessoaService =
            new PessoaService(
                    connectionFactory,
                    pessoaRepository,
                    pessoaPapelRepository,
                    enderecoRepository
            );
    private final CategoriaService categoriaService = new CategoriaService(categoriaRepository);
    private final ProdutoService produtoService = new ProdutoService(produtoRepository, categoriaRepository);

    private final MovimentoService movimentoService =
            new MovimentoService(
                    movimentoRepository,
                    movimentoItemRepository,
                    historicoEstoqueService,
                    connectionFactory
            );

    private final AjusteEstoqueService ajusteEstoqueService =
            new AjusteEstoqueService(
                    ajusteEstoqueRepository,
                    ajusteEstoqueItemRepository,
                    produtoRepository,
                    movimentoRepository,
                    movimentoItemRepository,
                    historicoEstoqueService,
                    pessoaRepository,
                    connectionFactory);

    private final PessoaMenu pessoaMenu = new PessoaMenu(scanner, pessoaService);
    private final CategoriaMenu categoriaMenu = new CategoriaMenu(scanner, categoriaService);
    private final ProdutoMenu produtoMenu = new ProdutoMenu(scanner, produtoService, categoriaService, categoriaMenu);
    private final AjusteEstoqueMenu ajusteEstoqueMenu =
            new AjusteEstoqueMenu(
                    scanner,
                    produtoService,
                    estoqueService,
                    ajusteEstoqueService
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