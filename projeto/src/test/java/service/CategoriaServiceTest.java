package service;

import domain.categoria.Categoria;
import domain.exception.categoria.*;
import domain.exception.produto.FalhaAoBuscarCategoriaException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.CategoriaRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {
    @Mock
    private CategoriaRepository repository;

    @InjectMocks
    private CategoriaService service;

    private List<Categoria> categoriaList;

    @BeforeEach
    void init() {
        var semDescricao = Categoria.builder().id(1L).descricao("Sem Categoria").ativo(true).build();
        var informatica = Categoria.builder().id(2L).descricao("Informática").ativo(true).build();
        var papelaria = Categoria.builder().id(3L).descricao("Papelaria").ativo(true).build();
        categoriaList = new ArrayList<>(List.of(semDescricao, informatica, papelaria));
    }

    @Test
    void inserirCategoria_QuandoForBemSucedido_RetornaCategoria() throws SQLException {
        var categoriaExpected = categoriaList.getLast();

        BDDMockito.when(repository.inserirCategoria(categoriaExpected))
                .thenReturn(true);

        BDDMockito.when(repository.existeOutraCategoriaComDescricao(categoriaExpected.getDescricao(), 0L))
                .thenReturn(false);

        var newCategoria = service.inserirCategoria(categoriaExpected);

        Assertions.assertThat(newCategoria).isNotNull().isEqualTo(categoriaExpected);
    }

    @Test
    void inserirCategoria_LancaExcecaoDeDominio_FalhaAoInserirCategoriaException() throws SQLException {
        var categoriaExpected = categoriaList.getLast();

        BDDMockito.when(repository.inserirCategoria(categoriaExpected))
                .thenThrow(new SQLException());

        Assertions.assertThatException()
                .isThrownBy(() -> service.inserirCategoria(categoriaExpected))
                .isInstanceOf(FalhaAoInserirCategoriaException.class)
                .withMessage("Nao foi possivel cadastrar a categoria. Tente novamente.");
    }

    @Test
    void inserirCategoria_RetornaCategoriaPadrao() throws SQLException {
        var categoriaPadrao = categoriaList.getFirst();

        BDDMockito.when(repository.buscarPorId(Categoria.ID_SEM_CATEGORIA))
                .thenReturn(categoriaPadrao);

        var categoriaPadraoExpected = service.inserirCategoria(categoriaPadrao);

        Assertions.assertThat(categoriaPadraoExpected).isNotNull().isEqualTo(categoriaPadrao);
    }

    @Test
    void inserirCategoria_LancaExcecaoDeDominio_FalhaCategoriaJaExisteException() throws SQLException {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.existeOutraCategoriaComDescricao(categoria.getDescricao(), 0L))
                .thenThrow(new FalhaCategoriaJaExisteException("Categoria já cadastrada."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.inserirCategoria(categoria))
                .isInstanceOf(FalhaCategoriaJaExisteException.class)
                .withMessage("Categoria já cadastrada.");
    }

    @Test
    void deletarInativarCategoria_AtualizarStatusCategoria_QuandoForBemSucedido_RetornaZero() throws SQLException {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.buscarPorId(categoria.getId()))
                .thenReturn(categoria);
        BDDMockito.when(repository.categoriaPossuiProdutos(categoria))
                .thenReturn(true);
        BDDMockito.when(repository.atualizarStatusCategoria(false, categoria.getId()))
                .thenReturn(true);

        var categoriaAtualizado = service.deletarInativarCategoria(categoria);

        Assertions.assertThat(categoriaAtualizado).isEqualTo(0);
    }

    @Test
    void deletarInativarCategoria_DeletarCategoria_QuandoForBemSucedido_RetornaUm() throws SQLException {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.buscarPorId(categoria.getId()))
                .thenReturn(categoria);
        BDDMockito.when(repository
                .categoriaPossuiProdutos(categoria)).thenReturn(false);
        BDDMockito.when(repository
                .deletarCategoria(categoria.getId())).thenReturn(true);

        var categoriaAtualizado = service.deletarInativarCategoria(categoria);

        Assertions.assertThat(categoriaAtualizado).isEqualTo(1);
    }

    @Test
    void deletarInativarCategoria_BuscarPorId_LancaExcecaoDominio_FalhaCategoriaJaExisteException() throws SQLException {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.buscarPorId(categoria.getId()))
                .thenThrow(new FalhaCategoriaNaoExisteException("Categoria não encontrada."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.deletarInativarCategoria(categoria))
                .isInstanceOf(FalhaCategoriaNaoExisteException.class)
                .withMessage("Categoria não encontrada.");
    }

    @Test
    void deletarInativarCategoria_DeletarCategoriaPadrao_LancaExcecaoDominio_FalhaCategoriaPadraoNaoPodeAlterarException() throws SQLException {
        var categoriaPadrao = categoriaList.getFirst();

        BDDMockito.when(repository.buscarPorId(Categoria.ID_SEM_CATEGORIA))
                .thenThrow(new FalhaCategoriaPadraoNaoPodeAlterarException("A categoria padrão nao pode ser removida."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.deletarInativarCategoria(categoriaPadrao))
                .isInstanceOf(FalhaCategoriaPadraoNaoPodeAlterarException.class)
                .withMessage("A categoria padrão nao pode ser removida.");
    }

    @Test
    void deletarInativarCategoria_BuscarPorId_LancaExcecaoDeDominio_FalhaAoDeletarOuInativarCategoriaException() throws SQLException {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.buscarPorId(categoria.getId()))
                .thenThrow(new SQLException());

        Assertions.assertThatException()
                .isThrownBy(() -> service.deletarInativarCategoria(categoria))
                .isInstanceOf(FalhaAoDeletarOuInativarCategoriaException.class)
                .withMessage("Nao foi possivel deletar/inativar a categoria. Tente novamente.");
    }

    @Test
    void deletarInativarCategoria_CategoriaPossuiProdutos_LancaExcecaoDeDominio_FalhaAoDeletarOuInativarCategoriaException() throws SQLException {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.buscarPorId(categoria.getId()))
                .thenReturn(categoria);
        BDDMockito.when(repository.categoriaPossuiProdutos(categoria))
                .thenThrow(new SQLException());

        Assertions.assertThatException()
                .isThrownBy(() -> service.deletarInativarCategoria(categoria))
                .isInstanceOf(FalhaAoDeletarOuInativarCategoriaException.class)
                .withMessage("Nao foi possivel deletar/inativar a categoria. Tente novamente.");
    }

    @Test
    void deletarInativarCategoria_AtualizarStatusCategoria_LancaExcecaoDeDominio_FalhaAoDeletarOuInativarCategoriaException() throws SQLException {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.buscarPorId(categoria.getId()))
                .thenReturn(categoria);
        BDDMockito.when(repository.categoriaPossuiProdutos(categoria))
                .thenReturn(true);
        BDDMockito.when(repository.atualizarStatusCategoria(false, categoria.getId()))
                .thenThrow(new SQLException());

        Assertions.assertThatException()
                .isThrownBy(() -> service.deletarInativarCategoria(categoria))
                .isInstanceOf(FalhaAoDeletarOuInativarCategoriaException.class)
                .withMessage("Nao foi possivel deletar/inativar a categoria. Tente novamente.");
    }

    @Test
    void deletarInativarCategoria_DeletarCategoria_LancaExcecaoDeDominio_FalhaAoDeletarOuInativarCategoriaException() throws SQLException {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.buscarPorId(categoria.getId()))
                .thenReturn(categoria);
        BDDMockito.when(repository.categoriaPossuiProdutos(categoria))
                .thenReturn(false);
        BDDMockito.when(repository.deletarCategoria(categoria.getId()))
                .thenThrow(new SQLException());

        Assertions.assertThatException()
                .isThrownBy(() -> service.deletarInativarCategoria(categoria))
                .isInstanceOf(FalhaAoDeletarOuInativarCategoriaException.class)
                .withMessage("Nao foi possivel deletar/inativar a categoria. Tente novamente.");
    }

    @Test
    void atualizarCategoria_QuandoForBemSucedido_RetornaTrue() throws SQLException {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.existeOutraCategoriaComDescricao(categoria.getDescricao(), categoria.getId()))
                .thenReturn(false);
        BDDMockito.when(repository.atualizarCategoria(categoria)).thenReturn(true);

        var isCategoriaAtualizado = service.atualizarCategoria(categoria);

        Assertions.assertThat(isCategoriaAtualizado).isTrue();
    }

    @Test
    void atualizarCategoria_CategoriaPadrao_LancaExcecaoDeDominio_FalhaCategoriaPadraoNaoPodeAlterarException() {
        var categoriaPadrao = categoriaList.getFirst();

        Assertions.assertThatException()
                .isThrownBy(() -> service.atualizarCategoria(categoriaPadrao))
                .isInstanceOf(FalhaCategoriaPadraoNaoPodeAlterarException.class)
                .withMessage("A categoria padrão não pode ser alterada.");
    }

    @Test
    void atualizarCategoria_ExisteOutraCategoriaComDescricao_LancaExcecaoDeDominio_FalhaCategoriaJaExisteException() throws SQLException {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.existeOutraCategoriaComDescricao(categoria.getDescricao(), categoria.getId()))
                .thenThrow(new FalhaCategoriaJaExisteException("Categoria já existe."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.atualizarCategoria(categoria))
                .isInstanceOf(FalhaCategoriaJaExisteException.class)
                .withMessage("Categoria já existe.");
    }

    @Test
    void atualizarCategoria_atualizarCategoria_LancaExcecaoDeDominio_FalhaAoAtualizarCategoriaException() throws SQLException {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.existeOutraCategoriaComDescricao(categoria.getDescricao(), categoria.getId()))
                .thenReturn(false);
        BDDMockito.when(repository.atualizarCategoria(categoria))
                .thenThrow(new SQLException());

        Assertions.assertThatException()
                .isThrownBy(() -> service.atualizarCategoria(categoria))
                .isInstanceOf(FalhaAoAtualizarCategoriaException.class)
                .withMessage("Nao foi possivel atualizar a categoria. Tente novamente.");
    }

    @Test
    void buscarPorId_RetornaCategoriia_QuandoForBemSucedido() throws SQLException {
        var categoria = categoriaList.getFirst();

        BDDMockito.when(repository.buscarPorId(categoria.getId()))
                .thenReturn(categoria);

        var categoriaExpected = service.buscarPorId(categoria.getId());

        Assertions.assertThat(categoriaExpected).isNotNull().isEqualTo(categoria);
    }

    @Test
    void buscarPorId_ValidarCategoriaExiste_LancaExcecaoDeDominio_FalhaCategoriaNaoExisteException() throws SQLException {
        var id = 99L;

        BDDMockito.when(repository.buscarPorId(id))
                .thenReturn(null);

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarPorId(id))
                .isInstanceOf(FalhaCategoriaNaoExisteException.class)
                .withMessage("Categoria não encontrada.");
    }

    @Test
    void buscarPorId_LancaExcecaoDeDominio_FalhaAoBuscarCategoriaException() throws SQLException {
        var categoria = categoriaList.getFirst();

        BDDMockito.when(repository.buscarPorId(categoria.getId()))
                .thenThrow(new SQLException());

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarPorId(categoria.getId()))
                .isInstanceOf(FalhaAoBuscarCategoriaException.class)
                .withMessage("Nao foi possivel buscar a categoria. Tente novamente.");
    }

    @Test
    void buscarPorDescricao_QuandoForBemSucedido() throws SQLException {
        var categoria = categoriaList.getFirst();

        BDDMockito.when(repository.buscarPorDescricao(categoria.getDescricao()))
                .thenReturn(List.of(categoria));

        var categoriaListExpected = service.buscarPorDescricao(categoria.getDescricao());

        Assertions.assertThat(categoriaListExpected).isNotEmpty().isEqualTo(List.of(categoria));
    }

    @Test
    void buscarPorDescricao_QuandoDescricaoForNull_RetornaTodasCategorias() throws SQLException {
        BDDMockito.when(repository.buscarTodos()).thenReturn(categoriaList);

        var categoriasListExpected = service.buscarPorDescricao(null);

        Assertions.assertThat(categoriasListExpected).isNotEmpty().isEqualTo(categoriaList);
    }

    @Test
    void buscarPorDescricao_QuandoDescricaoForBlank_RetornaTodasCategorias() throws SQLException {
        BDDMockito.when(repository.buscarTodos()).thenReturn(categoriaList);

        var categoriasListExpected = service.buscarPorDescricao("   ");

        Assertions.assertThat(categoriasListExpected).isNotEmpty().isEqualTo(categoriaList);
    }

    @Test
    void buscarPorDescricao_LancaExcecaoDeDominio_FalhaAoBuscarCategoriaException() throws SQLException {
        var categoria = categoriaList.getFirst();

        BDDMockito.when(repository.buscarPorDescricao(categoria.getDescricao()))
                .thenThrow(new SQLException());

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarPorDescricao(categoria.getDescricao()))
                .isInstanceOf(FalhaAoBuscarCategoriaException.class)
                .withMessage("Nao foi possivel buscar a categoria. Tente novamente.");
    }

    @Test
    void buscarTodos_QuandoForBemSucedido_RetornaTodasCategorias() throws SQLException {
        BDDMockito.when(repository.buscarTodos()).thenReturn(categoriaList);

        var categoriasListExpected = service.buscarTodos();

        Assertions.assertThat(categoriasListExpected).isNotEmpty().isEqualTo(categoriaList);
    }

    @Test
    void buscarTodos_LancaExcecaoDeDominio_FalhaAoBuscarCategoriaException() throws SQLException {
        BDDMockito.when(repository.buscarTodos()).thenThrow(new SQLException());

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarTodos())
                .isInstanceOf(FalhaAoBuscarCategoriaException.class)
                .withMessage("Nao foi possivel buscar a categoria. Tente novamente.");
    }
}