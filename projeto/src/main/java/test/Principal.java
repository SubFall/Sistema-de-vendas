package test;

import domain.categoria.Categoria;
import domain.produto.Produto;
import service.CategoriaService;
import service.ProdutoService;

import java.math.BigDecimal;

public class Principal {
    static void main(String[] args) {

//        PessoaService pessoaService = new PessoaService();
//        EnderecoService enderecoService = new EnderecoService();
//        ConsoleMenu consoleMenu = new ConsoleMenu(pessoaService, enderecoService);
        ProdutoService produtoService = new ProdutoService();
        CategoriaService categoriaService = new CategoriaService();
//        consoleMenu.iniciar();


        Produto teste = Produto.builder()
                .id(2)
                .descricao("TEST 2")
                .precoVenda(new BigDecimal("99.99"))
                .precoCusto(new BigDecimal("45.93"))
                .ativo(true)
                .build();


//        System.out.println(produtoService.inserirProduto(teste));
//        System.out.println(produtoService.atualizarProduto(teste));

//        System.out.println(produtoService.buscarTodos());

//        Categoria categoria = Categoria.builder().descricao("teste 123").build();
//        System.out.println(categoriaService.inserirCategoria(categoria));
//        System.out.println(categoriaService.deletarInativarCategoria(1));



    }
}