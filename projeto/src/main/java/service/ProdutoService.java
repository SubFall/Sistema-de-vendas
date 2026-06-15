package service;

import domain.categoria.Categoria;
import domain.produto.Produto;
import repository.CategoriaRepository;
import repository.ProdutoRepository;

import java.util.List;

public class ProdutoService {

    ProdutoRepository produtoRepository = new ProdutoRepository();
    CategoriaRepository categoriaRepository = new CategoriaRepository();

    public boolean inserirProduto(Produto produto) {

        if (produto.getCategoria() == null) {
            produto.setCategoria(categoriaRepository.buscarPorId(Categoria.ID_SEM_CATEGORIA));
        }
        return produtoRepository.inserirProduto(produto);
    }

    public boolean atualizarStatusProduto(int idProduto) {
        Produto produto = produtoRepository.buscarPorId(idProduto);

        if (produto == null) {
            throw new IllegalArgumentException("Produto não cadastrado!");
        }

        return produtoRepository.atualizarStatusProduto(!produto.isAtivo(), idProduto);

    }

    public boolean atualizarProduto(Produto produto) {
        Produto produtoOriginal = produtoRepository.buscarPorId(produto.getId());

        if (produtoOriginal == null) {
            throw new IllegalArgumentException("Produto não cadastrado");
        }

        if (produto.getCategoria() == null) {
            produto.setCategoria(categoriaRepository.buscarPorId(Categoria.ID_SEM_CATEGORIA));
        }

        return produtoRepository.atualizarProduto(produto);
    }

    public List<Produto> buscarTodos() {
        return produtoRepository.buscarTodos();
    }

    public Produto buscarPorId(int idProduto) {
        return produtoRepository.buscarPorId(idProduto);
    }

    public List<Produto> buscarPorNome(String descricao) {

        if (descricao == null || descricao.isBlank()) {
            return produtoRepository.buscarTodos();
        }
        return produtoRepository.buscarPorDescricao(descricao);
    }
}
