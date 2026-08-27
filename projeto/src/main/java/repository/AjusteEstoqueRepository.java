package repository;

import conn.ConnectionFactory;
import conn.ConnectionProvider;
import domain.ajusteestoque.AjusteEstoque;
import domain.ajusteestoque.AjusteEstoqueItens;
import domain.ajusteestoque.Status;
import domain.ajusteestoque.StatusMovimentoCriado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AjusteEstoqueRepository {
    private final ConnectionProvider connectionProvider = new ConnectionFactory();
    private final ProdutoRepository produtoRepository = new ProdutoRepository();
    private final EstoqueRepository estoqueRepository = new EstoqueRepository();

    public Long inserirAjusteEstoque(Connection conn, AjusteEstoque estoque) {
        String sql = "INSERT INTO ajuste_estoque (titulo, data, status) VALUES (?, ?, ?);";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, estoque.getTitulo());
            ps.setTimestamp(2, Timestamp.valueOf(estoque.getDateHora()));
            ps.setInt(3, estoque.getStatus().getCodigo());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            throw new IllegalArgumentException("Erro ao obter ID do Ajuste Estoque");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AjusteEstoque> buscarAjustePorStatus(Status status) {
        List<AjusteEstoque> ajusteEstoques = new ArrayList<>();
        String sql = "SELECT id_ajuste_estoque, titulo, data, status FROM ajuste_estoque WHERE status = ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status.getCodigo());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ajusteEstoques.add(mapearAjusteEstoque(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ajusteEstoques;
    }

    public AjusteEstoque buscarAjustePorId(int idAjuste) {
        String sql = "SELECT id_ajuste_estoque, titulo, data, status FROM ajuste_estoque WHERE id_ajuste_estoque = ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAjuste);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearAjusteEstoque(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<AjusteEstoqueItens> buscarAjusteEstoqueItensEntrada(Long idAjuste) {
        List<AjusteEstoqueItens> ajusteEstoqueItens = new ArrayList<>();
        String sql = """
                SELECT id_ajuste_estoque_itens, id_produto, saldo, contagem FROM ajuste_estoque_itens
                WHERE id_ajuste_estoque = ? AND contagem > 0;
                """;

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idAjuste);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ajusteEstoqueItens.add(mapearAjusteEstoqueItens(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ajusteEstoqueItens;
    }

    public List<AjusteEstoqueItens> buscarAjusteEstoqueItensSaida(Long idAjuste) {
        List<AjusteEstoqueItens> ajusteEstoqueItens = new ArrayList<>();
        String sql = """
                SELECT id_ajuste_estoque_itens, id_produto, saldo, contagem FROM ajuste_estoque_itens
                WHERE id_ajuste_estoque = ? AND contagem < 0;
                """;

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idAjuste);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ajusteEstoqueItens.add(mapearAjusteEstoqueItens(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ajusteEstoqueItens;
    }

    public void mudarStatusMovimento(Connection conn, StatusMovimentoCriado statusMovimentoCriado, Long idAjuste) {
        String sql = "UPDATE ajuste_estoque SET status_movimento = ? WHERE (id_ajuste_estoque = ?);";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, statusMovimentoCriado.getCodigo());
            ps.setLong(2, idAjuste);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AjusteEstoque mapearAjusteEstoque(ResultSet rs) throws SQLException {
        return AjusteEstoque.builder()
                .id(rs.getLong("id_ajuste_estoque"))
                .titulo(rs.getString("titulo"))
                .dateHora(rs.getTimestamp("data").toLocalDateTime())
                .status(Status.porCodigo(rs.getInt("status")))
                .build();
    }

    private AjusteEstoqueItens mapearAjusteEstoqueItens(ResultSet rs) throws SQLException {
        return AjusteEstoqueItens.builder()
                .id(rs.getLong("id_ajuste_estoque_itens"))
                .estoque(estoqueRepository.buscarPorIdProduto(rs.getInt("id_produto")))
                .produto(produtoRepository.buscarPorId(rs.getInt("id_produto")))
                .contagem(rs.getBigDecimal("contagem"))
                .build();
    }
}
