package repository;

import conn.ConnectionFactory;
import domain.produto.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProdutoRepository {

    public boolean inserirProduto(Produto produto) {
        String sql = "INSERT INTO produtos (descricao, preco_venda, preco_custo, ativo) VALUES (?, ?, ?, ?);";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, produto.getDescricao());
            ps.setBigDecimal(2, produto.getPrecoVenda());
            ps.setBigDecimal(3, produto.getPrecoCusto());
            ps.setBoolean(4, produto.getAtivo());

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
