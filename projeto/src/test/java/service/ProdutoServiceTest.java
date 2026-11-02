package service;

import domain.categoria.Categoria;
import domain.produto.Produto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.CategoriaRepository;
import repository.ProdutoRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {
    @InjectMocks
    private ProdutoService service;

    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private CategoriaRepository categoriaRepository;

    private List<Produto> produtoList;

    @BeforeEach
    void init() {
        var informaticaCategoria = Categoria.builder().id(2L).descricao("Informática").ativo(true).build();

        var processadorProduto = Produto.builder().id(1L).descricao("Processador").precoVenda(new BigDecimal("999.00"))
                .precoCusto(new BigDecimal("699.99")).ativo(true).categoria(informaticaCategoria).build();

        var tecladoProduto = Produto.builder().id(2L).descricao("Teclado").precoVenda(new BigDecimal("250.99"))
                .precoCusto(new BigDecimal("109.90")).ativo(true).categoria(informaticaCategoria).build();

        var monitorProduto = Produto.builder().id(3L).descricao("Monitor").precoVenda(new BigDecimal("1250.99"))
                .precoCusto(new BigDecimal("700")).ativo(true).categoria(informaticaCategoria).build();

        var vassouraProduto = Produto.builder().id(3L).descricao("Vassoura").precoVenda(new BigDecimal("30"))
                .precoCusto(new BigDecimal("17.50")).ativo(false).build();

        produtoList = new ArrayList<>(List.of(processadorProduto, tecladoProduto, monitorProduto, vassouraProduto));
    }

    @Test
    void inserirProduto_QuandoForBemSucedido_RetornaTrue() {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.inserirProduto(produto))
                .thenReturn(true);

        var isInseriu = service.inserirProduto(produto);

        Assertions.assertThat(isInseriu).isTrue();

        BDDMockito.verify(produtoRepository).inserirProduto(produto);
    }

    @Test
    void inserirProduto_QuandoRepositorioLancaExcecao_RetornaFalse() {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.inserirProduto(produto))
                .thenThrow(new RuntimeException("Falha ao inserir produto!"));

        var isInseriu = service.inserirProduto(produto);

        Assertions.assertThat(isInseriu).isFalse();
    }

    @Test
    void atualizarStatusProduto_QuandoForBemSucedido() {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.atualizarStatusProduto(produto))
                .thenReturn(true);

        var isAtualizou = service.atualizarStatusProduto(produto);

        Assertions.assertThat(isAtualizou).isTrue();

        BDDMockito.verify(produtoRepository).atualizarStatusProduto(produto);
    }

    @Test
    void atualizarStatusProduto_Falha_RetornaFalse() {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.atualizarStatusProduto(produto))
                .thenThrow(new RuntimeException("Falha ao atualizar Status!"));

        var isAtualizou = service.atualizarStatusProduto(produto);

        Assertions.assertThat(isAtualizou).isFalse();
    }

    @Test
    void atualizarProduto_QuandoForBemSucedido() {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produto.getId())).thenReturn(produto);

        BDDMockito.when(produtoRepository.atualizarProduto(produto)).thenReturn(true);

        var isAtualizou = service.atualizarProduto(produto);

        Assertions.assertThat(isAtualizou).isTrue();

        BDDMockito.verify(produtoRepository).atualizarProduto(produto);
    }

    @Test
    void atualizarProduto_BuscarPorIdFalha_RetornaFalse() {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produto.getId()))
                .thenThrow(new RuntimeException("Falha ao buscar Produto!"));

        var isAtualizou = service.atualizarProduto(produto);

        Assertions.assertThat(isAtualizou).isFalse();

        BDDMockito.verify(produtoRepository, BDDMockito.never())
                .atualizarProduto(produto);
    }

    @Test
    void buscarTodos_QuandoFoBemSucedido() {
        BDDMockito.when(produtoRepository.buscarTodos()).thenReturn(produtoList);

        var produtoListExpectd = service.buscarTodos();

        Assertions.assertThat(produtoListExpectd).isNotNull().isEqualTo(produtoList);
    }

    @Test
    void buscarTodos_RetornaListaVazia() {
        BDDMockito.when(produtoRepository.buscarTodos()).thenReturn(List.of());

        var listProdutoEmpty = service.buscarTodos();

        Assertions.assertThat(listProdutoEmpty).isEmpty();
    }

    @Test
    void buscarTodos_Falha_buscarTodosLancaExcecao_RetornaListaVazia() {
        BDDMockito.when(produtoRepository.buscarTodos())
                .thenThrow(new RuntimeException("Falha ao buscar Produtos!"));

        var listProdutoEmpty = service.buscarTodos();

        Assertions.assertThat(listProdutoEmpty).isEmpty();
    }

    @Test
    void buscarTodosAtivos_QuandoForBemSucedido() {
        BDDMockito.when(produtoRepository.buscarTodosPorStatus(true)).thenReturn(produtoList);

        var listProdutosAtivos = service.buscarTodosAtivos();

        Assertions.assertThat(listProdutosAtivos).isNotNull().isEqualTo(produtoList);
    }

    @Test
    void buscarTodosAtivos_buscarTodosPorStatusLancaExcecao_RetornaListaVazia() {
        BDDMockito.when(produtoRepository.buscarTodosPorStatus(true))
                .thenThrow(new RuntimeException("Falha ao buscar Produtos por Status!"));

        var listProdutosEmpty = service.buscarTodosAtivos();

        Assertions.assertThat(listProdutosEmpty).isEmpty();
    }

    @Test
    void buscarTodosAtivos_RetornaListaVazia() {
        BDDMockito.when(produtoRepository.buscarTodosPorStatus(true)).thenReturn(List.of());

        var listProdutosEmpty = service.buscarTodosAtivos();

        Assertions.assertThat(listProdutosEmpty).isEmpty();
    }

    @Test
    void buscarTodosInativos_QuandoForBemSucedido() {
        BDDMockito.when(produtoRepository.buscarTodosPorStatus(false)).thenReturn(produtoList);

        var listProdutosInativos = service.buscarTodosInativos();

        Assertions.assertThat(listProdutosInativos).isNotNull().isEqualTo(produtoList);
    }

    @Test
    void buscarTodosIntivos_buscarTodosPorStatusLancaExcecao_RetornaListaVazia() {
        BDDMockito.when(produtoRepository.buscarTodosPorStatus(false))
                .thenThrow(new RuntimeException("Falha ao buscar Produtos por Status!"));

        var listProdutosInativosEmpty = service.buscarTodosInativos();

        Assertions.assertThat(listProdutosInativosEmpty).isEmpty();
    }

    @Test
    void buscarTodosInativos_RetornaListaVazia() {
        BDDMockito.when(produtoRepository.buscarTodosPorStatus(false)).thenReturn(List.of());

        var listProdutosInativosEmpty = service.buscarTodosInativos();

        Assertions.assertThat(listProdutosInativosEmpty).isEmpty();
    }

    @Test
    void buscarPorId_QuandoForBemSucedido() {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produto.getId())).thenReturn(produto);

        var expectedProduto = service.buscarPorId(produto.getId());

        Assertions.assertThat(expectedProduto).isNotNull().isEqualTo(produto);
    }

    @Test
    void buscarPorId_RetornaNull() {
        var id = 99L;

        BDDMockito.when(produtoRepository.buscarPorId(id)).thenReturn(null);

        var produtoIsNull = service.buscarPorId(id);

        Assertions.assertThat(produtoIsNull).isNull();
    }

    @Test
    void buscarPorId_BuscarPorIdLancaExecao_RetornaNull() {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produto.getId()))
                .thenThrow(new RuntimeException("Falha ao buscar Produtos!"));

        var produtoIsNull = service.buscarPorId(produto.getId());

        Assertions.assertThat(produtoIsNull).isNull();
    }

    @Test
    void buscarPorIdAtivo_QuandoForBemSucedido() {
        var produtoAtivo = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produtoAtivo.getId())).thenReturn(produtoAtivo);

        var produtoAtivoExpected = service.buscarPorIdAtivo(produtoAtivo.getId());

        Assertions.assertThat(produtoAtivoExpected).isNotNull().isEqualTo(produtoAtivo);
    }

    @Test
    void buscarPorIdAtivo_RetornaNull() {
        var id = 99L;

        BDDMockito.when(produtoRepository.buscarPorId(id)).thenReturn(null);

        var produtoIsNull = service.buscarPorIdAtivo(id);

        Assertions.assertThat(produtoIsNull).isNull();
    }

    @Test
    void buscarPorIdAtivo_BuscarPorIdLancaExcecao_RetornaNull() {
        var produtoAtivo = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produtoAtivo.getId()))
                .thenThrow(new IllegalArgumentException("Falha ao buscar Produtos!"));

        var produtoIsNull = service.buscarPorIdAtivo(produtoAtivo.getId());

        Assertions.assertThat(produtoIsNull).isNull();
    }

    @Test
    void buscarPorIdAtivo_ValidarProdutoAtivoLancaExcecao_RetornaNull() {
        var produtoAtivo = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produtoAtivo.getId()))
                .thenThrow(new RuntimeException("Produto está inativo!"));

        var produtoIsNull = service.buscarPorIdAtivo(produtoAtivo.getId());

        Assertions.assertThat(produtoIsNull).isNull();
    }

    @Test
    void buscarPorIdInativo_QuandoForBemSucedido() {
        var produtoInativo = produtoList.getLast();

        BDDMockito.when(produtoRepository.buscarPorId(produtoInativo.getId())).thenReturn(produtoInativo);

        var produtoInativoExpected = service.buscarPorIdInativo(produtoInativo.getId());

        Assertions.assertThat(produtoInativoExpected).isNotNull().isEqualTo(produtoInativo);
    }

    @Test
    void buscarPorIdInativo_RetornaNull() {
        var id = 99L;

        BDDMockito.when(produtoRepository.buscarPorId(id)).thenReturn(null);

        var produtoIsNull = service.buscarPorIdInativo(id);

        Assertions.assertThat(produtoIsNull).isNull();
    }

    @Test
    void buscarPorIdInativo_BuscarPorIdLancaExcecao_RetornaNull() {
        var produtoInativo = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produtoInativo.getId()))
                .thenThrow(new IllegalArgumentException("Falha ao buscar Produtos!"));

        var produtoIsNull = service.buscarPorIdInativo(produtoInativo.getId());

        Assertions.assertThat(produtoIsNull).isNull();
    }

    @Test
    void buscarPorIdInativo_ValidarProdutoInativoLancaExcecao_RetornaNull() {
        var produtoInativo = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produtoInativo.getId()))
                .thenThrow(new RuntimeException("Produto está inativo!"));

        var produtoIsNull = service.buscarPorIdInativo(produtoInativo.getId());

        Assertions.assertThat(produtoIsNull).isNull();
    }
    
    @Test
    void buscarPorDescricao_QuandoForBemSucedido() {
        var produto = produtoList.getFirst();
        
        BDDMockito.when(produtoRepository.buscarPorDescricao(produto.getDescricao()))
                .thenReturn(List.of(produto));

        var produtoListExpected = service.buscarPorDescricao(produto.getDescricao());

        Assertions.assertThat(produtoListExpected).isNotNull().isEqualTo(List.of(produto));
    }

    @Test
    void buscarPorDescricao_BuscarTdosLancaExcecao_RetornaListVazia () {
        BDDMockito.when(produtoRepository.buscarTodos())
                .thenThrow(new RuntimeException("Falha ao buscar Produtos!"));

        var produtosListEmpty = service.buscarPorDescricao(null);

        Assertions.assertThat(produtosListEmpty).isEmpty();
    }

    @Test
    void buscarPorDescricao_BuscarPorDescricaoLancaExcecao_RetornaListVazia () {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorDescricao(produto.getDescricao()))
                .thenThrow(new RuntimeException("Falha ao buscar Produto por Descricao!"));

        var produtosListEmpty = service.buscarPorDescricao(produto.getDescricao());

        Assertions.assertThat(produtosListEmpty).isEmpty();
    }

    @Test
    void buscarPorDescricao_RetornaListVazia () {
        var teste = "teste";

        BDDMockito.when(produtoRepository.buscarPorDescricao(teste)).thenReturn(List.of());

        var produtosListEmpty = service.buscarPorDescricao(teste);

        Assertions.assertThat(produtosListEmpty).isEmpty();
    }
}
