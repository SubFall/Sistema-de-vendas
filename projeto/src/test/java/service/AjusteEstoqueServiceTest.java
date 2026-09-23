package service;

import conn.ConnectionProvider;
import domain.ajusteestoque.AjusteEstoque;
import domain.ajusteestoque.AjusteEstoqueItens;
import domain.ajusteestoque.Status;
import domain.estoque.Estoque;
import domain.produto.Produto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.AjusteEstoqueItemRepository;
import repository.AjusteEstoqueRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class AjusteEstoqueServiceTest {
    @Mock
    private AjusteEstoqueRepository estoqueRepository;

    @Mock
    private AjusteEstoqueItemRepository ajusteEstoqueItemRepository;

    @Mock
    private ConnectionProvider connectionProvider;

    @Mock
    private Connection connection;

    private List<AjusteEstoqueItens> estoqueItensList;
    private AjusteEstoque ajusteEstoque;

    @InjectMocks
    private AjusteEstoqueService service;

    @BeforeEach
    void init() {
        var computador = Produto.builder().id(1).descricao("computador").precoCusto(new BigDecimal("999")).precoVenda(new BigDecimal("1500")).build();
        var notebook = Produto.builder().id(2).descricao("notebook").precoCusto(new BigDecimal("1200")).precoVenda(new BigDecimal("2000")).build();
        var mouse = Produto.builder().id(3).descricao("mouse logitech").precoCusto(new BigDecimal("399")).precoVenda(new BigDecimal("800")).build();

        var estoqueComputador = Estoque.builder().idProduto(computador.getId()).quantidade(new BigDecimal("5")).build();
        var estoqueNotebook = Estoque.builder().idProduto(notebook.getId()).quantidade(new BigDecimal("10")).build();
        var estoqueMouse = Estoque.builder().idProduto(mouse.getId()).quantidade(new BigDecimal("15")).build();

        var ajusteEstoqueComputador = AjusteEstoqueItens.builder().id(1L).produto(computador).contagem(new BigDecimal("10")).estoque(estoqueComputador).build();
        var ajusteEstoqueNotebook = AjusteEstoqueItens.builder().id(2L).produto(notebook).contagem(new BigDecimal("20")).estoque(estoqueNotebook).build();
        var ajusteEstoqueMouse = AjusteEstoqueItens.builder().id(3L).produto(mouse).contagem(new BigDecimal("10")).estoque(estoqueMouse).build();

        this.estoqueItensList = new ArrayList<>(List.of(ajusteEstoqueComputador, ajusteEstoqueNotebook, ajusteEstoqueMouse));
        this.ajusteEstoque = AjusteEstoque.builder().id(1L).titulo("Teste").status(Status.ABERTO).ajusteEstoqueItens(this.estoqueItensList).build();
    }

    @Test
    @Order(1)
    void deveInserirAjusteEstoqueComSucesso() throws SQLException {
        var idAjuste = 1L;

        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.when(estoqueRepository.inserirAjusteEstoque(connection, ajusteEstoque)).thenReturn(idAjuste);
        BDDMockito.when(ajusteEstoqueItemRepository.inserirAjusteEstoqueItens(connection, idAjuste, estoqueItensList)).thenReturn(true);

        Assertions.assertThatNoException().isThrownBy(() -> service.inserirAjusteEstoque(ajusteEstoque));

        BDDMockito.verify(connection).setAutoCommit(false);
        BDDMockito.verify(connection).commit();
        BDDMockito.verify(connection).close();
    }

    @Test
    @Order(2)
    void deveLancarExcecaoQuandoNaoInserirItem() throws SQLException {
        var idAjuste = 1L;

        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.when(estoqueRepository.inserirAjusteEstoque(connection, ajusteEstoque)).thenReturn(idAjuste);
        BDDMockito.when(ajusteEstoqueItemRepository.inserirAjusteEstoqueItens(connection, idAjuste, estoqueItensList)).thenReturn(false);

        Assertions.assertThatException().isThrownBy(() -> service.inserirAjusteEstoque(ajusteEstoque))
                .isInstanceOf(IllegalArgumentException.class)
                .withMessage("Erro ao inserir item do Ajuste Estoque");

        BDDMockito.verify(connection).rollback();
        BDDMockito.verify(connection).close();
        BDDMockito.verify(connection, BDDMockito.never()).commit();
    }

    @Test
    @Order(3)
    void deveLancarExcecaoSQLException() throws SQLException {
        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.when(estoqueRepository.inserirAjusteEstoque(connection, ajusteEstoque)).thenThrow(SQLException.class);

        Assertions.assertThatException().isThrownBy(() -> service.inserirAjusteEstoque(ajusteEstoque))
                .isInstanceOf(RuntimeException.class);

        BDDMockito.verify(connection).rollback();
        BDDMockito.verify(connection).close();
        BDDMockito.verify(connection, BDDMockito.never()).commit();
    }
}