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
        var semDescricao = Categoria.builder().id(1L).descricao("Sem Descrição").ativo(true).build();
        var informatica = Categoria.builder().id(2L).descricao("Informática").ativo(true).build();
        var papelaria = Categoria.builder().id(3L).descricao("Papelaria").ativo(true).build();
        categoriaList = new ArrayList<>(List.of(semDescricao, informatica, papelaria));
    }

    @Test
    void inserirCategoria_QuandoForBemSucedido() throws SQLException {
        var categoria = categoriaList.getFirst();

        BDDMockito.when(repository.inserirCategoria(categoria)).thenReturn(true);

        var isInseriu = service.inserirCategoria(categoria);

        Assertions.assertThat(isInseriu).isTrue();
    }

    @Test
    void inserirCategoria_CategoriaPossuiProdutosLancaExcecao_RetornaFalse() {
        var categoria = categoriaList.getFirst();

        BDDMockito.when(repository.categoriaPossuiProdutos(categoria))
                .thenThrow(new RuntimeException("Falha ao verificar Cateoria tem Produtos!"));

        var isInseriu = service.inserirCategoria(categoria);

        Assertions.assertThat(isInseriu).isFalse();
    }

    @Test
    void inserirCategoria_VerificaSeCateoriaDescricaoExisteLancaExcecao_RetornaFalse() {
        var categoria = categoriaList.getFirst();

        BDDMockito.when(repository.categoriaPossuiProdutos(categoria))
                .thenThrow(new IllegalArgumentException("Categoria já cadastrada!"));

        var isInseriu = service.inserirCategoria(categoria);

        Assertions.assertThat(isInseriu).isFalse();
    }

    @Test
    void deletarInativarCategoria_QuandoForBemSucedido() {
        var categoria = categoriaList.getFirst();
    }
}