package service;

import domain.categoria.Categoria;
import lombok.RequiredArgsConstructor;
import repository.CategoriaRepository;

import java.util.List;

@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public Categoria inserirCategoria(Categoria categoria) {

        try {
            if (Categoria.ID_SEM_CATEGORIA.equals(categoria.getId())
                    || "Sem Categoria".equalsIgnoreCase(categoria.getDescricao().trim())) {
                return validarCategoriaExiste(categoriaRepository.buscarPorId(Categoria.ID_SEM_CATEGORIA));
            }

            verificaSeCategoriaDescricaoExiste(categoria);
            return categoriaRepository.inserirCategoria(categoria) ? categoria : null;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private void verificaSeCategoriaDescricaoExiste(Categoria categoria) {
        var isCategoriaJaCadastrada = categoriaRepository.existeOutraCategoriaComDescricao(categoria.getDescricao(), 0L);

        if (isCategoriaJaCadastrada) {
            throw new IllegalArgumentException("Categoria já cadastrada!");
        }
    }

    public int deletarInativarCategoria(Categoria categoria) {

        try {
            validarCategoriaExiste(categoriaRepository.buscarPorId(categoria.getId()));

            if (Categoria.ID_SEM_CATEGORIA.equals(categoria.getId())
                    || "Sem Categoria".equalsIgnoreCase(categoria.getDescricao().trim())) {
                throw new IllegalArgumentException("A categoria padrão nao pode ser removida!");
            }

            var isCategoriaExiste = categoriaRepository.categoriaPossuiProdutos(categoria);

            if (isCategoriaExiste) {
                categoriaRepository.atualizarStatusCategoria(false, categoria.getId());

                return 0;
            }
            categoriaRepository.deletarCategoria(categoria.getId());
            return 1;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return -1;
    }

    public boolean atualizarCategoria(Categoria categoria) {

        try {
            if (Categoria.ID_SEM_CATEGORIA.equals(categoria.getId())
                    || "Sem Categoria".equalsIgnoreCase(categoria.getDescricao().trim())) {
                throw new IllegalArgumentException("A categoria padrão não pode ser alterada.");
            }

            var isCategoriaExiste = categoriaRepository
                    .existeOutraCategoriaComDescricao(categoria.getDescricao(), categoria.getId());

            if (isCategoriaExiste) {
                throw new IllegalArgumentException("Categoria já existe");
            }
            return categoriaRepository.atualizarCategoria(categoria);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Categoria buscarPorId(Long idCategoria) {

        return validarCategoriaExiste(categoriaRepository.buscarPorId(idCategoria));
    }

    public List<Categoria> buscarPorDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            return categoriaRepository.buscarTodos();
        }
        return categoriaRepository.buscarPorDescricao(descricao);
    }

    public List<Categoria> buscarTodos() {
        return categoriaRepository.buscarTodos();
    }

    private Categoria validarCategoriaExiste(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não encontrada");
        }
        return categoria;
    }


}
