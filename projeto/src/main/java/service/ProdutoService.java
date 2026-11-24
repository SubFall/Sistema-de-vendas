package service;

import domain.exception.produto.*;
import domain.produto.Produto;
import lombok.RequiredArgsConstructor;
import repository.CategoriaRepository;
import repository.ProdutoRepository;
import util.Log;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public boolean inserirProduto(Produto produto) {
        try {
            var produtosCadastradosList = buscarPorDescricao(produto.getDescricao());
            if (!produtosCadastradosList.isEmpty()) throw new FalhaValorJaRegistradoException(
                    "\nProduto: " + produto.getDescricao() + " Já Cadastrado."
            );

            return produtoRepository.inserirProduto(produto);
        } catch (SQLException e) {
            Log.registrarErro("ProdutoService", "inserirProduto", e);
            throw new FalhaAoInserirProdutoException(
                    "Nao foi possivel cadastrar o produto. Tente novamente.", e
            );
        }
    }

    public boolean atualizarStatusProduto(Produto produto) {
        try {
            produto.setModifiedOn();

            return produtoRepository.atualizarStatusProduto(produto);
        } catch (SQLException e) {
            Log.registrarErro("ProdutoService", "atualizarStatusProduto", e);
            throw new FalhaAoAtualizarProdutoException(
                    "Nao foi possivel atualizar o produto. Tente novamente."
            );
        }
    }

    public boolean atualizarProduto(Produto produto) {
        try {
            validarProdutoExiste(produtoRepository.buscarPorId(produto.getId()));
            produto.setModifiedOn();

            return produtoRepository.atualizarProduto(produto);
        } catch (SQLException e) {
            Log.registrarErro("ProdutoService", "atualizarProduto", e);
            throw new FalhaAoAtualizarProdutoException(
                    "Nao foi possivel atualizar o produto. Tente novamente."
            );
        }
    }

    public List<Produto> buscarTodos() {
        try {
            return produtoRepository.buscarTodos();
        } catch (SQLException e) {
            Log.registrarErro("ProdutoService", "buscarTodos", e);
            throw new FalhaAoBuscarProdutoException(
                    "Nao foi possivel buscar o produto. Tente novamente."
            );
        }
    }

    public List<Produto> buscarTodosAtivos() {
        try {
            return produtoRepository.buscarTodosPorStatus(true);
        } catch (SQLException e) {
            Log.registrarErro("ProdutoService", "buscarTodosAtivos", e);
            throw new FalhaAoBuscarProdutoException(
                    "Nao foi possivel buscar o produto. Tente novamente."
            );
        }
    }

    public List<Produto> buscarTodosInativos() {
        try {
            return produtoRepository.buscarTodosPorStatus(false);
        } catch (SQLException e) {
            Log.registrarErro("ProdutoService", "buscarTodosInativos", e);
            throw new FalhaAoBuscarProdutoException(
                    "Nao foi possivel buscar o produto. Tente novamente."
            );
        }
    }

    public Produto buscarPorId(Long idProduto) {
        try {
            return validarProdutoExiste(produtoRepository.buscarPorId(idProduto));
        } catch (SQLException e) {
            Log.registrarErro("ProdutoService", "buscarPorId", e);
            throw new FalhaAoBuscarProdutoException(
                    "Nao foi possivel buscar o produto. Tente novamente."
            );
        }
    }

    public Produto buscarPorIdAtivo(Long idProduto) {
        try {
            return validarProdutoAtivo(produtoRepository.buscarPorId(idProduto));
        } catch (SQLException e) {
            Log.registrarErro("ProdutoService", "buscarPorIdAtivo", e);
            throw new FalhaAoBuscarProdutoException(
                    "Nao foi possivel buscar o produto. Tente novamente."
            );
        }
    }

    public Produto buscarPorIdInativo(Long idProduto) {
        try {
            return validarProdutoInativo(produtoRepository.buscarPorId(idProduto));
        } catch (SQLException e) {
            Log.registrarErro("ProdutoService", "buscarPorIdInativo", e);
            throw new FalhaAoBuscarProdutoException(
                    "Nao foi possivel buscar o produto. Tente novamente."
            );
        }
    }

    public List<Produto> buscarPorDescricao(String descricao) {
        try {
            if (descricao == null || descricao.isBlank()) {
                return produtoRepository.buscarTodos();
            }
            return produtoRepository.buscarPorDescricao(descricao);
        } catch (SQLException e) {
            Log.registrarErro("ProdutoService", "buscarPorDescricao", e);
            throw new FalhaAoBuscarProdutoException(
                    "Nao foi possivel buscar o produto. Tente novamente."
            );
        }
    }

    private Produto validarProdutoExiste(Produto produto) {
        if (produto == null) {
            throw new FalhaProdutoNaoExisteException("\nProduto nao cadastrado.");
        }
        return produto;
    }

    private Produto validarProdutoAtivo(Produto produto) {
        validarProdutoExiste(produto);

        if (!produto.isAtivo()) {
            throw new FalhaProdutoEstaInativoException("\nProduto está inativo.");
        }
        return produto;
    }

    private Produto validarProdutoInativo(Produto produto) {
        validarProdutoExiste(produto);

        if (produto.isAtivo()) {
            throw new FalhaProdutoEstaAtivoException("\nProduto está ativo.");
        }
        return produto;
    }

}
