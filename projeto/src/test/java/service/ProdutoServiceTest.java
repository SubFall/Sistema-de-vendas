package service;

import domain.categoria.Categoria;
import domain.exception.*;
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
import java.sql.SQLException;
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
    void inserirProduto_QuandoForBemSucedido_RetornaTrue() throws SQLException {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.inserirProduto(produto))
                .thenReturn(true);

        var isInseriu = service.inserirProduto(produto);

        Assertions.assertThat(isInseriu).isTrue();

        BDDMockito.verify(produtoRepository).inserirProduto(produto);
        BDDMockito.verifyNoInteractions(categoriaRepository);
    }

    @Test
    void inserirProduto_QuandoRepositorioLancaSQLException_LancaExcecaoDeDominio() throws SQLException {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.inserirProduto(produto))
                .thenThrow(new SQLException("Nao foi possivel cadastrar o produto. Tente novamente."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.inserirProduto(produto))
                .isInstanceOf(FalhaAoInserirProdutoException.class)
                .withMessage("Nao foi possivel cadastrar o produto. Tente novamente.");
    }

    @Test
    void inserirProduto_LancaExcecaoDeDominio_FalhaValorJaRegistradoException() throws SQLException {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorDescricao(produto.getDescricao()))
                .thenThrow(new FalhaValorJaRegistradoException("Produto: " + produto.getDescricao() + " Já Cadastrado."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.inserirProduto(produto))
                .isInstanceOf(FalhaValorJaRegistradoException.class)
                .withMessage("Produto: " + produto.getDescricao() + " Já Cadastrado.");
    }

    @Test
    void atualizarStatusProduto_QuandoForBemSucedido() throws SQLException {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.atualizarStatusProduto(produto))
                .thenReturn(true);

        var isAtualizou = service.atualizarStatusProduto(produto);

        Assertions.assertThat(isAtualizou).isTrue();

        BDDMockito.verify(produtoRepository).atualizarStatusProduto(produto);
    }

    @Test
    void atualizarStatusProduto_QuandoRepositorioLancaSQLException_LancaExcecaoDeDominio() throws SQLException {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.atualizarStatusProduto(produto))
                .thenThrow(new SQLException("Nao foi possivel atualizar o produto. Tente novamente."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.atualizarStatusProduto(produto))
                .isInstanceOf(FalhaAoAtualizarProdutoException.class)
                .withMessage("Nao foi possivel atualizar o produto. Tente novamente.");
    }

    @Test
    void atualizarProduto_QuandoForBemSucedido() throws SQLException {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produto.getId()))
                .thenReturn(produto);

        BDDMockito.when(produtoRepository.atualizarProduto(produto))
                .thenReturn(true);

        var isAtualizou = service.atualizarProduto(produto);

        Assertions.assertThat(isAtualizou).isTrue();

        BDDMockito.verify(produtoRepository).atualizarProduto(produto);
    }

    @Test
    void atualizarProduto_QuandoRepositorioLancaSQLException_LancaExcecaoDeDominio() throws SQLException {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produto.getId()))
                .thenThrow(new SQLException("Nao foi possivel atualizar o produto. Tente novamente."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.atualizarProduto(produto))
                .isInstanceOf(FalhaAoAtualizarProdutoException.class)
                .withMessage("Nao foi possivel atualizar o produto. Tente novamente.");

        BDDMockito.verify(produtoRepository, BDDMockito.never())
                .atualizarProduto(produto);
    }

    @Test
    void atualizarProduto_QuandoLancaExcecaoDeDominio() throws SQLException {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produto.getId()))
                .thenThrow(new FalhaProdutoNaoExisteException("Produto nao cadastrado."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.atualizarProduto(produto))
                .isInstanceOf(FalhaProdutoNaoExisteException.class)
                .withMessage("Produto nao cadastrado.");

        BDDMockito.verify(produtoRepository, BDDMockito.never())
                .atualizarProduto(produto);
    }

    @Test
    void buscarTodos_QuandoFoBemSucedido() throws SQLException {
        BDDMockito.when(produtoRepository.buscarTodos())
                .thenReturn(produtoList);

        var produtoListExpected = service.buscarTodos();

        Assertions.assertThat(produtoListExpected).isNotNull().isEqualTo(produtoList);
    }

    @Test
    void buscarTodos_RetornaListaVazia_QuandoNaoTemProdutoCadastrado() throws SQLException {
        BDDMockito.when(produtoRepository.buscarTodos())
                .thenReturn(List.of());

        var produtosListEmpty = service.buscarTodos();

        Assertions.assertThat(produtosListEmpty).isEmpty();
    }

    @Test
    void buscarTodos_QuandoRepositorioLancaSQLException_LancaExcecaoDeDominio() throws SQLException {
        BDDMockito.when(produtoRepository.buscarTodos())
                .thenThrow(new SQLException("Nao foi possivel buscar o produto. Tente novamente."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarTodos())
                .isInstanceOf(FalhaAoBuscarProdutoException.class)
                .withMessage("Nao foi possivel buscar o produto. Tente novamente.");
    }

    @Test
    void buscarTodosAtivos_QuandoForBemSucedido() throws SQLException {
        BDDMockito.when(produtoRepository.buscarTodosPorStatus(true))
                .thenReturn(produtoList);

        var listProdutosAtivos = service.buscarTodosAtivos();

        Assertions.assertThat(listProdutosAtivos).isNotNull().isEqualTo(produtoList);
    }

    @Test
    void buscarTodosAtivos_QuandoRepositorioLancaSQLException_LancaExcecaoDeDominio() throws SQLException {
        BDDMockito.when(produtoRepository.buscarTodosPorStatus(true))
                .thenThrow(new SQLException("Nao foi possivel buscar o produto. Tente novamente."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarTodosAtivos())
                .isInstanceOf(FalhaAoBuscarProdutoException.class)
                .withMessage("Nao foi possivel buscar o produto. Tente novamente.");
    }

    @Test
    void buscarTodosAtivos_RetornaListaVazia_QuandoNaoTemProdutoCadastrado() throws SQLException {
        BDDMockito.when(produtoRepository.buscarTodosPorStatus(true))
                .thenReturn(List.of());

        var listProdutosEmpty = service.buscarTodosAtivos();

        Assertions.assertThat(listProdutosEmpty).isEmpty();
    }

    @Test
    void buscarTodosInativos_QuandoForBemSucedido() throws SQLException {
        BDDMockito.when(produtoRepository.buscarTodosPorStatus(false))
                .thenReturn(produtoList);

        var listProdutosInativos = service.buscarTodosInativos();

        Assertions.assertThat(listProdutosInativos).isNotNull().isEqualTo(produtoList);
    }

    @Test
    void buscarTodosIntivos_QuandoRepositorioLancaSQLException_LancaExcecaoDeDominio() throws SQLException {
        BDDMockito.when(produtoRepository.buscarTodosPorStatus(false))
                .thenThrow(new SQLException("Nao foi possivel buscar o produto. Tente novamente."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarTodosInativos())
                .isInstanceOf(FalhaAoBuscarProdutoException.class)
                .withMessage("Nao foi possivel buscar o produto. Tente novamente.");
    }

    @Test
    void buscarTodosInativos_RetornaListaVazia() throws SQLException {
        BDDMockito.when(produtoRepository.buscarTodosPorStatus(false))
                .thenReturn(List.of());

        var listProdutosInativosEmpty = service.buscarTodosInativos();

        Assertions.assertThat(listProdutosInativosEmpty).isEmpty();
    }

    @Test
    void buscarPorId_QuandoForBemSucedido() throws SQLException {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produto.getId()))
                .thenReturn(produto);

        var expectedProduto = service.buscarPorId(produto.getId());

        Assertions.assertThat(expectedProduto).isNotNull().isEqualTo(produto);
    }

    @Test
    void buscarPorId_RetornaNull() throws SQLException {
        var id = 99L;

        BDDMockito.when(produtoRepository.buscarPorId(id))
                .thenThrow(new FalhaProdutoNaoExisteException("Produto nao cadastrado."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarPorId(id))
                .isInstanceOf(FalhaProdutoNaoExisteException.class)
                .withMessage("Produto nao cadastrado.");
    }

    @Test
    void buscarPorId_QuandoRepositorioLancaSQLException_LancaExcecaoDeDominio() throws SQLException {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produto.getId()))
                .thenThrow(new SQLException("Nao foi possivel buscar o produto. Tente novamente."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarPorId(produto.getId()))
                .isInstanceOf(FalhaAoBuscarProdutoException.class)
                .withMessage("Nao foi possivel buscar o produto. Tente novamente.");
    }

    @Test
    void buscarPorIdAtivo_QuandoForBemSucedido() throws SQLException {
        var produtoAtivo = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produtoAtivo.getId()))
                .thenReturn(produtoAtivo);

        var produtoAtivoExpected = service.buscarPorIdAtivo(produtoAtivo.getId());

        Assertions.assertThat(produtoAtivoExpected).isNotNull().isEqualTo(produtoAtivo);
    }

    @Test
    void buscarPorIdAtivo_RetornaNull() throws SQLException {
        var id = 99L;

        BDDMockito.when(produtoRepository.buscarPorId(id))
                .thenThrow(new FalhaProdutoNaoExisteException("Produto nao cadastrado."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarPorIdAtivo(id))
                .isInstanceOf(FalhaProdutoNaoExisteException.class)
                .withMessage("Produto nao cadastrado.");
    }

    @Test
    void buscarPorIdAtivo_QuandoRepositorioLancaSQLException_LancaExcecaoDeDominio() throws SQLException {
        var produtoAtivo = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produtoAtivo.getId()))
                .thenThrow(new SQLException("Nao foi possivel buscar o produto. Tente novamente."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarPorIdAtivo(produtoAtivo.getId()))
                .isInstanceOf(FalhaAoBuscarProdutoException.class)
                .withMessage("Nao foi possivel buscar o produto. Tente novamente.");
    }

    @Test
    void buscarPorIdAtivo_LancaExcecaoDeDominio_FalhaProdutoNaoExisteException() throws SQLException {
        var produtoAtivo = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produtoAtivo.getId()))
                .thenThrow(new FalhaProdutoNaoExisteException("Produto nao cadastrado."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarPorIdAtivo(produtoAtivo.getId()))
                .isInstanceOf(FalhaProdutoNaoExisteException.class)
                .withMessage("Produto nao cadastrado.");
    }

    @Test
    void buscarPorIdAtivo_LancaExcecaoDeDominio_FalhaProdutoEstaInativoException() throws SQLException {
        var produtoAtivo = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produtoAtivo.getId()))
                .thenThrow(new FalhaProdutoEstaInativoException("Produto está inativo."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarPorIdAtivo(produtoAtivo.getId()))
                .isInstanceOf(FalhaProdutoEstaInativoException.class)
                .withMessage("Produto está inativo.");
    }

    @Test
    void buscarPorIdInativo_QuandoForBemSucedido() throws SQLException {
        var produtoInativo = produtoList.getLast();

        BDDMockito.when(produtoRepository.buscarPorId(produtoInativo.getId()))
                .thenReturn(produtoInativo);

        var produtoInativoExpected = service.buscarPorIdInativo(produtoInativo.getId());

        Assertions.assertThat(produtoInativoExpected).isNotNull().isEqualTo(produtoInativo);
    }

    @Test
    void buscarPorIdInativo_QuandoRepositorioLancaSQLException_LancaExcecaoDeDominio() throws SQLException {
        var id = 99L;

        BDDMockito.when(produtoRepository.buscarPorId(id))
                .thenThrow(new SQLException("Nao foi possivel buscar o produto. Tente novamente."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarPorIdInativo(id))
                .isInstanceOf(FalhaAoBuscarProdutoException.class)
                .withMessage("Nao foi possivel buscar o produto. Tente novamente.");
    }

    @Test
    void buscarPorIdInativo_LancaExcecaoDeDominio_FalhaProdutoNaoExisteException() throws SQLException {
        var produtoInativo = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produtoInativo.getId()))
                .thenThrow(new FalhaProdutoNaoExisteException("Produto nao cadastrado."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarPorIdInativo(produtoInativo.getId()))
                .isInstanceOf(FalhaProdutoNaoExisteException.class)
                .withMessage("Produto nao cadastrado.");
    }

    @Test
    void buscarPorIdInativo_LancaExcecaoDeDominio_FalhaProdutoEstaAtivoException() throws SQLException {
        var produtoAtivo = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorId(produtoAtivo.getId()))
                .thenThrow(new FalhaProdutoEstaAtivoException("Produto está ativo."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarPorIdAtivo(produtoAtivo.getId()))
                .isInstanceOf(FalhaProdutoEstaAtivoException.class)
                .withMessage("Produto está ativo.");
    }

    @Test
    void buscarPorIdInativo_RetornaNull() throws SQLException {
        var id = 99L;

        BDDMockito.when(produtoRepository.buscarPorId(id))
                .thenThrow(new FalhaProdutoNaoExisteException("Produto nao cadastrado"));

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarPorIdInativo(id))
                .isInstanceOf(FalhaProdutoNaoExisteException.class)
                .withMessage("Produto nao cadastrado");
    }

    @Test
    void buscarPorDescricao_QuandoForBemSucedido() throws SQLException {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorDescricao(produto.getDescricao()))
                .thenReturn(List.of(produto));

        var produtoListExpected = service.buscarPorDescricao(produto.getDescricao());

        Assertions.assertThat(produtoListExpected).isNotNull().isEqualTo(List.of(produto));
    }

    @Test
    void buscarPorDescricao_PassandoParametroNull() throws SQLException {
        BDDMockito.when(produtoRepository.buscarTodos())
                .thenReturn(produtoList);

        var produtoAllListExpected = service.buscarPorDescricao(null);

        Assertions.assertThat(produtoAllListExpected).isNotNull().isEqualTo(produtoList);
    }

    @Test
    void buscarPorDescricao_PassandoParametroVazio() throws SQLException {
        BDDMockito.when(produtoRepository.buscarTodos())
                .thenReturn(produtoList);

        var produtoAllListExpected = service.buscarPorDescricao("");

        Assertions.assertThat(produtoAllListExpected).isNotNull().isEqualTo(produtoList);
    }

    @Test
    void buscarPorDescricao_QuandoRepositorioLancaSQLException_LancaExcecaoDeDominio() throws SQLException {
        var produto = produtoList.getFirst();

        BDDMockito.when(produtoRepository.buscarPorDescricao(produto.getDescricao()))
                .thenThrow(new SQLException("Nao foi possivel buscar o produto. Tente novamente."));

        Assertions.assertThatException()
                .isThrownBy(() -> service.buscarPorDescricao(produto.getDescricao()))
                .isInstanceOf(FalhaAoBuscarProdutoException.class)
                .withMessage("Nao foi possivel buscar o produto. Tente novamente.");
    }

    @Test
    void buscarPorDescricao_RetornaListVazia() throws SQLException {
        var teste = "teste";

        BDDMockito.when(produtoRepository.buscarPorDescricao(teste))
                .thenReturn(List.of());

        var produtosListEmpty = service.buscarPorDescricao(teste);

        Assertions.assertThat(produtosListEmpty).isEmpty();
    }
}
