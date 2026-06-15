package test;

import domain.categoria.Categoria;
import domain.produto.Produto;
import service.CategoriaService;
import service.ProdutoService;
import ui.categoria.CategoriaMenu;

import java.math.BigDecimal;

public class Principal {
    static void main(String[] args) {

//        PessoaService pessoaService = new PessoaService();
//        EnderecoService enderecoService = new EnderecoService();
//        ConsoleMenu consoleMenu = new ConsoleMenu(pessoaService, enderecoService);
        CategoriaService categoriaService = new CategoriaService();
        CategoriaMenu categoriaMenu = new CategoriaMenu(categoriaService);
        ProdutoService produtoService = new ProdutoService();
//        consoleMenu.iniciar();

        Produto produto = Produto.builder()
                .id(5)
                .descricao("Mouse Gamer")
                .precoVenda(new BigDecimal("499.99"))
                .precoCusto(new BigDecimal("299.99"))
                .categoria(null)
                .build();

//        System.out.println(produtoService.inserirProduto(produto));
        System.out.println(produtoService.atualizarProduto(produto));
//        System.out.println(produtoService.buscarPorId(1));



    }
}