package service;

import domain.categoria.Categoria;
import repository.CategoriaRepository;

import java.util.List;

public class CategoriaService {
    CategoriaRepository categoriaRepository = new CategoriaRepository();

    public boolean inserirCategoria(Categoria categoria) {
        if (categoriaRepository.categoriaPossuiProdutos(categoria.getDescricao())) {
            throw new IllegalArgumentException("Categoria já cadastrada");
        }
        return categoriaRepository.inserirCategoria(categoria);
    }

    public int deletarInativarCategoria(int idCategoria) {
        Categoria categoria = categoriaRepository.buscarPorId(idCategoria);

        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não existe");
        }

        if (categoria.getId() == Categoria.ID_SEM_CATEGORIA) {
            throw new IllegalArgumentException("A categoria padrão não pode se removida.");
        }

        if (categoriaRepository.categoriaPossuiProdutos(categoria.getId())) {
            categoriaRepository.atualizarStatusCategoria(false, categoria.getId());
            return 0;
        }

        categoriaRepository.deletarCategoria(categoria.getId());
        return 1;
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

    public Categoria buscarPorId(int idCategoria) {
        Categoria categoria = categoriaRepository.buscarPorId(idCategoria);

        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não encontrada");
        }

        return categoria;
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

}
