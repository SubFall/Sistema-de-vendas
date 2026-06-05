package repository;

import conn.ConnectionFactory;
import domain.produto.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
