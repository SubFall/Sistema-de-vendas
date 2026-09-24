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
    private AjusteEstoqueItemRepository estoqueItemRepository;

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
    void inserirAjusteEstoque_InserirAjusteEstoqueComSucesso() throws SQLException {
        var idAjuste = 1L;

        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.when(estoqueRepository.inserirAjusteEstoque(connection, ajusteEstoque)).thenReturn(idAjuste);
        BDDMockito.when(estoqueItemRepository.inserirAjusteEstoqueItens(connection, idAjuste, estoqueItensList)).thenReturn(true);

        Assertions.assertThatNoException().isThrownBy(() -> service.inserirAjusteEstoque(ajusteEstoque));

        BDDMockito.verify(connection).setAutoCommit(false);
        BDDMockito.verify(connection).commit();
        BDDMockito.verify(connection).close();
    }

    @Test
    @Order(2)
    void inserirAjusteEstoque_LancarExcecaoQuandoNaoInserirItem() throws SQLException {
        var idAjuste = 1L;

        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.when(estoqueRepository.inserirAjusteEstoque(connection, ajusteEstoque)).thenReturn(idAjuste);
        BDDMockito.when(estoqueItemRepository.inserirAjusteEstoqueItens(connection, idAjuste, estoqueItensList)).thenReturn(false);

        Assertions.assertThatException().isThrownBy(() -> service.inserirAjusteEstoque(ajusteEstoque))
                .isInstanceOf(IllegalArgumentException.class)
                .withMessage("Erro ao inserir item do Ajuste Estoque");

        BDDMockito.verify(connection).rollback();
        BDDMockito.verify(connection).close();
        BDDMockito.verify(connection, BDDMockito.never()).commit();
    }

    @Test
    @Order(3)
    void inserirAjusteEstoque_LancarExcecaoSQLException() throws SQLException {
        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.when(estoqueRepository.inserirAjusteEstoque(connection, ajusteEstoque)).thenThrow(SQLException.class);

        Assertions.assertThatException().isThrownBy(() -> service.inserirAjusteEstoque(ajusteEstoque))
                .isInstanceOf(RuntimeException.class);

        BDDMockito.verify(connection).rollback();
        BDDMockito.verify(connection).close();
        BDDMockito.verify(connection, BDDMockito.never()).commit();
    }

    @Test
    @Order(4)
    void buscarAjustePorStatus_RetornaListaAjusteEstoque_QuandoForBemSucedido() {
        var status = Status.ABERTO;

        BDDMockito.when(estoqueRepository.buscarAjustePorStatus(status)).thenReturn(List.of(ajusteEstoque));

        var ajusteEstoquesList = service.buscarAjustePorStatus(status);

        Assertions.assertThat(ajusteEstoquesList).isEqualTo(List.of(ajusteEstoque));
    }

    @Test
    @Order(5)
    void buscarAjustePorStatus_RetornaListaAjusteEstoqueVazia() {
        var status = Status.ABERTO;

        BDDMockito.when(estoqueRepository.buscarAjustePorStatus(status)).thenReturn(List.of());

        var ajusteEstoquesList = service.buscarAjustePorStatus(status);

        Assertions.assertThat(ajusteEstoquesList).isEmpty();
    }

    @Test
    @Order(6)
    void buscarTodosAjuste_RetornaListaAjusteEstoque_QuandoForBemSucedido() {
        BDDMockito.when(estoqueRepository.buscarTodosAjuste()).thenReturn(List.of(ajusteEstoque));

        var ajusteEstoqueList = service.buscarTodosAjuste();

        Assertions.assertThat(ajusteEstoqueList).isEqualTo(List.of(ajusteEstoque));
    }

    @Test
    @Order(7)
    void buscarTodosAjuste_RetornaListaVaziaAjusteEstoque() {
        BDDMockito.when(estoqueRepository.buscarTodosAjuste()).thenReturn(List.of());

        var ajusteEstoqueList = service.buscarTodosAjuste();

        Assertions.assertThat(ajusteEstoqueList).isEmpty();
    }
    
    @Test
    @Order(8)
    void buscarAjustePorId_RetornaAjusteEstoque_QuandoForBemSucedido() {
        var ajusteEstoqueId = ajusteEstoque.getId();
        
        BDDMockito.when(estoqueRepository.buscarAjustePorId(ajusteEstoqueId)).thenReturn(ajusteEstoque);

        var ajusteEstoqueEsperado = service.buscarAjustePorId(ajusteEstoqueId);

        Assertions.assertThat(ajusteEstoqueEsperado).isEqualTo(ajusteEstoque);
    }

    @Test
    @Order(9)
    void buscarAjustePorId_LancaExcecaoIllegalArgumentException_QuandoIdNaoEncontrado() {
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(estoqueRepository.buscarAjustePorId(ajusteEstoqueId)).thenThrow(IllegalArgumentException.class);

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarAjustePorId(ajusteEstoqueId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Order(10)
    void buscarAjusteEstoqueItemPorId_RetornaAjusteEstoqueItens_QuandoForBemSucedido() {
        var ajusteEstoqueItem = estoqueItensList.getFirst();

        BDDMockito.when(estoqueRepository.buscarAjusteEstoqueItem(ajusteEstoqueItem.getId())).thenReturn(ajusteEstoqueItem);

        var ajusteEstoqueItemEsperado = service.buscarAjusteEstoqueItemPorId(ajusteEstoqueItem.getId());

        Assertions.assertThat(ajusteEstoqueItemEsperado).isEqualTo(ajusteEstoqueItem);
    }

    @Test
    @Order(11)
    void buscarAjusteEstoqueItemPorId_LancaExcecaoIllegalArgumentException_QuandoIdNaoEncontrado() {
        var ajusteEstoqueItem = estoqueItensList.getFirst();

        BDDMockito.when(estoqueRepository.buscarAjusteEstoqueItem(ajusteEstoqueItem.getId())).thenThrow(IllegalArgumentException.class);

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarAjusteEstoqueItemPorId(ajusteEstoqueItem.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Order(12)
    void buscarAjusteEstoqueItensEntrada_RetornaListaAjusteEstoqueItens_QuandoForBemSucedido() {
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(estoqueRepository.buscarAjusteEstoqueItensEntrada(ajusteEstoqueId)).thenReturn(estoqueItensList);

        var ajusteEstoqueItensList = service.buscarAjusteEstoqueItensEntrada(ajusteEstoqueId);

        Assertions.assertThat(ajusteEstoqueItensList).hasSize(estoqueItensList.size());
    }

    @Test
    @Order(12)
    void buscarAjusteEstoqueItensEntrada_RetornaListaVaziaAjusteEstoqueItens() {
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(estoqueRepository.buscarAjusteEstoqueItensEntrada(ajusteEstoqueId)).thenReturn(List.of());

        var ajusteEstoqueItensList = service.buscarAjusteEstoqueItensEntrada(ajusteEstoqueId);

        Assertions.assertThat(ajusteEstoqueItensList).isEmpty();
    }

    @Test
    @Order(13)
    void buscarAjusteEstoqueItensSaida_RetornaListaAjusteEstoqueItens_QuandoForBemSucedido() {
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(estoqueRepository.buscarAjusteEstoqueItensSaida(ajusteEstoqueId)).thenReturn(estoqueItensList);

        var ajusteEstoqueItensList = service.buscarAjusteEstoqueItensSaida(ajusteEstoqueId);

        Assertions.assertThat(ajusteEstoqueItensList).hasSize(estoqueItensList.size());
    }

    @Test
    @Order(14)
    void buscarAjusteEstoqueItensSaida_RetornaListaVaziaAjusteEstoqueItens() {
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(estoqueRepository.buscarAjusteEstoqueItensSaida(ajusteEstoqueId)).thenReturn(List.of());

        var ajusteEstoqueItensList = service.buscarAjusteEstoqueItensSaida(ajusteEstoqueId);

        Assertions.assertThat(ajusteEstoqueItensList).isEmpty();
    }
}