package service;

import conn.ConnectionFactory;
import domain.movimento.Movimento;
import domain.movimento.MovimentoItem;
import domain.movimento.StatusMovimento;
import domain.pessoa.PessoaPapel;
import domain.produto.Produto;
import repository.MovimentoItemRepository;
import repository.MovimentoRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MovimentoService {
    MovimentoRepository movimentoRepository = new MovimentoRepository();
    MovimentoItemRepository movimentoItemRepository = new MovimentoItemRepository();
    HistoricoEstoqueService historicoEstoqueService = new HistoricoEstoqueService();

    public void inserirMovimento(Movimento movimento) {
        Connection conn = null;

        validarMovimentoParaGravacao(movimento);

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            int idMovimento = movimentoRepository.inserirMovimento(conn, movimento);

            for (MovimentoItem movimentoItem : movimento.getMovimentoItens()) {
                boolean isInseriu = movimentoItemRepository.inserirMovimentoItem(conn, idMovimento, movimentoItem);

                if (movimento.getStatusMovimento() == StatusMovimento.FINALIZADO) {
                    historicoEstoqueService.movimentar(conn, movimentoItem.getProduto().getId(), idMovimento,
                            movimentoItem.getQuantidade(), movimento.getTipo());
                }

                if (!isInseriu) {
                    throw new IllegalArgumentException("Erro ao inserir item do movimento");
                }
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

    public void editarMovimento(Movimento movimento) {
        Movimento movimentoOriginal = validarMovimentoNaoNulo(buscarPorId(movimento.getId()));

        validarMovimentoEditavel(movimentoOriginal);

        validarMovimentoNaoNulo(movimento);
        validarMovimentoParaGravacao(movimento);

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            movimentoRepository.editarMovimento(conn, movimento);
            movimentoItemRepository.deletarMovimentoItem(conn, movimento.getId());

            for (MovimentoItem movimentoItem : movimento.getMovimentoItens()) {
                validarProdutoAtivo(movimentoItem.getProduto());
                movimentoItemRepository.inserirMovimentoItem(conn, movimento.getId(), movimentoItem);

                if (movimento.getStatusMovimento() == StatusMovimento.FINALIZADO) {
                    historicoEstoqueService.movimentar(conn, movimentoItem.getProduto().getId(),
                            movimento.getId(), movimentoItem.getQuantidade(), movimento.getTipo());
                }
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
            throw new RuntimeException(e);
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

    public boolean reabrirMovimento(int idMovimento) {
        Movimento movimento = validarMovimentoNaoFinalizado(
                validarMovimentoNaoNulo(
                        movimentoRepository.buscarPorId(idMovimento)
                )
        );
        return movimentoRepository.reabrirMovimento(movimento.getId());
    }

    public boolean cancelarMovimento(int idMovimento) {
        Movimento movimento = validarMovimentoPodeSerCancelado(
                validarMovimentoNaoNulo(buscarPorId(idMovimento))
        );
        return movimentoRepository.cancelarMovimento(movimento.getId());
    }

    public boolean finalizarMovimento(int idMovimento) {
        Movimento movimento = validarMovimentoNaoNulo(buscarPorId(idMovimento));
        validarMovimentoAberto(movimento);

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            for (MovimentoItem movimentoItem : movimento.getMovimentoItens()) {

                historicoEstoqueService.movimentar(conn, movimentoItem.getProduto().getId(),
                        movimento.getId(), movimentoItem.getQuantidade(), movimento.getTipo());
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

            throw new RuntimeException("Erro ao inserir estoque", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar a conexão", e);
            }
        }
        return movimentoRepository.finalizarMovimento(movimento.getId());
    }

    public boolean recuperarMovimento(int idMovimento) {
        Movimento movimento = validarMovimentoNaoNulo(buscarPorId(idMovimento));
        validarMovimentoEstaCancelado(movimento);
        return movimentoRepository.recuperarMovimento(movimento.getId());
    }

    public Movimento buscarPorId(int idMovimento) {
        return validarMovimentoNaoNulo(movimentoRepository.buscarPorId(idMovimento));
    }

    public Movimento buscarPorIdStatus(int idMovimento, StatusMovimento statusMovimento) {
        return validarMovimentoNaoNulo(movimentoRepository.buscarPorIdStatus(idMovimento, statusMovimento));
    }

    public List<Movimento> buscarTodos() {
        return movimentoRepository.buscarTodos();
    }

    public List<Movimento> buscarPorStatus(StatusMovimento statusMovimento) {
        return movimentoRepository.buscarPorStatus(statusMovimento);
    }

    private Movimento validarMovimentoNaoNulo(Movimento movimento) {
        if (movimento == null) {
            throw new IllegalArgumentException("Movimento não encontrado");
        }
        return movimento;
    }

    private Movimento validarMovimentoNaoFinalizado(Movimento movimento) {
        if (movimento.getStatusMovimento() != StatusMovimento.FINALIZADO) {
            throw new IllegalArgumentException("Movimento não está finalizado");
        }
        return movimento;
    }

    private Movimento validarMovimentoPodeSerCancelado(Movimento movimento) {
        if (movimento.getStatusMovimento() == StatusMovimento.CANCELADO) {
            throw new IllegalArgumentException("Movimento já está cancelado");
        }

        if (movimento.getStatusMovimento() == StatusMovimento.FINALIZADO) {
            throw new IllegalArgumentException("Movimento finalizado deve ser estornado");
        }
        return movimento;
    }

    private Movimento validarMovimentoParaGravacao(Movimento movimento) {
        if (!movimento.getPessoa().isAtivo()) {
            throw new IllegalArgumentException("Cliente informado está inativo");
        }

        if (!movimento.getPessoa().getPessoaPapel().contains(PessoaPapel.CLIENTE)) {
            throw new IllegalArgumentException("Pessoa informada não é um cliente");
        }

        if (!movimento.getFuncionario().isAtivo()) {
            throw new IllegalArgumentException("Funcionário informado está inativo");
        }

        if (!movimento.getFuncionario().getPessoaPapel().contains(PessoaPapel.FUNCIONARIO)) {
            throw new IllegalArgumentException("Pessoa informada não é um funcionário");
        }

        if (movimento.getMovimentoItens().isEmpty()) {
            throw new IllegalArgumentException("Movimento sem itens");
        }

        if (movimento.getStatusMovimento() == StatusMovimento.CANCELADO) {
            throw new IllegalArgumentException("Movimento não pode ser salvo com status cancelado");
        }
        return movimento;
    }

    private void validarProdutoAtivo(Produto produto) {
        if (!produto.isAtivo()) {
            throw new IllegalArgumentException("Produto " + produto.getDescricao() + " está inativo");
        }
    }

    private void validarMovimentoEditavel(Movimento movimentoOriginal) {
        validarMovimentoCancelado(movimentoOriginal);
        validarMovimentoFinalizado(movimentoOriginal);
    }

    private void validarMovimentoCancelado(Movimento movimento) {
        if (movimento.getStatusMovimento() == StatusMovimento.CANCELADO) {
            throw new IllegalArgumentException("Movimento cancelado não pode ser alterado.");
        }
    }

    private void validarMovimentoFinalizado(Movimento movimentoOriginal) {
        if (movimentoOriginal.getStatusMovimento() == StatusMovimento.FINALIZADO) {
            throw new IllegalArgumentException("Movimento finalizado não pode ser alterado.");
        }
    }

    private void validarMovimentoAberto(Movimento movimento) {
        if (movimento.getStatusMovimento() != StatusMovimento.ABERTO) {
            throw new IllegalArgumentException("Movimento não está em aberto.");
        }
    }

    private void validarMovimentoEstaCancelado(Movimento movimento) {
        if (movimento.getStatusMovimento() != StatusMovimento.CANCELADO) {
            throw new IllegalArgumentException("Movimento não está em cancelado.");
        }
    }

}
