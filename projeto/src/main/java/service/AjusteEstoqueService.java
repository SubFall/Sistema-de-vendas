package service;

import conn.ConnectionProvider;
import domain.ajusteestoque.AjusteEstoque;
import domain.ajusteestoque.AjusteEstoqueItens;
import domain.ajusteestoque.Status;
import domain.ajusteestoque.StatusMovimentoCriado;
import domain.movimento.Movimento;
import domain.movimento.MovimentoItem;
import domain.movimento.StatusMovimento;
import domain.movimento.Tipo;
import domain.pessoa.Pessoa;
import domain.produto.Produto;
import lombok.RequiredArgsConstructor;
import repository.*;
import util.console.ConsoleUtils;
import util.mensagens.MensagensAjusteEstoque;
import util.mensagens.MensagensGlobais;
import util.mensagens.MensagensMovimentos;
import util.mensagens.MensagensProdutos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ui.grid.ProdutoEstoqueGrid.*;

@RequiredArgsConstructor
public class AjusteEstoqueService {
    private final Scanner scanner;
    private final AjusteEstoqueRepository ajusteEstoqueRepository;
    private final AjusteEstoqueItemRepository ajusteEstoqueItemRepository;
    private final ProdutoRepository produtoRepository;
    private final MovimentoRepository movimentoRepository;
    private final MovimentoItemRepository movimentoItemRepository;
    private final HistoricoEstoqueService historicoEstoqueService;
    private final PessoaRepository pessoaRepository;
    private final ConnectionProvider connectionProvider;
    private final EstoqueService estoqueService;
    private final ProdutoService produtoService;

