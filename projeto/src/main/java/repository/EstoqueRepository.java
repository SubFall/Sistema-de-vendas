package repository;

import conn.ConnectionFactory;
import domain.estoque.Estoque;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EstoqueRepository {
//    ProdutoRepository produtoRepository = new ProdutoRepository();

    public boolean inserirEstoque(Connection conn, int idProduto, BigDecimal saldo) {
        String sql = "INSERT INTO estoque (id_produto, quantidade) VALUES (?, ?);";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProduto);
            ps.setBigDecimal(2, saldo);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean atualizarEstoque(Connection conn, int idProduto, BigDecimal saldo) {
        String sql = "UPDATE estoque SET quantidade = ? WHERE id_produto = ?;";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, saldo);
            ps.setInt(2, idProduto);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Estoque buscarPorIdProduto(Connection conn, int idProduto) {
        String sql = "SELECT id_estoque, id_produto, quantidade FROM estoque WHERE id_produto = ?;";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProduto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Estoque.builder()
                            .idProduto(rs.getInt("id_produto"))
                            .quantidade(rs.getBigDecimal("quantidade"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Estoque buscarPorIdProduto(int idProduto) {
        String sql = "SELECT id_estoque, id_produto, quantidade FROM estoque WHERE id_produto = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProduto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Estoque.builder()
                            .idProduto(rs.getInt("id_produto"))
                            .quantidade(rs.getBigDecimal("quantidade"))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
