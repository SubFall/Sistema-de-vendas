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
        var categoriaExpected = categoriaList.getLast();

        BDDMockito.when(repository.inserirCategoria(categoriaExpected)).thenReturn(true);

        var newCategoria = service.inserirCategoria(categoriaExpected);

        Assertions.assertThat(newCategoria).isNotNull().isEqualTo(categoriaExpected);
    }

}