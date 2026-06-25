package service;

import conn.ConnectionFactory;
import domain.movimento.Movimento;
import domain.movimento.MovimentoItem;
import domain.movimento.StatusMovimento;
import domain.pessoa.PessoaPapel;
import repository.MovimentoItemRepository;
import repository.MovimentoRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class MovimentoService {
    MovimentoRepository movimentoRepository = new MovimentoRepository();
    MovimentoItemRepository movimentoItemRepository = new MovimentoItemRepository();

    public void inserirMovimento(Movimento movimento) {
        Connection conn = null;

        if (!movimento.getPessoa().getPessoaPapel().contains(PessoaPapel.CLIENTE)) {
            throw new IllegalArgumentException("Pessoa informada não é um cliente");
        }

        if (!movimento.getFuncionario().getPessoaPapel().contains(PessoaPapel.FUNCIONARIO)) {
            throw new IllegalArgumentException("Pessoa informada não é um funcionário");
        }

        if (movimento.getMovimentoItens().isEmpty()) {
            throw new IllegalArgumentException("Movimento sem itens");
        }

        if (movimento.getStatusMovimento() == StatusMovimento.CANCELADO) {
            throw new IllegalArgumentException("Movimento não pode ser criado cancelado");
        }

        try {
            conn = ConnectionFactory.getConnection();

            conn.setAutoCommit(false);

            int idMovimento = movimentoRepository.inserirMovimento(conn, movimento);

            for (MovimentoItem movimentoItem : movimento.getMovimentoItens()) {
                boolean inseriu = movimentoItemRepository.inserirMovimentoItem(conn, idMovimento, movimentoItem);

                if (!inseriu) {
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

    public boolean estornarMovimento(int idMovimento) {
        Movimento movimento = validarMovimentoFinalizado(
                validarMovimentoExiste(
                        movimentoRepository.buscarPorId(idMovimento)
                )
        );

        return movimentoRepository.reabrirMovimento(movimento.getId());
    }

    public Movimento buscarPorId(int idMovimento) {
        return movimentoRepository.buscarPorId(idMovimento);
    }

    private Movimento validarMovimentoExiste(Movimento movimento) {
        if (movimento == null) {
            throw new IllegalArgumentException("Movimento não encontrado");
        }
        return movimento;
    }

    private Movimento validarMovimentoFinalizado(Movimento movimento) {
        if (movimento.getStatusMovimento() != StatusMovimento.FINALIZADO) {
            throw new IllegalArgumentException("Movimento não está finalizado");
        }
        return movimento;
    }
}
