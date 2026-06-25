package repository;

import conn.ConnectionFactory;
import domain.movimento.Movimento;
import domain.movimento.MovimentoItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimentoItemRepository {
    ProdutoRepository produtoRepository = new ProdutoRepository();

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

    public List<MovimentoItem> buscarPorIdMovimento(int idMovimento) {
        List<MovimentoItem> itens = new ArrayList<>();
        String sql = """
                SELECT id_movimento_item, id_movimento, id_produto, quantidade, valor_unitario, valor_total 
                FROM movimento_item WHERE id_movimento = ?;
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMovimento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    itens.add(mapearMovimentoItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return itens;
    }

    private MovimentoItem mapearMovimentoItem(ResultSet rs) throws SQLException {
        return MovimentoItem.builder()
                .id(rs.getInt("id_movimento_item"))
                .produto(produtoRepository.buscarPorId(rs.getInt("id_produto")))
                .quantidade(rs.getBigDecimal("quantidade"))
                .valorUnitario(rs.getBigDecimal("valor_unitario"))
                .build();
    }
}
