package service;

import conn.ConnectionProvider;
import domain.ajusteestoque.AjusteEstoque;
import domain.ajusteestoque.AjusteEstoqueItens;
import domain.ajusteestoque.Status;
import domain.estoque.Estoque;
import domain.produto.Produto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.AjusteEstoqueItemRepository;
import repository.AjusteEstoqueRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AjusteEstoqueServiceTest {
    @Mock
    private AjusteEstoqueRepository estoqueRepository;

    @Mock
    private AjusteEstoqueItemRepository ajusteEstoqueItemRepository;

    @Mock
    private ConnectionProvider connectionProvider;

    @Mock
    private Connection connection;

    @InjectMocks
    private AjusteEstoqueService service;

    @Test
    void deveInserirAjusteEstoqueComSucesso() throws SQLException {

        AjusteEstoqueItens item = AjusteEstoqueItens.builder()
                .produto(Produto.builder().id(1).descricao("teste").precoVenda(new BigDecimal("9.99")).build())
                .estoque(Estoque.builder().idProduto(1).build())
                .contagem(new BigDecimal("1"))
                .build();

        AjusteEstoque ajuste = AjusteEstoque.builder()
                .titulo("teste")
                .status(Status.ABERTO)
                .ajusteEstoqueItens(List.of(item))
                .build();

        when(connectionProvider.getConnection())
                .thenReturn(connection);

        when(estoqueRepository.inserirAjusteEstoque(connection, ajuste))
                .thenReturn(10L);

        when(ajusteEstoqueItemRepository.inserirAjusteEstoqueItem(
                connection, 10L, item))
                .thenReturn(true);

        service.inserirAjusteEstoque(ajuste);

        verify(connection).setAutoCommit(false);

        verify(estoqueRepository)
                .inserirAjusteEstoque(connection, ajuste);

        verify(ajusteEstoqueItemRepository)
                .inserirAjusteEstoqueItem(connection, 10L, item);

        verify(connection).commit();

        verify(connection).close();
    }

    @Test
    void deveLancarExcecaoQuandoNaoInserirItem() throws SQLException {

        AjusteEstoqueItens item = AjusteEstoqueItens.builder()
                .produto(Produto.builder().id(1).descricao("teste").precoVenda(new BigDecimal("9.99")).build())
                .estoque(Estoque.builder().idProduto(1).build())
                .contagem(new BigDecimal("1"))
                .build();

        AjusteEstoque ajuste = AjusteEstoque.builder()
                .titulo("teste")
                .status(Status.ABERTO)
                .ajusteEstoqueItens(List.of(item))
                .build();

        when(connectionProvider.getConnection())
                .thenReturn(connection);

        when(estoqueRepository.inserirAjusteEstoque(connection, ajuste))
                .thenReturn(10L);

        when(ajusteEstoqueItemRepository.inserirAjusteEstoqueItem(
                connection, 10L, item))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.inserirAjusteEstoque(ajuste)
        );

        assertEquals(
                "Erro ao inserir item do Ajuste Estoque",
                exception.getMessage()
        );

        verify(connection).rollback();
        verify(connection).close();
    }
}