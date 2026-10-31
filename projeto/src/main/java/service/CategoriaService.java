package service;

import domain.categoria.Categoria;
import lombok.RequiredArgsConstructor;
import repository.CategoriaRepository;

import java.util.List;

@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public boolean inserirCategoria(Categoria categoria) {

        try {
            verificaSeCateoriaDescricaoExiste(categoria);

            return categoriaRepository.inserirCategoria(categoria);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    private void verificaSeCateoriaDescricaoExiste(Categoria categoria) {
        var isCategoriaJaCadastrada = categoriaRepository.categoriaPossuiProdutos(categoria);

        if (isCategoriaJaCadastrada) {
            throw new IllegalArgumentException("Categoria já cadastrada!");
        }
    }

    public int deletarInativarCategoria(Categoria categoria) {

        try {
            validarCategoriaExiste(categoriaRepository.buscarPorId(categoria.getId()));

            if (categoria.getId().equals(Categoria.ID_SEM_CATEGORIA)) {
                throw new IllegalArgumentException("A categoria padrão não pode ser removida!");
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
        if (categoria.getId() == Categoria.ID_SEM_CATEGORIA) {
            throw new IllegalArgumentException("A categoria padrão não pode ser alterada.");
        }

        if (categoriaRepository.existeOutraCategoriaComDescricao(categoria.getDescricao(), categoria.getId())) {
            throw new IllegalArgumentException("Categoria já existe");
        }
        return categoriaRepository.atualizarCategoria(categoria);
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
