package service;

import domain.categoria.Categoria;
import domain.produto.Produto;
import repository.CategoriaRepository;
import repository.ProdutoRepository;

import java.util.List;

public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public boolean inserirProduto(Produto produto) {

        if (produto.getCategoria() == null) {
            produto.setCategoria(categoriaRepository.buscarPorId(Categoria.ID_SEM_CATEGORIA));
        }
        return produtoRepository.inserirProduto(produto);
    }

    public boolean atualizarStatusProduto(int idProduto) {
        Produto produto = validarProdutoExiste(produtoRepository.buscarPorId(idProduto));

        return produtoRepository.atualizarStatusProduto(!produto.isAtivo(), idProduto);

    }

    public boolean atualizarProduto(Produto produto) {
        Produto produtoOriginal = validarProdutoExiste(produtoRepository.buscarPorId(produto.getId()));

        if (produto.getCategoria() == null) {
            produto.setCategoria(categoriaRepository.buscarPorId(Categoria.ID_SEM_CATEGORIA));
        }

        return produtoRepository.atualizarProduto(produto);
    }

    public List<Produto> buscarTodos() {
        return produtoRepository.buscarTodos();
    }

    public List<Produto> buscarTodosAtivo() {
        return produtoRepository.buscarTodosAtivo();
    }

    public Produto buscarPorId(int idProduto) {

        return validarProdutoExiste(produtoRepository.buscarPorId(idProduto));
    }

    public Produto buscarPorIdAtivo(int idProduto) {

        return validarProdutoAtivo(produtoRepository.buscarPorId(idProduto));
    }

    public List<Produto> buscarPorDescricao(String descricao) {

        if (descricao == null || descricao.isBlank()) {
            return produtoRepository.buscarTodos();
        }
        return produtoRepository.buscarPorDescricao(descricao);
    }

    private Produto validarProdutoExiste(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não cadastrado");
        }
        return produto;
    }

    private Produto validarProdutoAtivo(Produto produto) {
        validarProdutoExiste(produto);

        if (!produto.isAtivo()) {
            throw new IllegalArgumentException("Produto está inativo");
        }
        return produto;
    }

}
