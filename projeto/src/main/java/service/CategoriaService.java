package service;

import domain.categoria.Categoria;
import domain.exception.categoria.*;
import domain.exception.produto.FalhaAoBuscarCategoriaException;
import lombok.RequiredArgsConstructor;
import repository.CategoriaRepository;
import util.Log;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public Categoria inserirCategoria(Categoria categoria) {
        try {
            if (Categoria.ID_SEM_CATEGORIA.equals(categoria.getId()) ||
                    "Sem Categoria".equalsIgnoreCase(categoria.getDescricao().trim()))
                return validarCategoriaExiste(categoriaRepository.buscarPorId(Categoria.ID_SEM_CATEGORIA));

            verificaSeCategoriaDescricaoExiste(categoria);
            return categoriaRepository.inserirCategoria(categoria) ? categoria : null;
        } catch (SQLException e) {
            Log.registrarErro("CategoriaService", "inserirCategoria", e);
            throw new FalhaAoInserirCategoriaException(
                    "Nao foi possivel cadastrar a categoria. Tente novamente."
            );
        }
    }

    private void verificaSeCategoriaDescricaoExiste(Categoria categoria) {
        try {
            var isCategoriaJaCadastrada = categoriaRepository.existeOutraCategoriaComDescricao(categoria.getDescricao(), 0L);

            if (isCategoriaJaCadastrada) throw new FalhaCategoriaJaExisteException("Categoria já cadastrada.");

        } catch (SQLException e) {
            Log.registrarErro("CategoriaService", "verificaSeCategoriaDescricaoExiste", e);
            throw new FalhaAoBuscarCategoriaException(
                    "Nao foi possivel buscar a categoria. Tente novamente."
            );
        }
    }

    public int deletarInativarCategoria(Categoria categoria) {
        try {
            validarCategoriaExiste(categoriaRepository.buscarPorId(categoria.getId()));

            if (Categoria.ID_SEM_CATEGORIA.equals(categoria.getId())
                    || "Sem Categoria".equalsIgnoreCase(categoria.getDescricao().trim()))
                throw new FalhaCategoriaPadraoNaoPodeAlterarException("A categoria padrão nao pode ser removida.");

            var isCategoriaExiste = categoriaRepository.categoriaPossuiProdutos(categoria);

            if (isCategoriaExiste) {
                categoriaRepository.atualizarStatusCategoria(false, categoria.getId());

                return 0;
            }

            categoriaRepository.deletarCategoria(categoria.getId());
            return 1;
        } catch (SQLException e) {
            Log.registrarErro("CategoriaService", "deletarInativarCategoria", e);
            throw new FalhaAoDeletarOuInativarCategoriaException(
                    "Nao foi possivel deletar/inativar a categoria. Tente novamente."
            );
        }
    }

    public boolean atualizarCategoria(Categoria categoria) {
        try {
            if (Categoria.ID_SEM_CATEGORIA.equals(categoria.getId())
                    || "Sem Categoria".equalsIgnoreCase(categoria.getDescricao().trim()))
                throw new FalhaCategoriaPadraoNaoPodeAlterarException("A categoria padrão não pode ser alterada.");

            var isCategoriaExiste = categoriaRepository.existeOutraCategoriaComDescricao(categoria.getDescricao(), categoria.getId());

            if (isCategoriaExiste) throw new FalhaCategoriaJaExisteException("Categoria já existe.");

            return categoriaRepository.atualizarCategoria(categoria);
        } catch (SQLException e) {
            Log.registrarErro("CategoriaService", "atualizarCategoria", e);
            throw new FalhaAoAtualizarCategoriaException(
                    "Nao foi possivel atualizar a categoria. Tente novamente."
            );
        }
    }

    public Categoria buscarPorId(Long idCategoria) {
        try {
            return validarCategoriaExiste(categoriaRepository.buscarPorId(idCategoria));
        } catch (SQLException e) {
            Log.registrarErro("CategoriaService", "buscarPorId", e);
            throw new FalhaAoBuscarCategoriaException(
                    "Nao foi possivel buscar a categoria. Tente novamente."
            );
        }
    }

    public List<Categoria> buscarPorDescricao(String descricao) {
        try {
            if (descricao == null || descricao.isBlank()) return categoriaRepository.buscarTodos();

            return categoriaRepository.buscarPorDescricao(descricao);
        } catch (SQLException e) {
            Log.registrarErro("CategoriaService", "buscarPorDescricao", e);
            throw new FalhaAoBuscarCategoriaException(
                    "Nao foi possivel buscar a categoria. Tente novamente."
            );
        }
    }

    public List<Categoria> buscarTodos() {
        try {
            return categoriaRepository.buscarTodos();
        } catch (SQLException e) {
            Log.registrarErro("CategoriaService", "buscarTodos", e);
            throw new FalhaAoBuscarCategoriaException(
                    "Nao foi possivel buscar a categoria. Tente novamente."
            );
        }
    }

    private Categoria validarCategoriaExiste(Categoria categoria) {
        if (categoria == null) throw new FalhaCategoriaNaoExisteException("Categoria não encontrada.");

        return categoria;
    }
}