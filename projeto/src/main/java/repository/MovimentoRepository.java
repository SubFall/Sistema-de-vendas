package repository;

import conn.ConnectionFactory;
import domain.movimento.Movimento;

import java.sql.*;

public class MovimentoRepository {
    public int inserirMovimento(Connection conn, Movimento movimento) {
        String sql = "INSERT INTO movimento (id_pessoa, data_movimento, quantidade_itens, valor_total) VALUES (?, ?, ?, ?);";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1,movimento.getPessoa().getId());
            ps.setTimestamp(2, Timestamp.valueOf(movimento.getDataMovimento()));
            ps.setBigDecimal(3, movimento.getQuantidadeTotal());
            ps.setBigDecimal(4, movimento.getValorTotal());

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
}
