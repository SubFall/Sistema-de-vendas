package test;

import domain.categoria.Categoria;
import domain.produto.Produto;
import service.CategoriaService;
import service.ProdutoService;
import ui.categoria.CategoriaMenu;
import ui.produto.ProdutoMenu;

import java.math.BigDecimal;

public class Principal {
    static void main(String[] args) {

//        PessoaService pessoaService = new PessoaService();
//        EnderecoService enderecoService = new EnderecoService();
//        ConsoleMenu consoleMenu = new ConsoleMenu(pessoaService, enderecoService);
        CategoriaService categoriaService = new CategoriaService();
        CategoriaMenu categoriaMenu = new CategoriaMenu(categoriaService);
        ProdutoService produtoService = new ProdutoService();
        ProdutoMenu produtoMenu = new ProdutoMenu(produtoService, categoriaService, categoriaMenu);
//        consoleMenu.iniciar();
        produtoMenu.iniciar();

        Produto produto = Produto.builder()
                .id(5)
                .descricao("Mouse Gamer")
                .precoVenda(new BigDecimal("499.99"))
                .precoCusto(new BigDecimal("299.99"))
                .categoria(null)
                .build();





    }
}