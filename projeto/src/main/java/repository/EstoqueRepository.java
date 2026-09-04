package repository;

import conn.ConnectionProvider;
import domain.estoque.Estoque;
import dto.ProdutoEstoqueDTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstoqueRepository {
    private final ConnectionProvider connectionProvider;

    public EstoqueRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

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

        try (Connection conn = connectionProvider.getConnection();
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

    public List<ProdutoEstoqueDTO> buscarProdutosEstoque() {
        List<ProdutoEstoqueDTO> produtoEstoque = new ArrayList<>();
        String sql = """
                SELECT
                	p.id_produto, p.descricao, COALESCE(e.quantidade, 0) AS quantidade
                FROM produtos p LEFT JOIN estoque e ON e.id_produto = p.id_produto;
                """;
        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                produtoEstoque.add(mapearProdutosEstoque(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return produtoEstoque;
    }

    private ProdutoEstoqueDTO mapearProdutosEstoque(ResultSet rs) throws SQLException {
        return ProdutoEstoqueDTO.builder()
                .idProduto(rs.getLong("id_produto"))
                .descricao(rs.getString("descricao"))
                .quantidade(rs.getBigDecimal("quantidade"))
                .build();
    }
}
