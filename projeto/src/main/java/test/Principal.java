package test;

import service.ProdutoService;

public class Principal {
    static void main(String[] args) {

//        PessoaService pessoaService = new PessoaService();
//        EnderecoService enderecoService = new EnderecoService();
//        ConsoleMenu consoleMenu = new ConsoleMenu(pessoaService, enderecoService);
        ProdutoService produtoService = new ProdutoService();
//        consoleMenu.iniciar();


//        Produto teste = Produto.builder()
////                .descricao("")
////                .valorVenda(new BigDecimal("10"))
////                .precoCusto(new BigDecimal("5"))
////                .ativo(0)
//                .build();

//        System.out.println(produtoService.inserirProduto(teste));
        System.out.println(produtoService.atualizarStatusProduto(1));
    }
}