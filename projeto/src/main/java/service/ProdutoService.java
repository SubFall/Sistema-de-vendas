package service;

import domain.produto.Produto;
import repository.ProdutoRepository;

public class ProdutoService {

    ProdutoRepository produtoRepository = new ProdutoRepository();

    public boolean inserirProduto(Produto produto) {
        return produtoRepository.inserirProduto(produto);
    }
}
