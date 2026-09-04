package repository;

import conn.ConnectionProvider;
import domain.movimento.MovimentoItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovimentoItemRepository {
    private final ConnectionProvider connectionProvider;
    private final ProdutoRepository produtoRepository;

    public MovimentoItemRepository(ConnectionProvider connectionProvider, ProdutoRepository produtoRepository) {
        this.connectionProvider = connectionProvider;
        this.produtoRepository = produtoRepository;
    }

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

    public boolean deletarMovimentoItem(Connection conn, int idMovimento) {
        String sql = "DELETE FROM movimento_item WHERE id_movimento = ?;";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMovimento);

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

        try (Connection conn = connectionProvider.getConnection();
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
                .build();
    }
}
