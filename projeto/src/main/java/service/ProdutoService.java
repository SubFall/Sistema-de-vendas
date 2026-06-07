package service;

import domain.produto.Produto;
import repository.ProdutoRepository;

import java.util.List;

public class ProdutoService {

    ProdutoRepository produtoRepository = new ProdutoRepository();

    public boolean inserirProduto(Produto produto) {
        return produtoRepository.inserirProduto(produto);
    }

    public boolean atualizarStatusProduto(int idProduto) {
        Produto produto = produtoRepository.buscarPorId(idProduto);

        if (produto == null) {
            throw new IllegalArgumentException("Produto não cadastrado!");
        }

        return produtoRepository.atualizarStatusProduto(!produto.getAtivo(), idProduto);

    }

    public boolean atualizarProduto(Produto produto) {
        Produto produtoOriginal = produtoRepository.buscarPorId(produto.getId());

        if (produtoOriginal == null) {
            throw new IllegalArgumentException("Produto não cadastrado");
        }

        return produtoRepository.atualizarProduto(produto);
    }

    public List<Produto> buscarTodos() {
        return produtoRepository.buscarTodos();
    }

    public List<Produto> buscarPorNome(String descricao) {

        if (descricao == null || descricao.isBlank()) {
            return produtoRepository.buscarTodos();
        }
        return produtoRepository.buscarPorDescricao(descricao);
    }
}
