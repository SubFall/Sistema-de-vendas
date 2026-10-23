package repository;

import conn.ConnectionProvider;
import domain.ajusteestoque.AjusteEstoque;
import domain.ajusteestoque.AjusteEstoqueItens;
import domain.ajusteestoque.Status;
import domain.ajusteestoque.StatusMovimentoCriado;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AjusteEstoqueRepository {
    private final ConnectionProvider connectionProvider;
    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;

    public Long inserirAjusteEstoque(Connection conn, AjusteEstoque estoque) throws SQLException {
        var sql = "INSERT INTO ajuste_estoque (titulo, data, status, status_movimento) VALUES (?, ?, ?, ?);";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, estoque.getTitulo());
            ps.setTimestamp(2, Timestamp.valueOf(estoque.getDateHora()));
            ps.setInt(3, estoque.getStatus().getCodigo());
            ps.setInt(4, estoque.getStatusMovimentoCriado().getCodigo());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            throw new SQLException("Erro ao obter ID do Ajuste Estoque.");
        }
    }

    public void atualizarAjusteEstoque(Connection conn, AjusteEstoque ajusteEstoque) throws SQLException {
        var sql = "UPDATE ajuste_estoque SET titulo = ?, status = ? WHERE id_ajuste_estoque = ?;";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ajusteEstoque.getTitulo());
            ps.setInt(2, ajusteEstoque.getStatus().getCodigo());
            ps.setLong(3, ajusteEstoque.getId());

            ps.executeUpdate();
        }
    }

    public List<AjusteEstoque> buscarAjustePorStatus(Status status) throws SQLException{
        List<AjusteEstoque> ajusteEstoques = new ArrayList<>();
        var sql = """
                SELECT id_ajuste_estoque, titulo, data, status, status_movimento FROM ajuste_estoque 
                WHERE status = ? AND status_movimento <> 1;
                """;

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status.getCodigo());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ajusteEstoques.add(mapearAjusteEstoque(rs));
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar Ajuste Estoque por Status.", e);
        }
        return ajusteEstoques;
    }

    public List<AjusteEstoque> buscarTodosAjusteEstoqueAberto() throws SQLException {
        List<AjusteEstoque> ajusteEstoques = new ArrayList<>();
        var sql = """
                SELECT id_ajuste_estoque, titulo, data, status, status_movimento FROM ajuste_estoque 
                WHERE status = ? AND status_movimento = ?;
                """;

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, Status.ABERTO.getCodigo());
            ps.setInt(2, StatusMovimentoCriado.MOVIMENTO_NAO_CRIADO.getCodigo());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ajusteEstoques.add(mapearAjusteEstoque(rs));
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar Ajuste Estoque aberto.", e);
        }
        return ajusteEstoques;
    }

    public List<AjusteEstoque> buscarTodosAjusteEstoque() throws SQLException{
        List<AjusteEstoque> ajusteEstoques = new ArrayList<>();
        var sql = """
                SELECT id_ajuste_estoque, titulo, data, status, status_movimento FROM ajuste_estoque;
                """;

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ajusteEstoques.add(mapearAjusteEstoque(rs));
            }

        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar Todos os Ajuste Estoque.", e);
        }
        return ajusteEstoques;
    }

    public List<AjusteEstoque> buscarAjusteEstoquePorTitulo(String titulo) throws SQLException {
        List<AjusteEstoque> ajusteEstoques = new ArrayList<>();
        var sql = """
                SELECT id_ajuste_estoque, titulo, data, status, status_movimento FROM ajuste_estoque
                WHERE titulo LIKE ?;
                """;

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, '%'+titulo+'%');

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ajusteEstoques.add(mapearAjusteEstoque(rs));
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar Ajuste Estoque por Título.", e);
        }
        return ajusteEstoques;
    }

    public AjusteEstoque buscarAjusteEstoquePorId(Long idAjuste) throws SQLException {
        var sql = "SELECT id_ajuste_estoque, titulo, data, status, status_movimento FROM ajuste_estoque WHERE id_ajuste_estoque = ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idAjuste);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearAjusteEstoque(rs);
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar Ajuste Estoque", e);
        }
        return null;
    }

    public List<AjusteEstoqueItens> buscarAjusteEstoqueItensEntrada(Long idAjuste) throws SQLException {
        List<AjusteEstoqueItens> ajusteEstoqueItens = new ArrayList<>();
        var sql = """
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
            throw new SQLException("Erro ao buscar Ajuste Estoque Itens Entrada", e);
        }

        return ajusteEstoqueItens;
    }

    public List<AjusteEstoqueItens> buscarAjusteEstoqueItensSaida(Long idAjuste) throws SQLException {
        List<AjusteEstoqueItens> ajusteEstoqueItens = new ArrayList<>();
        var sql = """
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
            throw new SQLException("Erro ao buscar Ajuste Estoque Itens Sáida");
        }

        return ajusteEstoqueItens;
    }

    public AjusteEstoqueItens buscarAjusteEstoqueItem(Long idAjuste) {
        var sql = """
                SELECT id_ajuste_estoque_itens, id_produto, saldo, contagem FROM ajuste_estoque_itens
                WHERE id_ajuste_estoque_itens = ?;
                """;

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idAjuste);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearAjusteEstoqueItens(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<AjusteEstoqueItens> buscarAjusteEstoqueItensList(Long idAjuste) {
        List<AjusteEstoqueItens> ajusteEstoqueItensList = new ArrayList<>();
        var sql = """
                SELECT id_ajuste_estoque_itens, id_produto, saldo, contagem FROM ajuste_estoque_itens
                WHERE id_ajuste_estoque = ?;
                """;

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idAjuste);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ajusteEstoqueItensList.add(mapearAjusteEstoqueItens(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ajusteEstoqueItensList;
    }

    public void mudarStatusMovimento(Connection conn, StatusMovimentoCriado statusMovimentoCriado, Long idAjuste) throws SQLException {
        var sql = "UPDATE ajuste_estoque SET status_movimento = ? WHERE (id_ajuste_estoque = ?);";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, statusMovimentoCriado.getCodigo());
            ps.setLong(2, idAjuste);

            if (ps.executeUpdate() != 1) {
                throw new SQLException("O ajuste de estoque não foi encontrado ou não pôde ser atualizado.");
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao atualizar o status dos movimentos do ajuste.", e);
        }
    }

    public void removerAjusteEstoque(Connection conn, AjusteEstoque ajusteEstoque) throws SQLException {
        var sql = "DELETE FROM ajuste_estoque WHERE id_ajuste_estoque = ?;";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, ajusteEstoque.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void removerAjusteEstoqueItem(Connection conn, AjusteEstoque ajusteEstoque) throws SQLException {
        var sql = "DELETE FROM ajuste_estoque_itens WHERE id_ajuste_estoque = ?;";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, ajusteEstoque.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    private AjusteEstoque mapearAjusteEstoque(ResultSet rs) throws SQLException {
        return AjusteEstoque.builder()
                .id(rs.getLong("id_ajuste_estoque"))
                .titulo(rs.getString("titulo"))
                .dateHora(rs.getTimestamp("data").toLocalDateTime())
                .status(Status.porCodigo(rs.getInt("status")))
                .statusMovimentoCriado(StatusMovimentoCriado.porCodigo(rs.getInt("status_movimento")))
                .build();
    }

    private AjusteEstoqueItens mapearAjusteEstoqueItens(ResultSet rs) throws SQLException {
        return AjusteEstoqueItens.builder()
                .id(rs.getLong("id_ajuste_estoque_itens"))
                .estoque(estoqueRepository.buscarPorIdProduto(rs.getLong("id_produto")))
                .produto(produtoRepository.buscarPorId(rs.getLong("id_produto")))
                .contagem(rs.getBigDecimal("contagem"))
                .build();
    }
}
