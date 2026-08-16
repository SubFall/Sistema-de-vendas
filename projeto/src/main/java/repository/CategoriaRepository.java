package repository;

import conn.ConnectionFactory;
import conn.ConnectionProvider;
import domain.categoria.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepository {
    private final ConnectionProvider connectionProvider = new ConnectionFactory();

    public boolean inserirCategoria(Categoria categoria) {
        String sql = "INSERT INTO categoria (descricao) VALUES (?);";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoria.getDescricao());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deletarCategoria(int idCategoria) {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean atualizarStatusCategoria(boolean ativo, int idCategoria) {
        String sql = "UPDATE categoria SET ativo = ? WHERE id_categoria = ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, ativo);
            ps.setInt(2, idCategoria);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean atualizarCategoria(Categoria categoria) {
        String sql = "UPDATE categoria SET descricao = ?, ativo = ? WHERE id_categoria = ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoria.getDescricao());
            ps.setBoolean(2, categoria.isAtivo());
            ps.setInt(3, categoria.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean categoriaPossuiProdutos(String categoria) {
        String sql = "SELECT 1 FROM categoria WHERE descricao = ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoria);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean categoriaPossuiProdutos(int idCategoria) {
        String sql = "SELECT 1 FROM produtos WHERE id_categoria = ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Categoria buscarPorId(int idCategoria) {
        String sql = "SELECT id_categoria, descricao, ativo FROM categoria WHERE id_categoria = ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCategoria(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Categoria> buscarPorDescricao(String descricao) {
        String sql = "SELECT id_categoria, descricao, ativo FROM categoria WHERE descricao LIKE ? ORDER BY descricao;";
        List<Categoria> categorias = new ArrayList<>();

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + descricao + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categorias.add(mapearCategoria(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categorias;
    }

    public List<Categoria> buscarTodos() {
        String sql = "SELECT id_categoria, descricao, ativo FROM categoria ORDER BY descricao;";
        List<Categoria> categorias = new ArrayList<>();

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categorias.add(mapearCategoria(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categorias;
    }

    public boolean existeOutraCategoriaComDescricao(String descricao, int idCategoria) {
        String sql = "SELECT 1 FROM categoria WHERE descricao = ? AND id_categoria <> ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, descricao);
            ps.setInt(2, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Categoria mapearCategoria(ResultSet rs) throws SQLException {
        return Categoria.builder()
                .id(rs.getInt("id_categoria"))
                .descricao(rs.getString("descricao"))
                .ativo(rs.getBoolean("ativo"))
                .build();

    }
}
