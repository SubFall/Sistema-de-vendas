package service;

import domain.categoria.Categoria;
import domain.produto.Produto;
import lombok.RequiredArgsConstructor;
import repository.CategoriaRepository;
import repository.ProdutoRepository;

import java.util.List;

@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public boolean inserirProduto(Produto produto) {

        try {
            return produtoRepository.inserirProduto(produto);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public boolean atualizarStatusProduto(Produto produto) {

        try {
            return produtoRepository.atualizarStatusProduto(produto);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public boolean atualizarProduto(Produto produto) {

        try {
            validarProdutoExiste(produtoRepository.buscarPorId(produto.getId()));

            if (produto.getCategoria() == null) {
                produto.setCategoria(categoriaRepository.buscarPorId(Categoria.ID_SEM_CATEGORIA));
            }

            return produtoRepository.atualizarProduto(produto);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public List<Produto> buscarTodos() {

        try {
            return produtoRepository.buscarTodos();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return List.of();
    }

    public List<Produto> buscarTodosAtivos() {

        try {
            return produtoRepository.buscarTodosPorStatus(true);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return List.of();
    }

    public List<Produto> buscarTodosInativos() {

        try {
            return produtoRepository.buscarTodosPorStatus(false);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return List.of();
    }

    public Produto buscarPorId(Long idProduto) {

        try {
            return validarProdutoExiste(produtoRepository.buscarPorId(idProduto));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Produto buscarPorIdAtivo(Long idProduto) {

        try {
            return validarProdutoAtivo(produtoRepository.buscarPorId(idProduto));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Produto buscarPorIdInativo(Long idProduto) {

        try {
            return validarProdutoInativo(produtoRepository.buscarPorId(idProduto));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Produto> buscarPorDescricao(String descricao) {

        try {
            if (descricao == null || descricao.isBlank()) {
                return produtoRepository.buscarTodos();
            }
            return produtoRepository.buscarPorDescricao(descricao);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return List.of();
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
            throw new IllegalArgumentException("Produto está inativo!");
        }
        return produto;
    }

    private Produto validarProdutoInativo(Produto produto) {
        validarProdutoExiste(produto);

        if (produto.isAtivo()) {
            throw new IllegalArgumentException("Produto está ativo");
        }
        return produto;
    }

}
