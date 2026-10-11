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
import repository.*;
import util.console.ConsoleUtils;
import util.mensagens.MensagensAjusteEstoque;
import util.mensagens.MensagensGlobais;
import util.mensagens.MensagensProdutos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ui.grid.ProdutoEstoqueGrid.*;

public class AjusteEstoqueService {
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
    private final Scanner scanner = new Scanner(System.in);

    public AjusteEstoqueService(AjusteEstoqueRepository estoqueRepository,
                                AjusteEstoqueItemRepository ajusteEstoqueItemRepository,
                                ProdutoRepository produtoRepository,
                                MovimentoRepository movimentoRepository,
                                MovimentoItemRepository movimentoItemRepository,
                                HistoricoEstoqueService historicoEstoqueService,
                                PessoaRepository pessoaRepository,
                                ConnectionProvider connectionProvider,
                                EstoqueService estoqueService,
                                ProdutoService produtoService) {
        this.ajusteEstoqueRepository = estoqueRepository;
        this.ajusteEstoqueItemRepository = ajusteEstoqueItemRepository;
        this.produtoRepository = produtoRepository;
        this.movimentoRepository = movimentoRepository;
        this.movimentoItemRepository = movimentoItemRepository;
        this.historicoEstoqueService = historicoEstoqueService;
        this.pessoaRepository = pessoaRepository;
        this.connectionProvider = connectionProvider;
        this.estoqueService = estoqueService;
        this.produtoService = produtoService;
    }