    public void inserirAjusteEstoque(AjusteEstoque ajusteEstoque) {
        Connection conn = null;

        try {
            conn = connectionProvider.getConnection();
            conn.setAutoCommit(false);

            var idAjusteEstoque = ajusteEstoqueRepository.inserirAjusteEstoque(conn, ajusteEstoque);

            var isInseriu = ajusteEstoqueItemRepository.inserirAjusteEstoqueItens(conn, idAjusteEstoque, ajusteEstoque.getAjusteEstoqueItens());

            if (!isInseriu) {
                throw new IllegalArgumentException(MensagensAjusteEstoque.ERRO_INSERIR_AJUSTE_ESTOQUE);
            }

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);

            throw new RuntimeException(MensagensMovimentos.ERRO_INSERIR_MOVIMENTO, e);
        } catch (IllegalArgumentException e) {
            rollback(conn);

            throw e;
        } finally {
            fechar(conn);
        }
    }

    public List<AjusteEstoque> buscarTodosAjusteEstoqueAberto() {
        List<AjusteEstoque> ajusteEstoqueList = new ArrayList<>();
        try {
            ajusteEstoqueList = ajusteEstoqueRepository.buscarTodosAjusteEstoqueAberto();

            return ajusteEstoqueList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ajusteEstoqueList;
    }

    public AjusteEstoque buscarAjustePorId(Long idAjuste) {
        try {
            var ajusteEstoque = ajusteEstoqueRepository.buscarAjusteEstoquePorId(idAjuste);

            if (ajusteEstoque == null) {
                throw new IllegalArgumentException(MensagensAjusteEstoque.AJUSTE_ESTOQUE_NAO_LOCALIZADO);
            }

            return ajusteEstoque;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void criarMovimento(Connection conn, List<AjusteEstoqueItens> ajusteEstoqueItens, Tipo tipo) {
        List<MovimentoItem> movimentoItems = new ArrayList<>();

        adicionarMovimentoItem(ajusteEstoqueItens, movimentoItems);

        var movimento = getMovimento(tipo, movimentoItems);

        var idMovimento = movimentoRepository.inserirMovimento(conn, movimento);
        movimento.setId(idMovimento);

        inserirMovimentoItem(conn, movimento, idMovimento);
    }

    private void adicionarMovimentoItem(List<AjusteEstoqueItens> ajusteEstoqueItens, List<MovimentoItem> movimentoItems) {
        for (AjusteEstoqueItens estoqueItens : ajusteEstoqueItens) {
            var movimentoItem = MovimentoItem.builder()
                    .produto(produtoRepository.buscarPorId(estoqueItens.getProduto().getId()))
                    .quantidade(estoqueItens.getContagem())
                    .build();

            movimentoItems.add(movimentoItem);
        }
    }

    private Movimento getMovimento(Tipo tipo, List<MovimentoItem> movimentoItems) {
        return Movimento.builder()
                .pessoa(pessoaRepository.buscarPorId(Pessoa.ID_PESSOA_PADRAO))
                .funcionario(pessoaRepository.buscarPorId(Pessoa.ID_PESSOA_PADRAO))
                .tipo(tipo)
                .statusMovimento(StatusMovimento.FINALIZADO)
                .movimentoItens(movimentoItems)
                .build();
    }

    private void inserirMovimentoItem(Connection conn, Movimento movimento, int idMovimento) {
        for (MovimentoItem movimentoItem : movimento.getMovimentoItens()) {
            var isInseriu = movimentoItemRepository.inserirMovimentoItem(conn, idMovimento, movimentoItem);

            if (!isInseriu) {
                throw new IllegalArgumentException(MensagensMovimentos.ERRO_INSERIR_MOVIMENTO);
            }

            if (movimento.getStatusMovimento() == StatusMovimento.FINALIZADO) {
                historicoEstoqueService.movimentar(conn, movimentoItem.getProduto(), movimento,
                        movimentoItem.getQuantidade(), movimento.getTipo());
            }
        }
    }

    public void criarMovimentoAjusteEstoque(Long idAjuste) {
        Connection conn = null;

        var ajusteEstoqueItensEntradaList = buscarAjusteEstoqueItensEntrada(idAjuste);
        var ajusteEstoqueItensSaidaList = buscarAjusteEstoqueItensSaida(idAjuste);

        try {
            conn = connectionProvider.getConnection();
            conn.setAutoCommit(false);

            if (!ajusteEstoqueItensEntradaList.isEmpty()) {
                criarMovimento(conn, ajusteEstoqueItensEntradaList, Tipo.ENTRADA);
            }

            if (!ajusteEstoqueItensSaidaList.isEmpty()) {
                criarMovimento(conn, ajusteEstoqueItensSaidaList, Tipo.SAIDA);
            }

            if (!ajusteEstoqueItensEntradaList.isEmpty() || !ajusteEstoqueItensSaidaList.isEmpty()) {
                ajusteEstoqueRepository.mudarStatusMovimento(conn, StatusMovimentoCriado.FINALIZADO_CRIADO, idAjuste);
            }

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);

            throw new RuntimeException(MensagensMovimentos.ERRO_INSERIR_MOVIMENTO, e);
        } catch (IllegalArgumentException e) {
            rollback(conn);

            throw e;
        } finally {
            fechar(conn);
        }
    }

    public List<AjusteEstoqueItens> buscarAjusteEstoqueItensEntrada(Long idAjuste) {
        List<AjusteEstoqueItens> ajusteEstoqueItensList = new ArrayList<>();

        try {
            ajusteEstoqueItensList = ajusteEstoqueRepository.buscarAjusteEstoqueItensEntrada(idAjuste);

            return ajusteEstoqueItensList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ajusteEstoqueItensList;
    }

    public List<AjusteEstoqueItens> buscarAjusteEstoqueItensSaida(Long idAjuste) {
        List<AjusteEstoqueItens> ajusteEstoqueItensList = new ArrayList<>();

        try {
            ajusteEstoqueItensList = ajusteEstoqueRepository.buscarAjusteEstoqueItensSaida(idAjuste);

            return ajusteEstoqueItensList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ajusteEstoqueItensList;
    }

    public void criarMovimento() {
        var ajusteEstoqueList = buscarAjustePorStatus(Status.FECHADO);
        exibirGridAjusteEstoque(ajusteEstoqueList);

        if (ajusteEstoqueList.isEmpty()) {
            return;
        }

        try {
            System.out.print(MensagensAjusteEstoque.DIGITE_ID);
            var ajusteEstoque = buscarAjustePorId(ConsoleUtils.lerLong(scanner, MensagensGlobais.ID));

            criarMovimentoAjusteEstoque(ajusteEstoque.getId());

            System.out.println("\nMovimento criado com Sucesso!\n");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<AjusteEstoque> buscarAjustePorStatus(Status status) {
        List<AjusteEstoque> ajusteEstoqueList = new ArrayList<>();
        try {
            ajusteEstoqueList = ajusteEstoqueRepository.buscarAjustePorStatus(status);

            return ajusteEstoqueList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ajusteEstoqueList;
    }

    public void novoAjuste() {

        System.out.println("Título:");
        String titulo = scanner.nextLine();

        List<AjusteEstoqueItens> itens = new ArrayList<>();

        boolean repeticao = true;
        while (repeticao) {
            System.out.println("1 - Adicionar item");
            System.out.println("2 - remover item");
            System.out.println("3 - finalizar ajuste");
            System.out.print(MensagensGlobais.OPCAO);

            switch (ConsoleUtils.lerInteiro(scanner, MensagensGlobais.OPCAO)) {
                case 1 -> adicionarItem(itens);
                case 2 -> removerItem(itens);
                case 3 -> {
                    if (concluir(titulo, itens)) {
                        repeticao = false;
                    }
                }
            }
        }
    }

    public void removerAjusteEstoque(AjusteEstoque ajusteEstoque) {
        Connection conn = null;

        try {
            conn = connectionProvider.getConnection();
            conn.setAutoCommit(false);

            ajusteEstoqueRepository.removerAjusteEstoqueItem(conn, ajusteEstoque);
            ajusteEstoqueRepository.removerAjusteEstoque(conn, ajusteEstoque);

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);

            throw new RuntimeException(MensagensMovimentos.ERRO_INSERIR_MOVIMENTO, e);
        } catch (IllegalArgumentException e) {
            rollback(conn);

            throw e;
        } finally {
            fechar(conn);
        }
    }

    public void remover() {
        var ajusteEstoqueList = buscarTodosAjusteEstoqueAberto();
        exibirGridAjusteEstoque(ajusteEstoqueList);

        if (ajusteEstoqueList.isEmpty()) return;

        try {
            System.out.print(MensagensAjusteEstoque.DIGITE_ID);
            AjusteEstoque ajusteEstoque = buscarAjustePorId(ConsoleUtils.lerLong(scanner, MensagensGlobais.ID));

            removerAjusteEstoque(ajusteEstoque);

            System.out.println("\nAjuste Estoque removido com Sucesso!\n");
        } catch (IllegalAccessError e) {
            System.out.println(e.getMessage());
        }
    }

    public void atualizarAjusteEstoque(AjusteEstoque ajusteEstoque) {
        Connection conn = null;

        try {
            conn = connectionProvider.getConnection();
            conn.setAutoCommit(false);

            ajusteEstoqueRepository.atualizarAjusteEstoque(conn, ajusteEstoque);
            ajusteEstoqueRepository.removerAjusteEstoqueItem(conn, ajusteEstoque);

            var isInseriu = ajusteEstoqueItemRepository.inserirAjusteEstoqueItens(conn, ajusteEstoque.getId(), ajusteEstoque.getAjusteEstoqueItens());

            if (!isInseriu) {
                throw new IllegalArgumentException(MensagensAjusteEstoque.ERRO_INSERIR_AJUSTE_ESTOQUE_ITENS);
            }

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);

            throw new RuntimeException(MensagensMovimentos.ERRO_INSERIR_MOVIMENTO, e);
        } catch (IllegalArgumentException e) {
            rollback(conn);

            throw e;
        } finally {
            fechar(conn);
        }
    }

    public void adicionarItem(List<AjusteEstoqueItens> ajusteEstoqueItensList) {
        var produto = getProduto();

        var ajusteEstoqueItens = AjusteEstoqueItens.builder()
                .produto(produto)
                .estoque(estoqueService.buscarPorIdProduto(produto.getId()))
                .contagem(ConsoleUtils.lerBigDecimal(scanner, "Contagem"))
                .build();

        var isProdutoCotensList = produtoExisteNaList(ajusteEstoqueItensList, ajusteEstoqueItens);

        if (!isProdutoCotensList) {
            ajusteEstoqueItensList.add(ajusteEstoqueItens);
            exibirGridItens(ajusteEstoqueItensList);
            return;
        }

        for (AjusteEstoqueItens estoqueItens : ajusteEstoqueItensList) {
            if (estoqueItens.equals(ajusteEstoqueItens)) {
                estoqueItens.setContagem(estoqueItens.getContagem().add(ajusteEstoqueItens.getContagem()));

                exibirGridItens(ajusteEstoqueItensList);
                return;
            }
        }
    }

    private boolean produtoExisteNaList(List<AjusteEstoqueItens> ajusteEstoqueItensList, AjusteEstoqueItens ajusteEstoqueItens) {
        return ajusteEstoqueItensList.contains(ajusteEstoqueItens);
    }

    public void removerItem(List<AjusteEstoqueItens> ajusteEstoqueItensList) {
        var produto = getProdutoAjusteEstoqueItens(ajusteEstoqueItensList);

        if (produto == null) return;

        var ajusteEstoqueItens = AjusteEstoqueItens.builder().produto(produto).build();

        if (!ajusteEstoqueItensList.contains(ajusteEstoqueItens)) {
            System.out.println("\nItem Não localizado!\n");
            return;
        }

        ajusteEstoqueItensList.remove(ajusteEstoqueItens);

        System.out.println("\nItem removido da Lista.\n");
        exibirGridItens(ajusteEstoqueItensList);
    }

    private Produto getProduto() {
        exibirtGridProdutoEstoque(estoqueService.buscarTodosProdutosEstoque());
        System.out.print(MensagensProdutos.DIGITE_ID);

        return produtoService.buscarPorId(ConsoleUtils.lerLong(scanner, MensagensGlobais.ID));
    }

    private Produto getProdutoAjusteEstoqueItens(List<AjusteEstoqueItens> ajusteEstoqueItensList) {
        exibirGridItens(ajusteEstoqueItensList);
        System.out.print(MensagensProdutos.DIGITE_ID);

        try {
            return produtoService.buscarPorId(ConsoleUtils.lerLong(scanner, MensagensGlobais.ID));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void atualizarAjusteEstoque() {
        var ajusteEstoqueList = buscarTodosAjusteEstoqueAberto();
        exibirGridAjusteEstoque(ajusteEstoqueList);

        if (ajusteEstoqueList.isEmpty()) return;

        try {
            System.out.print(MensagensAjusteEstoque.DIGITE_ID);

            var ajusteEstoque = buscarAjustePorId(ConsoleUtils.lerLong(scanner, MensagensGlobais.ID));
            ajusteEstoque.setAjusteEstoqueItens(buscarAjusteEstoqueItensList(ajusteEstoque.getId()));

            System.out.println(MensagensGlobais.MANTER_VALOR_ANTIGO);

            atualizarTitulo(ajusteEstoque);
            atualizarStatus(ajusteEstoque);

            if (ConsoleUtils.confirmar(scanner, MensagensGlobais.ATUALIZAR_ITENS)) {
                exibirGridItens(ajusteEstoque.getAjusteEstoqueItens());
                var isRepeticao = true;

                while (isRepeticao) {
                    System.out.println(MensagensProdutos.ADICIONAR_ITEM);
                    System.out.print(MensagensGlobais.OPCAO);

                    switch (ConsoleUtils.lerInteiro(scanner, MensagensGlobais.OPCAO)) {
                        case 1 -> adicionarItem(ajusteEstoque.getAjusteEstoqueItens());
                        case 2 -> removerItem(ajusteEstoque.getAjusteEstoqueItens());
                        case 3 -> isRepeticao = false;
                    }
                }
            }

            atualizarAjusteEstoque(ajusteEstoque);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private List<AjusteEstoqueItens> buscarAjusteEstoqueItensList(Long idAjuste) {
        return ajusteEstoqueRepository.buscarAjusteEstoqueItensList(idAjuste);
    }

    private void atualizarTitulo(AjusteEstoque ajusteEstoque) {
        System.out.println("Título Atual: " + ajusteEstoque.getTitulo());
        System.out.print("Novo Título: ");

        var titulo = scanner.nextLine();
        ajusteEstoque.setTitulo(titulo.isBlank() ? ajusteEstoque.getTitulo() : titulo);
    }

    private void atualizarStatus(AjusteEstoque ajusteEstoque) {
        int op;
        do {
            System.out.println("Status Atual: " + ajusteEstoque.getStatus());
            System.out.println("1 - Aberto");
            System.out.println("2 - Fechado");
            System.out.print("Status: ");

            op = ConsoleUtils.lerInteiro(scanner, MensagensGlobais.OPCAO);
        } while (op != 1 && op != 2);

        if (op == 1) {
            ajusteEstoque.setStatus(Status.ABERTO);
        } else {
            ajusteEstoque.setStatus(Status.FECHADO);
        }
    }

    public boolean concluir(String titulo, List<AjusteEstoqueItens> itens) {
        int opcao;
        do {
            System.out.println("1 - Salvar ajuste em aberto");
            System.out.println("2 - Salvar ajuste finalizado");
            System.out.println("3 - Cancelar operação");

            opcao = ConsoleUtils.lerInteiro(scanner, MensagensGlobais.OPCAO);
        } while (opcao != 1 && opcao != 2 && opcao != 3);

        if (itens.isEmpty()) {
            System.out.println("\nAdicione pelo menos um item antes de concluir a venda.\n");
            return false;
        }

        var ajusteEstoque = AjusteEstoque.builder()
                .titulo(titulo)
                .status(Status.ABERTO)
                .ajusteEstoqueItens(itens)
                .build();

        if (opcao == 2) {
            ajusteEstoque.setStatus(Status.FECHADO);
        } else if (opcao == 3) {
            System.out.println("\nAjuste cancelado!\n");
            return true;
        }

        inserirAjusteEstoque(ajusteEstoque);
        System.out.println("\nAjuste criado com sucesso!\n");
        return true;
    }

    public void buscarTodosAjusteEstoque() {
        try {
            exibirGridAjusteEstoque(ajusteEstoqueRepository.buscarTodosAjusteEstoque());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void buscarAjusteEstoquePorTitulo() {
        System.out.print("Digite o Título: ");
        var titulo = scanner.nextLine();

        try {
            exibirGridAjusteEstoque(ajusteEstoqueRepository.buscarAjusteEstoquePorTitulo(titulo));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void buscarAjusteEstoqueItens() {
        try {
            var ajusteEstoqueList = ajusteEstoqueRepository.buscarTodosAjusteEstoque();
            exibirGridAjusteEstoque(ajusteEstoqueList);

            System.out.print(MensagensAjusteEstoque.DIGITE_ID);
            var ajusteEstoque = buscarAjustePorId(ConsoleUtils.lerLong(scanner, MensagensGlobais.ID));

            ajusteEstoque.setAjusteEstoqueItens(ajusteEstoqueRepository.buscarAjusteEstoqueItensList(ajusteEstoque.getId()));

            exibirGridItens(ajusteEstoque.getAjusteEstoqueItens());
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("\nErro ao realizar rollback!\n", ex);
        }
    }

    private void fechar(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("\nErro ao fechar a conexão!\n", e);
        }
    }
}
