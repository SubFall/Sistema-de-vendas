package repository;

import conn.ConnectionFactory;
import domain.movimento.Movimento;
import domain.movimento.StatusMovimento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovimentoRepository {
    PessoaRepository pessoaRepository = new PessoaRepository();
    MovimentoItemRepository movimentoItemRepository = new MovimentoItemRepository();

    public int inserirMovimento(Connection conn, Movimento movimento) {
        String sql = """
                INSERT INTO movimento (id_pessoa, id_funcionario, status, tipo_movimento, data_movimento, quantidade_itens, valor_total) 
                VALUES (?, ?, ?, ?, ?, ?, ?);
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, movimento.getPessoa().getId());
            ps.setInt(2, movimento.getFuncionario().getId());
            ps.setInt(3, movimento.getStatusMovimento().getCodigo());
            ps.setInt(4, movimento.getTipoMovimento().getCodigo());
            ps.setTimestamp(5, Timestamp.valueOf(movimento.getDataMovimento()));
            ps.setBigDecimal(6, movimento.getQuantidadeTotal());
            ps.setBigDecimal(7, movimento.getValorTotal());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            throw new IllegalArgumentException("Erro ao obter ID do movimento");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editarMovimento(Connection conn, Movimento movimento) {
        String sql = """
                UPDATE movimento SET id_pessoa = ?, id_funcionario = ?, status = ?, data_movimento = ?, quantidade_itens = ?, valor_total = ?
                WHERE id_movimento = ?;
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movimento.getPessoa().getId());
            ps.setInt(2, movimento.getFuncionario().getId());
            ps.setInt(3, movimento.getStatusMovimento().getCodigo());
            ps.setTimestamp(4, Timestamp.valueOf(movimento.getDataMovimento()));
            ps.setBigDecimal(5, movimento.getQuantidadeTotal());
            ps.setBigDecimal(6, movimento.getValorTotal());
            ps.setInt(7, movimento.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean reabrirMovimento(int idMovimento) {
        String sql = "UPDATE movimento SET status = ? WHERE id_movimento = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, StatusMovimento.ABERTO.getCodigo());
            ps.setInt(2, idMovimento);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean cancelarMovimento(int idMovimento) {
        String sql = "UPDATE movimento SET status = ? WHERE id_movimento = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, StatusMovimento.CANCELADO.getCodigo());
            ps.setInt(2, idMovimento);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean finalizarMovimento(int idMovimento) {
        String sql = "UPDATE movimento SET status = ? WHERE id_movimento = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, StatusMovimento.FINALIZADO.getCodigo());
            ps.setInt(2, idMovimento);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean recuperarMovimento(int idMovimento) {
        String sql = "UPDATE movimento SET status = ? WHERE id_movimento = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, StatusMovimento.ABERTO.getCodigo());
            ps.setInt(2, idMovimento);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Movimento buscarPorId(int idMovimento) {
        String sql = """
                SELECT id_movimento, id_pessoa, id_funcionario, status, data_movimento, quantidade_itens, valor_total 
                FROM movimento WHERE id_movimento = ?;
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMovimento);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMovimento(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Movimento buscarPorIdStatus(int idMovimento, StatusMovimento statusMovimento) {
        String sql = """
                SELECT id_movimento, id_pessoa, id_funcionario, status, data_movimento, quantidade_itens, valor_total 
                FROM movimento WHERE id_movimento = ? AND status = ?;
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idMovimento);
            ps.setInt(2, statusMovimento.getCodigo());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMovimento(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Movimento> buscarTodos() {
        List<Movimento> movimentos = new ArrayList<>();
        String sql = """
                SELECT id_movimento, id_pessoa, id_funcionario, status, data_movimento, quantidade_itens, valor_total 
                FROM movimento;
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                movimentos.add(mapearMovimento(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movimentos;
    }

    public List<Movimento> buscarPorStatus(StatusMovimento statusMovimento) {
        List<Movimento> movimentos = new ArrayList<>();
        String sql = """
                SELECT id_movimento, id_pessoa, id_funcionario, status, data_movimento, quantidade_itens, valor_total 
                FROM movimento WHERE status = ?;
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, statusMovimento.getCodigo());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    movimentos.add(mapearMovimento(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movimentos;
    }

    private Movimento mapearMovimento(ResultSet rs) throws SQLException {
        return Movimento.builder()
                .id(rs.getInt("id_movimento"))
                .pessoa(pessoaRepository.buscarPorId(rs.getInt("id_pessoa")))
                .funcionario(pessoaRepository.buscarPorId(rs.getInt("id_funcionario")))
                .statusMovimento(StatusMovimento.porCodigo(rs.getInt("status")))
                .dataMovimento(rs.getTimestamp("data_movimento").toLocalDateTime())
                .movimentoItens(movimentoItemRepository.buscarPorIdMovimento(rs.getInt("id_movimento")))
                .build();

    }
}