    public void inserirAjusteEstoque(AjusteEstoque ajusteEstoque) {
        Connection conn = null;

        try {
            conn = connectionProvider.getConnection();
            conn.setAutoCommit(false);

            var idAjusteEstoque = ajusteEstoqueRepository.inserirAjusteEstoque(conn, ajusteEstoque);

            var isInseriu = ajusteEstoqueItemRepository.inserirAjusteEstoqueItens(conn, idAjusteEstoque, ajusteEstoque.getAjusteEstoqueItens());

            if (!isInseriu) {
                throw new IllegalArgumentException("Erro ao inserir item do Ajuste Estoque");
            }

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);

            throw new RuntimeException("Erro ao inserir movimento", e);
        } catch (IllegalArgumentException e) {
            rollback(conn);

            throw e;
        } finally {
            fechar(conn);
        }
    }

    public List<AjusteEstoque> buscarTodosAjusteEstoqueAberto() {
        return ajusteEstoqueRepository.buscarTodosAjusteEstoqueAberto();
    }

    public AjusteEstoque buscarAjustePorId(Long idAjuste) {
        var ajusteEstoque = ajusteEstoqueRepository.buscarAjustePorId(idAjuste);

        if (ajusteEstoque == null) {
            throw new IllegalArgumentException("Ajuste de Estoque não localizado");
        }

        return ajusteEstoque;
    }

    public AjusteEstoqueItens buscarAjusteEstoqueItemPorId(Long idAjuste) {
        var ajusteEstoqueItem = ajusteEstoqueRepository.buscarAjusteEstoqueItem(idAjuste);

        if (ajusteEstoqueItem == null) {
            throw new IllegalArgumentException("Ajuste Item não localizado");
        }

        return ajusteEstoqueItem;
    }

    public List<AjusteEstoqueItens> buscarAjusteEstoqueItensList(Long idAjuste) {
        return ajusteEstoqueRepository.buscarAjusteEstoqueItensList(idAjuste);
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
                throw new IllegalArgumentException("Erro ao inserir item do movimento");
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

            throw new RuntimeException("Erro ao inserir movimento", e);
        } catch (IllegalArgumentException e) {
            rollback(conn);

            throw e;
        } finally {
            fechar(conn);
        }
    }

    public List<AjusteEstoqueItens> buscarAjusteEstoqueItensEntrada(Long idAjuste) {
        return ajusteEstoqueRepository.buscarAjusteEstoqueItensEntrada(idAjuste);
    }

    public List<AjusteEstoqueItens> buscarAjusteEstoqueItensSaida(Long idAjuste) {
        return ajusteEstoqueRepository.buscarAjusteEstoqueItensSaida(idAjuste);
    }

    public void criarMovimento() {
        var ajusteEstoqueList = buscarAjustePorStatus(Status.FECHADO);
        exibirGridAjusteEstoque(ajusteEstoqueList);

        if (ajusteEstoqueList.isEmpty()) {
            return;
        }

        try {
            System.out.print(MensagensAjusteEstoque.DIGITE_ID);
            var ajusteEstoque = buscarAjustePorId(ConsoleUtils.lerLong(scanner, "ID"));

            criarMovimentoAjusteEstoque(ajusteEstoque.getId());

            System.out.println("Movimento criado com Sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<AjusteEstoque> buscarAjustePorStatus(Status status) {
        return ajusteEstoqueRepository.buscarAjustePorStatus(status);
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
                    if (concluirAjuste(titulo, itens)) {
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

            throw new RuntimeException("Erro ao inserir movimento", e);
        } catch (IllegalArgumentException e) {
            rollback(conn);

            throw e;
        } finally {
            fechar(conn);
        }
    }

    public void removerAjuste() {
        var ajusteEstoqueList = buscarTodosAjusteEstoqueAberto();
        exibirGridAjusteEstoque(ajusteEstoqueList);

        if (ajusteEstoqueList.isEmpty()) return;

        try {
            System.out.print(MensagensAjusteEstoque.DIGITE_ID);
            AjusteEstoque ajusteEstoque = buscarAjustePorId(ConsoleUtils.lerLong(scanner, "ID"));

            removerAjusteEstoque(ajusteEstoque);

            System.out.println("Ajuste Estoque removido com Sucesso!");
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
                throw new IllegalArgumentException("Erro ao inserir item do Ajuste Estoque");
            }

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);

            throw new RuntimeException("Erro ao inserir movimento", e);
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
            System.out.println("Item Não localizado!");
            return;
        }

        ajusteEstoqueItensList.remove(ajusteEstoqueItens);

        System.out.println("\nItem removido da Lista");
        exibirGridItens(ajusteEstoqueItensList);
    }

    private Produto getProduto() {
        exibirtGirdProdutoEstoque(estoqueService.buscarProdutosEstoque());
        System.out.print(MensagensProdutos.DIGITE_ID);

        return produtoService.buscarPorId(ConsoleUtils.lerLong(scanner, "ID"));
    }

    private Produto getProdutoAjusteEstoqueItens(List<AjusteEstoqueItens> ajusteEstoqueItensList) {
        exibirGridItens(ajusteEstoqueItensList);
        System.out.print(MensagensProdutos.DIGITE_ID);

        try {
            return produtoService.buscarPorId(ConsoleUtils.lerLong(scanner, "ID"));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void atualizar() {
        var ajusteEstoqueList = buscarTodosAjusteEstoqueAberto();
        exibirGridAjusteEstoque(ajusteEstoqueList);

        if (ajusteEstoqueList.isEmpty()) return;

        try {
            System.out.print(MensagensAjusteEstoque.DIGITE_ID);

            var ajusteEstoque = buscarAjustePorId(ConsoleUtils.lerLong(scanner, "ID"));
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

    public boolean concluirAjuste(String titulo, List<AjusteEstoqueItens> itens) {
        int opcao;
        do {
            System.out.println("1 - Salvar ajuste em aberto");
            System.out.println("2 - Salvar ajuste finalizado");
            System.out.println("3 - Cancelar operação");

            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");
        } while (opcao != 1 && opcao != 2 && opcao != 3);

        if (itens.isEmpty()) {
            System.out.println("Adicione pelo menos um item antes de concluir a venda.");
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
            System.out.println("Ajuste cancelado!");
            return true;
        }

        inserirAjusteEstoque(ajusteEstoque);
        System.out.println("Ajuste criado com sucesso!");
        return true;
    }

    public void buscarTodosAjusteEstoque() {
        exibirGridAjusteEstoque(ajusteEstoqueRepository.buscarTodosAjusteEstoque());
    }

    public void buscarAjusteEstoquePorTitulo() {
        System.out.print("Digite o Título: ");
        var titulo = scanner.nextLine();
        exibirGridAjusteEstoque(ajusteEstoqueRepository.buscarAjusteEstoquePorTitulo(titulo));
    }

    private void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao realizar rollback", ex);
        }
    }

    private void fechar(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar a conexão", e);
        }
    }
}
