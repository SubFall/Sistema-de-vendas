package service;

import conn.ConnectionProvider;
import domain.ajusteestoque.AjusteEstoque;
import domain.ajusteestoque.AjusteEstoqueItens;
import domain.ajusteestoque.Status;
import domain.estoque.Estoque;
import domain.movimento.Movimento;
import domain.movimento.MovimentoItem;
import domain.movimento.Tipo;
import domain.pessoa.Pessoa;
import domain.produto.Produto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class AjusteEstoqueServiceTest {
    @Mock
    private AjusteEstoqueRepository ajusteEstoqueRepository;

    @Mock
    private AjusteEstoqueItemRepository ajusteEstoqueItemRepository;

    @Mock
    private ConnectionProvider connectionProvider;

    @Mock
    private Connection connection;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private MovimentoRepository movimentoRepository;

    @Mock
    private MovimentoItemRepository movimentoItemRepository;

    @Mock
    private HistoricoEstoqueService historicoEstoqueService;

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
        BDDMockito.when(ajusteEstoqueRepository.inserirAjusteEstoque(connection, ajusteEstoque)).thenReturn(idAjuste);
        BDDMockito.when(ajusteEstoqueItemRepository.inserirAjusteEstoqueItens(connection, idAjuste, estoqueItensList)).thenReturn(true);

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
        BDDMockito.when(ajusteEstoqueRepository.inserirAjusteEstoque(connection, ajusteEstoque)).thenReturn(idAjuste);
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
    void inserirAjusteEstoque_LancarExcecaoSQLException() throws SQLException {
        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.when(ajusteEstoqueRepository.inserirAjusteEstoque(connection, ajusteEstoque)).thenThrow(SQLException.class);

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

        BDDMockito.when(ajusteEstoqueRepository.buscarAjustePorStatus(status)).thenReturn(List.of(ajusteEstoque));

        var ajusteEstoquesList = service.buscarAjustePorStatus(status);

        Assertions.assertThat(ajusteEstoquesList).isEqualTo(List.of(ajusteEstoque));
    }

    @Test
    @Order(5)
    void buscarAjustePorStatus_RetornaListaAjusteEstoqueVazia() {
        var status = Status.ABERTO;

        BDDMockito.when(ajusteEstoqueRepository.buscarAjustePorStatus(status)).thenReturn(List.of());

        var ajusteEstoquesList = service.buscarAjustePorStatus(status);

        Assertions.assertThat(ajusteEstoquesList).isEmpty();
    }

    @Test
    @Order(6)
    void buscarTodosAjuste_RetornaListaAjusteEstoque_QuandoForBemSucedido() {
        BDDMockito.when(ajusteEstoqueRepository.buscarTodosAjuste()).thenReturn(List.of(ajusteEstoque));

        var ajusteEstoqueList = service.buscarTodosAjuste();

        Assertions.assertThat(ajusteEstoqueList).isEqualTo(List.of(ajusteEstoque));
    }

    @Test
    @Order(7)
    void buscarTodosAjuste_RetornaListaVaziaAjusteEstoque() {
        BDDMockito.when(ajusteEstoqueRepository.buscarTodosAjuste()).thenReturn(List.of());

        var ajusteEstoqueList = service.buscarTodosAjuste();

        Assertions.assertThat(ajusteEstoqueList).isEmpty();
    }

    @Test
    @Order(8)
    void buscarAjustePorId_RetornaAjusteEstoque_QuandoForBemSucedido() {
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(ajusteEstoqueRepository.buscarAjustePorId(ajusteEstoqueId)).thenReturn(ajusteEstoque);

        var ajusteEstoqueEsperado = service.buscarAjustePorId(ajusteEstoqueId);

        Assertions.assertThat(ajusteEstoqueEsperado).isEqualTo(ajusteEstoque);
    }

    @Test
    @Order(9)
    void buscarAjustePorId_LancaExcecaoIllegalArgumentException_QuandoIdNaoEncontrado() {
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(ajusteEstoqueRepository.buscarAjustePorId(ajusteEstoqueId)).thenThrow(IllegalArgumentException.class);

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarAjustePorId(ajusteEstoqueId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Order(10)
    void buscarAjusteEstoqueItemPorId_RetornaAjusteEstoqueItens_QuandoForBemSucedido() {
        var ajusteEstoqueItem = estoqueItensList.getFirst();

        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItem(ajusteEstoqueItem.getId())).thenReturn(ajusteEstoqueItem);

        var ajusteEstoqueItemEsperado = service.buscarAjusteEstoqueItemPorId(ajusteEstoqueItem.getId());

        Assertions.assertThat(ajusteEstoqueItemEsperado).isEqualTo(ajusteEstoqueItem);
    }

    @Test
    @Order(11)
    void buscarAjusteEstoqueItemPorId_LancaExcecaoIllegalArgumentException_QuandoIdNaoEncontrado() {
        var ajusteEstoqueItem = estoqueItensList.getFirst();

        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItem(ajusteEstoqueItem.getId())).thenThrow(IllegalArgumentException.class);

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarAjusteEstoqueItemPorId(ajusteEstoqueItem.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Order(12)
    void buscarAjusteEstoqueItensEntrada_RetornaListaAjusteEstoqueItens_QuandoForBemSucedido() {
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensEntrada(ajusteEstoqueId)).thenReturn(estoqueItensList);

        var ajusteEstoqueItensList = service.buscarAjusteEstoqueItensEntrada(ajusteEstoqueId);

        Assertions.assertThat(ajusteEstoqueItensList).hasSize(estoqueItensList.size());
    }

    @Test
    @Order(12)
    void buscarAjusteEstoqueItensEntrada_RetornaListaVaziaAjusteEstoqueItens() {
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensEntrada(ajusteEstoqueId)).thenReturn(List.of());

        var ajusteEstoqueItensList = service.buscarAjusteEstoqueItensEntrada(ajusteEstoqueId);

        Assertions.assertThat(ajusteEstoqueItensList).isEmpty();
    }

    @Test
    @Order(13)
    void buscarAjusteEstoqueItensSaida_RetornaListaAjusteEstoqueItens_QuandoForBemSucedido() {
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensSaida(ajusteEstoqueId)).thenReturn(estoqueItensList);

        var ajusteEstoqueItensList = service.buscarAjusteEstoqueItensSaida(ajusteEstoqueId);

        Assertions.assertThat(ajusteEstoqueItensList).hasSize(estoqueItensList.size());
    }

    @Test
    @Order(14)
    void buscarAjusteEstoqueItensSaida_RetornaListaVaziaAjusteEstoqueItens() {
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensSaida(ajusteEstoqueId)).thenReturn(List.of());

        var ajusteEstoqueItensList = service.buscarAjusteEstoqueItensSaida(ajusteEstoqueId);

        Assertions.assertThat(ajusteEstoqueItensList).isEmpty();
    }

    @Test
    @Order(15)
    void criarMovimentoAjusteEstoque_QuandoForBemSucedido() throws SQLException {
        var computador = estoqueItensList.get(0).getProduto();
        var notebook = estoqueItensList.get(1).getProduto();
        var mouse = estoqueItensList.get(2).getProduto();
        var pessoaPadrao = Pessoa.builder().id(Pessoa.ID_PESSOA_PADRAO).build();
        var id = 1L;

        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensEntrada(id)).thenReturn(estoqueItensList);
        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensSaida(id)).thenReturn(List.of());

        BDDMockito.when(produtoRepository.buscarPorId(1)).thenReturn(computador);
        BDDMockito.when(produtoRepository.buscarPorId(2)).thenReturn(notebook);
        BDDMockito.when(produtoRepository.buscarPorId(3)).thenReturn(mouse);

        BDDMockito.when(pessoaRepository.buscarPorId(Pessoa.ID_PESSOA_PADRAO)).thenReturn(pessoaPadrao);

        BDDMockito.when(movimentoRepository
                .inserirMovimento(BDDMockito.eq(connection), BDDMockito.any(Movimento.class))).thenReturn(100);

        BDDMockito.when(movimentoItemRepository
                        .inserirMovimentoItem(BDDMockito.eq(connection), BDDMockito.eq(100), BDDMockito.any(MovimentoItem.class)))
                .thenReturn(true);

        service.criarMovimentoAjusteEstoque(id);

        BDDMockito.verify(movimentoRepository).inserirMovimento(BDDMockito.eq(connection), BDDMockito.any(Movimento.class));

        BDDMockito.verify(movimentoItemRepository, BDDMockito.times(3))
                .inserirMovimentoItem(BDDMockito.eq(connection), BDDMockito.eq(100), BDDMockito.any(MovimentoItem.class));

        BDDMockito.verify(historicoEstoqueService, BDDMockito.times(3))
                .movimentar(BDDMockito.eq(connection),
                        BDDMockito.any(Produto.class),
                        BDDMockito.any(Movimento.class),
                        BDDMockito.any(BigDecimal.class),
                        BDDMockito.any(Tipo.class));

        BDDMockito.verify(connection).commit();
        BDDMockito.verify(connection).close();
        BDDMockito.verify(connection, BDDMockito.never()).rollback();
    }
//    @Test
//    @Order(15)
//    void criarMovimentoAjusteEstoque_QuandoForBemSucedido() throws SQLException {
//        var ajusteEstoque = this.ajusteEstoque;
//
//        BDDMockito.when(estoqueRepository.buscarAjusteEstoqueItensEntrada(ajusteEstoque.getId())).thenReturn(estoqueItensList);
//        BDDMockito.when(estoqueRepository.buscarAjusteEstoqueItensSaida(ajusteEstoque.getId())).thenReturn(estoqueItensList);
//        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
//
//
//    }
}