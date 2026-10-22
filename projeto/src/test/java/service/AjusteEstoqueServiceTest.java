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
import util.mensagens.MensagensAjusteEstoque;
import util.mensagens.MensagensMovimentos;

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
        var computador = Produto.builder().id(1L).descricao("computador").precoCusto(new BigDecimal("999")).precoVenda(new BigDecimal("1500")).build();
        var notebook = Produto.builder().id(2L).descricao("notebook").precoCusto(new BigDecimal("1200")).precoVenda(new BigDecimal("2000")).build();
        var mouse = Produto.builder().id(3L).descricao("mouse logitech").precoCusto(new BigDecimal("399")).precoVenda(new BigDecimal("800")).build();

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
                .withMessage(MensagensAjusteEstoque.ERRO_INSERIR_AJUSTE_ESTOQUE_ITENS);

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
    void buscarAjustePorStatus_RetornaListaAjusteEstoque_QuandoForBemSucedido() throws SQLException {
        var status = Status.ABERTO;

        BDDMockito.when(ajusteEstoqueRepository.buscarAjustePorStatus(status)).thenReturn(List.of(ajusteEstoque));

        var ajusteEstoquesList = service.buscarAjustePorStatus(status);

        Assertions.assertThat(ajusteEstoquesList).isEqualTo(List.of(ajusteEstoque));
    }

    @Test
    @Order(5)
    void buscarAjustePorStatus_RetornaListaAjusteEstoqueVazia() throws SQLException {
        var status = Status.ABERTO;

        BDDMockito.when(ajusteEstoqueRepository.buscarAjustePorStatus(status)).thenReturn(List.of());

        var ajusteEstoquesList = service.buscarAjustePorStatus(status);

        Assertions.assertThat(ajusteEstoquesList).isEmpty();
    }

    @Test
    @Order(6)
    void buscarTodosAjusteAberto_RetornaListaAjusteEstoqueAbertoEstoque_QuandoForBemSucedido() throws SQLException {
        BDDMockito.when(ajusteEstoqueRepository.buscarTodosAjusteEstoqueAberto()).thenReturn(List.of(ajusteEstoque));

        var ajusteEstoqueList = service.buscarTodosAjusteEstoqueAberto();

        Assertions.assertThat(ajusteEstoqueList).isEqualTo(List.of(ajusteEstoque));
    }

    @Test
    @Order(7)
    void buscarTodosAjusteAberto_RetornaListaVaziaAjusteEstoqueAbertoEstoque() throws SQLException {
        BDDMockito.when(ajusteEstoqueRepository.buscarTodosAjusteEstoqueAberto()).thenReturn(List.of());

        var ajusteEstoqueList = service.buscarTodosAjusteEstoqueAberto();

        Assertions.assertThat(ajusteEstoqueList).isEmpty();
    }

    @Test
    @Order(8)
    void buscarAjustePorId_RetornaAjusteEstoque_QuandoForBemSucedido() throws SQLException {
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoquePorId(ajusteEstoqueId)).thenReturn(ajusteEstoque);

        var ajusteEstoqueEsperado = service.buscarAjustePorId(ajusteEstoqueId);

        Assertions.assertThat(ajusteEstoqueEsperado).isEqualTo(ajusteEstoque);
    }

    @Test
    @Order(9)
    void buscarAjustePorId_LancaExcecaoIllegalArgumentException_QuandoIdNaoEncontrado() throws SQLException {
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoquePorId(ajusteEstoqueId)).thenThrow(IllegalArgumentException.class);

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarAjustePorId(ajusteEstoqueId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Order(10)
    void buscarAjusteEstoqueItensEntrada_RetornaListaAjusteEstoqueItens_QuandoForBemSucedido() throws SQLException{
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensEntrada(ajusteEstoqueId)).thenReturn(estoqueItensList);

        var ajusteEstoqueItensList = service.buscarAjusteEstoqueItensEntrada(ajusteEstoqueId);

        Assertions.assertThat(ajusteEstoqueItensList).hasSize(estoqueItensList.size());
    }

    @Test
    @Order(11)
    void buscarAjusteEstoqueItensEntrada_RetornaListaVaziaAjusteEstoqueItens() throws SQLException {
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensEntrada(ajusteEstoqueId)).thenReturn(List.of());

        var ajusteEstoqueItensList = service.buscarAjusteEstoqueItensEntrada(ajusteEstoqueId);

        Assertions.assertThat(ajusteEstoqueItensList).isEmpty();
    }

    @Test
    @Order(12)
    void buscarAjusteEstoqueItensSaida_RetornaListaAjusteEstoqueItens_QuandoForBemSucedido() throws SQLException {
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensSaida(ajusteEstoqueId)).thenReturn(estoqueItensList);

        var ajusteEstoqueItensList = service.buscarAjusteEstoqueItensSaida(ajusteEstoqueId);

        Assertions.assertThat(ajusteEstoqueItensList).hasSize(estoqueItensList.size());
    }

    @Test
    @Order(13)
    void buscarAjusteEstoqueItensSaida_RetornaListaVaziaAjusteEstoqueItens() throws SQLException{
        var ajusteEstoqueId = ajusteEstoque.getId();

        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensSaida(ajusteEstoqueId)).thenReturn(List.of());

        var ajusteEstoqueItensList = service.buscarAjusteEstoqueItensSaida(ajusteEstoqueId);

        Assertions.assertThat(ajusteEstoqueItensList).isEmpty();
    }

    @Test
    @Order(14)
    void criarMovimentoAjusteEstoque_QuandoForBemSucedido() throws SQLException {
        var computador = estoqueItensList.get(0).getProduto();
        var notebook = estoqueItensList.get(1).getProduto();
        var mouse = estoqueItensList.get(2).getProduto();
        var pessoaPadrao = Pessoa.builder().id(Pessoa.ID_PESSOA_PADRAO).build();
        var id = 1L;

        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensEntrada(id)).thenReturn(estoqueItensList);
        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensSaida(id)).thenReturn(List.of());

        BDDMockito.when(produtoRepository.buscarPorId(1L)).thenReturn(computador);
        BDDMockito.when(produtoRepository.buscarPorId(2L)).thenReturn(notebook);
        BDDMockito.when(produtoRepository.buscarPorId(3L)).thenReturn(mouse);

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

    @Test
    @Order(15)
    void criarMovimentoAjusteEstoque_QuandoAjusteEstoqueItensListForVazio() throws SQLException {
        var id = 1L;

        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensEntrada(id)).thenReturn(List.of());
        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensSaida(id)).thenReturn(List.of());

        service.criarMovimentoAjusteEstoque(id);


        BDDMockito.verify(connection).commit();
        BDDMockito.verify(connection).close();
        BDDMockito.verify(connection, BDDMockito.never()).rollback();
    }

    @Test
    @Order(16)
    void criarMovimentoAjusteEstoque_DeveLancarExcecao() throws SQLException {
        var computador = estoqueItensList.get(0).getProduto();
        var notebook = estoqueItensList.get(1).getProduto();
        var mouse = estoqueItensList.get(2).getProduto();
        var pessoaPadrao = Pessoa.builder().id(Pessoa.ID_PESSOA_PADRAO).build();
        var id = 1L;

        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensEntrada(id)).thenReturn(estoqueItensList);
        BDDMockito.when(ajusteEstoqueRepository.buscarAjusteEstoqueItensSaida(id)).thenReturn(List.of());

        BDDMockito.when(produtoRepository.buscarPorId(1L)).thenReturn(computador);
        BDDMockito.when(produtoRepository.buscarPorId(2L)).thenReturn(notebook);
        BDDMockito.when(produtoRepository.buscarPorId(3L)).thenReturn(mouse);

        BDDMockito.when(pessoaRepository.buscarPorId(Pessoa.ID_PESSOA_PADRAO)).thenReturn(pessoaPadrao);

        BDDMockito.when(movimentoRepository
                .inserirMovimento(BDDMockito.eq(connection), BDDMockito.any(Movimento.class))).thenReturn(100);

        BDDMockito.when(movimentoItemRepository
                        .inserirMovimentoItem(BDDMockito.eq(connection), BDDMockito.eq(100), BDDMockito.any(MovimentoItem.class)))
                .thenReturn(false);

        Assertions.assertThatException()
                        .isThrownBy(() -> service.criarMovimentoAjusteEstoque(id))
                                .isInstanceOf(IllegalArgumentException.class);

        BDDMockito.verify(movimentoRepository).inserirMovimento(BDDMockito.eq(connection), BDDMockito.any(Movimento.class));

        BDDMockito.verify(connection, BDDMockito.never()).commit();
        BDDMockito.verify(connection).rollback();
        BDDMockito.verify(connection).close();
    }

    @Test
    @Order(17)
    void removerAjusteEstoque_QuandoForBemSucedido() throws SQLException{
        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);

        Assertions.assertThatNoException()
                .isThrownBy(() -> service.removerAjusteEstoque(ajusteEstoque));

        BDDMockito.verify(ajusteEstoqueRepository).removerAjusteEstoqueItem(connection, ajusteEstoque);
        BDDMockito.verify(ajusteEstoqueRepository).removerAjusteEstoque(connection, ajusteEstoque);

        BDDMockito.verify(connection).commit();
        BDDMockito.verify(connection).close();
    }

    @Test
    @Order(18)
    void removerAjusteEstoque_removerAjusteEstoqueItem_LancaExcecaoRuntimeException() throws SQLException{
        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);

        BDDMockito.doThrow(SQLException.class).when(ajusteEstoqueRepository).removerAjusteEstoqueItem(connection, ajusteEstoque);;

        Assertions.assertThatException()
                .isThrownBy(() -> service.removerAjusteEstoque(ajusteEstoque))
                .isInstanceOf(RuntimeException.class)
                .withMessage(MensagensMovimentos.ERRO_INSERIR_MOVIMENTO);


        BDDMockito.verify(connection).rollback();
        BDDMockito.verify(connection).close();
    }

    @Test
    @Order(19)
    void removerAjusteEstoque_removerAjusteEstoque_LancaExcecaoRuntimeException() throws SQLException{
        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);

        BDDMockito.doThrow(SQLException.class).when(ajusteEstoqueRepository).removerAjusteEstoque(connection, ajusteEstoque);;

        Assertions.assertThatException()
                .isThrownBy(() -> service.removerAjusteEstoque(ajusteEstoque))
                .isInstanceOf(RuntimeException.class)
                .withMessage(MensagensMovimentos.ERRO_INSERIR_MOVIMENTO);

        BDDMockito.verify(connection).rollback();
        BDDMockito.verify(connection).close();
    }

    @Test
    @Order(20)
    void atualizarAjusteEstoque_QuandoForBemSucedido() throws SQLException {
        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.doNothing().when(ajusteEstoqueRepository).atualizarAjusteEstoque(
                connection, ajusteEstoque);
        BDDMockito.doNothing().when(ajusteEstoqueRepository).removerAjusteEstoqueItem(
                connection, ajusteEstoque);
        BDDMockito.when(ajusteEstoqueItemRepository.inserirAjusteEstoqueItens(
                connection, ajusteEstoque.getId(), estoqueItensList)).thenReturn(true);

        service.atualizarAjusteEstoque(ajusteEstoque);

        BDDMockito.verify(connection).commit();
        BDDMockito.verify(connection).close();
    }

    @Test
    @Order(21)
    void atualizarAjusteEstoque_LancaExcecaoIllegalArgumentException() throws SQLException {
        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.doNothing().when(ajusteEstoqueRepository).atualizarAjusteEstoque(
                connection, ajusteEstoque);
        BDDMockito.doNothing().when(ajusteEstoqueRepository).removerAjusteEstoqueItem(
                connection, ajusteEstoque);
        BDDMockito.when(ajusteEstoqueItemRepository.inserirAjusteEstoqueItens(
                connection, ajusteEstoque.getId(), estoqueItensList)).thenReturn(false);

        Assertions.assertThatException()
                .isThrownBy(() -> service.atualizarAjusteEstoque(ajusteEstoque))
                .isInstanceOf(IllegalArgumentException.class)
                .withMessage("\nErro ao inserir item do Ajuste Estoque!\n");

        BDDMockito.verify(connection).rollback();
        BDDMockito.verify(connection).close();
    }

    @Test
    @Order(22)
    void atualizarAjusteEstoque_atualizarAjusteEstoque_LancaExcecaoSQLException() throws SQLException {
        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.doThrow(SQLException.class)
                        .when(ajusteEstoqueRepository).atualizarAjusteEstoque(
                        connection, ajusteEstoque);

        Assertions.assertThatException()
                .isThrownBy(() -> service.atualizarAjusteEstoque(ajusteEstoque))
                .isInstanceOf(RuntimeException.class)
                .withMessage(MensagensMovimentos.ERRO_INSERIR_MOVIMENTO);

        BDDMockito.verify(connection).rollback();
        BDDMockito.verify(connection).close();
    }

    @Test
    @Order(23)
    void atualizarAjusteEstoque_removerAjusteEstoqueItem_LancaExcecaoSQLException() throws SQLException {
        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.doThrow(SQLException.class)
                .when(ajusteEstoqueRepository).removerAjusteEstoqueItem(
                        connection, ajusteEstoque);

        Assertions.assertThatException()
                .isThrownBy(() -> service.atualizarAjusteEstoque(ajusteEstoque))
                .isInstanceOf(RuntimeException.class)
                .withMessage(MensagensMovimentos.ERRO_INSERIR_MOVIMENTO);

        BDDMockito.verify(connection).rollback();
        BDDMockito.verify(connection).close();
    }

    @Test
    @Order(24)
    void atualizarAjusteEstoque_inserirAjusteEstoqueItens_LancaExcecaoIllegalArgumentException() throws SQLException {
        BDDMockito.when(connectionProvider.getConnection()).thenReturn(connection);
        BDDMockito.when(ajusteEstoqueItemRepository.inserirAjusteEstoqueItens(
                        connection,
                        ajusteEstoque.getId(),
                        ajusteEstoque.getAjusteEstoqueItens())
        ).thenReturn(false);

        Assertions.assertThatException()
                .isThrownBy(() -> service.atualizarAjusteEstoque(ajusteEstoque))
                .isInstanceOf(IllegalArgumentException.class)
                .withMessage(MensagensAjusteEstoque.ERRO_INSERIR_AJUSTE_ESTOQUE_ITENS);

        BDDMockito.verify(connection).rollback();
        BDDMockito.verify(connection).close();
    }
}