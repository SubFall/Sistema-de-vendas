package service;

import domain.produto.Produto;
import repository.ProdutoRepository;

public class ProdutoService {

    ProdutoRepository produtoRepository = new ProdutoRepository();

    public boolean inserirProduto(Produto produto) {
        return produtoRepository.inserirProduto(produto);
    }

    public boolean atualizarStatusProduto(int idProduto) {
        Produto produto = produtoRepository.buscarPorId(idProduto);

        if (produto == null) {
            System.out.println("Produto não cadastrado!");
        }

        return produtoRepository.atualizarStatusProduto(!produto.getAtivo(), idProduto);

    }
}
