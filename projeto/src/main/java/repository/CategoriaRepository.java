package repository;

import conn.ConnectionProvider;
import domain.categoria.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepository {
    private final ConnectionProvider connectionProvider;

    public CategoriaRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public boolean inserirCategoria(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria (descricao) VALUES (?);";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoria.getDescricao());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deletarCategoria(Long idCategoria) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idCategoria);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean atualizarStatusCategoria(boolean ativo, Long idCategoria) throws SQLException {
        String sql = "UPDATE categoria SET ativo = ? WHERE id_categoria = ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, ativo);
            ps.setLong(2, idCategoria);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean atualizarCategoria(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria SET descricao = ?, ativo = ? WHERE id_categoria = ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoria.getDescricao());
            ps.setBoolean(2, categoria.isAtivo());
            ps.setLong(3, categoria.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean categoriaPossuiProdutos(Categoria categoria) throws SQLException {
        String sql = "SELECT 1 FROM categoria WHERE descricao = ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoria.getDescricao());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean categoriaPossuiProdutos(int idCategoria) throws SQLException {
        String sql = "SELECT 1 FROM produtos WHERE id_categoria = ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public Categoria buscarPorId(Long idCategoria) throws SQLException  {
        String sql = "SELECT id_categoria, descricao, ativo FROM categoria WHERE id_categoria = ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCategoria(rs);
                }
            }
        }
        return null;
    }

    public List<Categoria> buscarPorDescricao(String descricao) throws SQLException {
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
        }
        return categorias;
    }

    public List<Categoria> buscarTodos() throws SQLException {
        String sql = "SELECT id_categoria, descricao, ativo FROM categoria ORDER BY descricao;";
        List<Categoria> categorias = new ArrayList<>();

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categorias.add(mapearCategoria(rs));
            }
        }
        return categorias;
    }

    public boolean existeOutraCategoriaComDescricao(String descricao, Long idCategoria) throws SQLException {
        String sql = "SELECT 1 FROM categoria WHERE descricao = ? AND id_categoria <> ?;";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, descricao);
            ps.setLong(2, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Categoria mapearCategoria(ResultSet rs) throws SQLException {
        return Categoria.builder()
                .id(rs.getLong("id_categoria"))
                .descricao(rs.getString("descricao"))
                .ativo(rs.getBoolean("ativo"))
                .build();

    }
}
