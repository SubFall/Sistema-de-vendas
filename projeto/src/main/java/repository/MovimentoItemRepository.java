package repository;

import conn.ConnectionFactory;
import domain.movimento.Movimento;
import domain.movimento.MovimentoItem;

import java.sql.*;

public class MovimentoItemRepository {
    public boolean inserirMovimentoItem(Connection conn, int idMovimento, MovimentoItem item) {
        String sql = """
                INSERT INTO movimento_item (id_movimento, id_produto, quantidade, valor_unitario, valor_total) 
                VALUES (?, ?, ?, ?, ?);
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMovimento);
            ps.setInt(2, item.getProduto().getId());
            ps.setBigDecimal(3, item.getQuantidade());
            ps.setBigDecimal(4, item.getValorUnitario());
            ps.setBigDecimal(5, item.getValorTotal());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
