package service;

import domain.categoria.Categoria;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
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
    void inserirCategoria_QuandoForBemSucedido() throws SQLException {
        var categoriaExpected = categoriaList.getLast();

        BDDMockito.when(repository.inserirCategoria(categoriaExpected)).thenReturn(true);

        var newCategoria = service.inserirCategoria(categoriaExpected);

        Assertions.assertThat(newCategoria).isNotNull().isEqualTo(categoriaExpected);
    }

    @Test
    void inserirCategoria_RetornaCategoriaPadrao() {
        var categoriaPadrao = categoriaList.getFirst();

        BDDMockito.when(repository.buscarPorId(categoriaPadrao.getId())).thenReturn(categoriaPadrao);

        var categoriaPadraoExpected = service.inserirCategoria(categoriaPadrao);

        Assertions.assertThat(categoriaPadraoExpected).isNotNull().isEqualTo(categoriaPadrao);
    }

    @Test
    void inserirCategoria_VerificaSeCategoriaDescricaoExiste_LancaExcecao_RetornaNull() {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository
                .existeOutraCategoriaComDescricao(categoria.getDescricao(), 0L)).thenReturn(true);

        var categoriaIsNull = service.inserirCategoria(categoria);

        Assertions.assertThat(categoriaIsNull).isNull();
    }

    @Test
    void deletarInativarCategoria_AtualizarStatusCategoria_QuandoForBemSucedido_RetornaZero() {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.buscarPorId(categoria.getId())).thenReturn(categoria);
        BDDMockito.when(repository.categoriaPossuiProdutos(categoria)).thenReturn(true);
        BDDMockito.when(repository.atualizarStatusCategoria(false, categoria.getId())).thenReturn(true);

        var categoriaAtualizado = service.deletarInativarCategoria(categoria);

        Assertions.assertThat(categoriaAtualizado).isEqualTo(0);
    }

    @Test
    void deletarInativarCategoria_DeletarCategoria_QuandoForBemSucedido_RetornaUm() {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.buscarPorId(categoria.getId())).thenReturn(categoria);
        BDDMockito.when(repository.categoriaPossuiProdutos(categoria)).thenReturn(false);
        BDDMockito.when(repository.deletarCategoria(categoria.getId())).thenReturn(true);

        var categoriaAtualizado = service.deletarInativarCategoria(categoria);

        Assertions.assertThat(categoriaAtualizado).isEqualTo(1);
    }

    @Test
    void deletarInativarCategoria_LancaExcecao_DeletarCategoriaPadrao_RetornaMenosUm() {
        var categoriaPadrao = categoriaList.getFirst();

        BDDMockito.when(repository.buscarPorId(categoriaPadrao.getId())).thenReturn(categoriaPadrao);

        var categoriaExcecao = service.deletarInativarCategoria(categoriaPadrao);

        Assertions.assertThat(categoriaExcecao).isEqualTo(-1);
    }

    @Test
    void deletarInativarCategoria_BuscarPorId_LancaExcecao_RetornaMenosUm() {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.buscarPorId(categoria.getId()))
                .thenThrow(new RuntimeException("Falha ao buscar por ID"));

        var categoriaExcecao = service.deletarInativarCategoria(categoria);

        Assertions.assertThat(categoriaExcecao).isEqualTo(-1);
    }

    @Test
    void deletarInativarCategoria_CategoriaPossuiProdutos_LancaExcecao_RetornaMenosUm() {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.buscarPorId(categoria.getId())).thenReturn(categoria);
        BDDMockito.when(repository.categoriaPossuiProdutos(categoria))
                .thenThrow(new RuntimeException("Falha ao verificar Categoria tem Produtos!"));

        var categoriaExcecao = service.deletarInativarCategoria(categoria);

        Assertions.assertThat(categoriaExcecao).isEqualTo(-1);
    }

    @Test
    void deletarInativarCategoria_AtualizarStatusCategoria_LancaExcecao_RetornaMenosUm() {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.buscarPorId(categoria.getId())).thenReturn(categoria);
        BDDMockito.when(repository.categoriaPossuiProdutos(categoria)).thenReturn(true);
        BDDMockito.when(repository.atualizarStatusCategoria(false, categoria.getId()))
                .thenThrow(new RuntimeException("Falha ao atualizar Status Categoria!"));

        var categoriaExcecao = service.deletarInativarCategoria(categoria);

        Assertions.assertThat(categoriaExcecao).isEqualTo(-1);
    }

    @Test
    void deletarInativarCategoria_DeletarCategoria_LancaExcecao_RetornaMenosUm() {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.buscarPorId(categoria.getId())).thenReturn(categoria);
        BDDMockito.when(repository.categoriaPossuiProdutos(categoria)).thenReturn(false);
        BDDMockito.when(repository.deletarCategoria(categoria.getId()))
                .thenThrow(new RuntimeException("Falha ao Deletar Categoria"));

        var categoriaExcecao = service.deletarInativarCategoria(categoria);

        Assertions.assertThat(categoriaExcecao).isEqualTo(-1);
    }

    @Test
    void atualizarCategoria_QuandoForBemSucedido_RetornaTrue() {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.existeOutraCategoriaComDescricao(categoria.getDescricao(), categoria.getId()))
                .thenReturn(false);
        BDDMockito.when(repository.atualizarCategoria(categoria)).thenReturn(true);

        var isCategoriaAtualizado = service.atualizarCategoria(categoria);

        Assertions.assertThat(isCategoriaAtualizado).isTrue();
    }

    @Test
    void atualizarCategoria_LancaExcecao_CategoriaPadrao_RetornaFalse() {
        var categoriaPadrao = categoriaList.getFirst();

        var isCategoriaAtualizado = service.atualizarCategoria(categoriaPadrao);

        Assertions.assertThat(isCategoriaAtualizado).isFalse();
    }
    
    @Test
    void atualizarCategoria_ExisteOutraCategoriaComDescricao_LancaExcecao_RetornaFalse() {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.existeOutraCategoriaComDescricao(categoria.getDescricao(), categoria.getId()))
                .thenThrow(new RuntimeException("Falha ao verificar Outra Categoria Com Descricao"));

        var isCategoriaAtualizado = service.atualizarCategoria(categoria);

        Assertions.assertThat(isCategoriaAtualizado).isFalse();
    }

    @Test
    void atualizarCategoria_CategoriaJaExistente_LancaExcecao_RetornaFalse() {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.existeOutraCategoriaComDescricao(categoria.getDescricao(), categoria.getId()))
                .thenReturn(true);

        var isCategoriaAtualizado = service.atualizarCategoria(categoria);

        Assertions.assertThat(isCategoriaAtualizado).isFalse();
    }

    @Test
    void atualizarCategoria_atualizarCategoria_LancaExcecao_RetornaFalse() {
        var categoria = categoriaList.getLast();

        BDDMockito.when(repository.existeOutraCategoriaComDescricao(categoria.getDescricao(), categoria.getId()))
                .thenReturn(false);
        BDDMockito.when(repository.atualizarCategoria(categoria))
                .thenThrow(new RuntimeException("Falha ao Atualizar Categoria"));

        var isCategoriaAtualizado = service.atualizarCategoria(categoria);

        Assertions.assertThat(isCategoriaAtualizado).isFalse();
    }
}