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
import repository.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AjusteEstoqueService {
    private final AjusteEstoqueRepository ajusteEstoqueRepository;
    private final AjusteEstoqueItemRepository ajusteEstoqueItemRepository;
    private final ProdutoRepository produtoRepository;
    private final MovimentoRepository movimentoRepository;
    private final MovimentoItemRepository movimentoItemRepository;
    private final HistoricoEstoqueService historicoEstoqueService;
    private final PessoaRepository pessoaRepository;
    private final ConnectionProvider connectionProvider;

    public AjusteEstoqueService(AjusteEstoqueRepository estoqueRepository,
                                AjusteEstoqueItemRepository ajusteEstoqueItemRepository,
                                ProdutoRepository produtoRepository,
                                MovimentoRepository movimentoRepository,
                                MovimentoItemRepository movimentoItemRepository,
                                HistoricoEstoqueService historicoEstoqueService,
                                PessoaRepository pessoaRepository,
                                ConnectionProvider connectionProvider) {
        this.ajusteEstoqueRepository = estoqueRepository;
        this.ajusteEstoqueItemRepository = ajusteEstoqueItemRepository;
        this.produtoRepository = produtoRepository;
        this.movimentoRepository = movimentoRepository;
        this.movimentoItemRepository = movimentoItemRepository;
        this.historicoEstoqueService = historicoEstoqueService;
        this.pessoaRepository = pessoaRepository;
        this.connectionProvider = connectionProvider;
    }

    public void inserirAjusteEstoque(AjusteEstoque estoque) {
        Connection conn = null;

        try {
            conn = connectionProvider.getConnection();
            conn.setAutoCommit(false);

            Long idAjusteEstoque = ajusteEstoqueRepository.inserirAjusteEstoque(conn, estoque);

            for (AjusteEstoqueItens item : estoque.getAjusteEstoqueItens()) {
                boolean isInseriu = ajusteEstoqueItemRepository.inserirAjusteEstoqueItem(conn, idAjusteEstoque, item);

                if (!isInseriu) {
                    throw new IllegalArgumentException("Erro ao inserir item do Ajuste Estoque");
                }
            }

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);

            throw new RuntimeException("Erro ao inserir movimento", e);
        } catch (IllegalArgumentException e) {
            rollback(conn);

            throw e;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar a conexão", e);
            }
        }
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

    public List<AjusteEstoque> buscarAjustePorStatus(Status status) {
        return ajusteEstoqueRepository.buscarAjustePorStatus(status);
    }

    public AjusteEstoque buscarAjustePorId(int idAjuste) {
        AjusteEstoque ajusteEstoque = ajusteEstoqueRepository.buscarAjustePorId(idAjuste);

        if (ajusteEstoque == null) {
            throw new IllegalArgumentException("Ajuste de Estoque não localizado");
        }

        return ajusteEstoque;
    }

    public List<AjusteEstoqueItens> buscarAjusteEstoqueItensEntrada(Long idAjuste) {
        return ajusteEstoqueRepository.buscarAjusteEstoqueItensEntrada(idAjuste);
    }

    public List<AjusteEstoqueItens> buscarAjusteEstoqueItensSaida(Long idAjuste) {
        return ajusteEstoqueRepository.buscarAjusteEstoqueItensSaida(idAjuste);
    }

    public void criarMovimentoAjusteEstoque(Long idAjuste) {
        Connection conn = null;

        List<AjusteEstoqueItens> ajusteEstoqueItensEntrada = buscarAjusteEstoqueItensEntrada(idAjuste);

        List<AjusteEstoqueItens> ajusteEstoqueItensSaida = buscarAjusteEstoqueItensSaida(idAjuste);

        try {
            conn = connectionProvider.getConnection();
            conn.setAutoCommit(false);

            if (!ajusteEstoqueItensEntrada.isEmpty()) {
                ;
                criarMovimento(conn, ajusteEstoqueItensEntrada, Tipo.ENTRADA);
            }

            if (!ajusteEstoqueItensSaida.isEmpty()) {
                criarMovimento(conn, ajusteEstoqueItensSaida, Tipo.SAIDA);
            }

            if (!ajusteEstoqueItensEntrada.isEmpty() || !ajusteEstoqueItensSaida.isEmpty()) {
                ajusteEstoqueRepository.mudarStatusMovimento(conn, StatusMovimentoCriado.FINALIZADO_CRIADO, idAjuste);
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao realizar rollback", ex);
            }

            throw new RuntimeException("Erro ao inserir movimento", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar a conexão", e);
            }
        }
    }

    private void criarMovimento(Connection conn, List<AjusteEstoqueItens> ajusteEstoqueItens, Tipo tipo) {
        List<MovimentoItem> movimentoItems = new ArrayList<>();
        for (AjusteEstoqueItens estoqueItens : ajusteEstoqueItens) {
            movimentoItems.add(MovimentoItem.builder()
                    .produto(produtoRepository.buscarPorId(estoqueItens.getProduto().getId()))
                    .quantidade(estoqueItens.getContagem())
                    .build());
        }

        Movimento movimento = Movimento.builder()
                .pessoa(pessoaRepository.buscarPorId(Pessoa.ID_PESSOA_PADRAO))
                .funcionario(pessoaRepository.buscarPorId(Pessoa.ID_PESSOA_PADRAO))
                .tipo(tipo)
                .statusMovimento(StatusMovimento.FINALIZADO)
                .movimentoItens(movimentoItems)
                .build();

        int idMovimento = movimentoRepository.inserirMovimento(conn, movimento);
        movimento.setId(idMovimento);

        for (MovimentoItem movimentoItem : movimento.getMovimentoItens()) {
            boolean isInseriu = movimentoItemRepository.inserirMovimentoItem(conn, idMovimento, movimentoItem);

            if (movimento.getStatusMovimento() == StatusMovimento.FINALIZADO) {
                historicoEstoqueService.movimentar(conn, movimentoItem.getProduto(), movimento,
                        movimentoItem.getQuantidade(), movimento.getTipo());
            }

            if (!isInseriu) {
                throw new IllegalArgumentException("Erro ao inserir item do movimento");
            }
        }
    }

}
