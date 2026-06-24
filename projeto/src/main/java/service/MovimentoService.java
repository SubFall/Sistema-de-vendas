package service;

import conn.ConnectionFactory;
import domain.movimento.Movimento;
import domain.movimento.MovimentoItem;
import repository.MovimentoItemRepository;
import repository.MovimentoRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class MovimentoService {
    MovimentoRepository movimentoRepository = new MovimentoRepository();
    MovimentoItemRepository movimentoItemRepository = new MovimentoItemRepository();

    public void inserirMovimento(Movimento movimento) {
        Connection conn = null;

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
}
