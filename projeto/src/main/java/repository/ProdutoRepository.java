package repository;

import conn.ConnectionFactory;
import domain.produto.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    public boolean inserirProduto(Produto produto) {
        String sql = "INSERT INTO produtos (descricao, preco_venda, preco_custo, ativo, id_catergoria) VALUES (?, ?, ?, ?, ?);";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, produto.getDescricao());
            ps.setBigDecimal(2, produto.getPrecoVenda());
            ps.setBigDecimal(3, produto.getPrecoCusto());
            ps.setBoolean(4, produto.getAtivo());
//            ps.setInt(5, );

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean atualizarStatusProduto(boolean ativo, int idProduto) {
        String sql = "UPDATE produtos SET ativo = ? WHERE (`id_produto` = ?);";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, ativo);
            ps.setInt(2, idProduto);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean atualizarProduto(Produto produto) {
        String sql = "UPDATE produtos SET descricao = ?, preco_venda = ?, preco_custo = ?, ativo = ? WHERE id_produto = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, produto.getDescricao());
            ps.setBigDecimal(2, produto.getPrecoVenda());
            ps.setBigDecimal(3, produto.getPrecoCusto());
            ps.setBoolean(4, produto.getAtivo());
            ps.setInt(5, produto.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Produto> buscarTodos() {
        String sql = "SELECT id_produto, descricao, preco_venda, preco_custo, ativo FROM produtos ORDER BY descricao;";
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return produtos;
    }

    public List<Produto> buscarPorDescricao(String descricao) {
        String sql = """
                SELECT id_produto, descricao, preco_venda, preco_custo, ativo 
                FROM produtos WHERE descricao LIKE ? ORDER BY descricao;
                """;
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%"+descricao.trim()+"%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    produtos.add(mapearProduto(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return produtos;
    }

    public Produto buscarPorId(int idProduto) {
        String sql = "select id_produto, descricao, preco_venda, preco_custo, ativo from produtos where id_produto = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idProduto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProduto(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Produto mapearProduto(ResultSet rs) throws SQLException {
        return Produto.builder()
                .id(rs.getInt("id_produto"))
                .descricao(rs.getString("descricao"))
                .precoVenda(rs.getBigDecimal("preco_venda"))
                .precoCusto(rs.getBigDecimal("preco_custo"))
                .ativo(rs.getBoolean("ativo"))
                .build();
    }

}
