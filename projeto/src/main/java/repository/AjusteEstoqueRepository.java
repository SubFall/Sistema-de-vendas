package repository;

import domain.ajusteestoque.AjusteEstoque;

import java.sql.*;

public class AjusteEstoqueRepository {
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
}
