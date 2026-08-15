package repository;

import domain.ajusteestoque.AjusteEstoqueItens;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AjusteEstoqueItemRepository {
    public boolean inserirAjusteEstoqueItem(Connection conn, Long idAjusteEstoque, AjusteEstoqueItens itens) {
        String sql = "INSERT INTO ajuste_estoque_itens (id_produto, saldo, contagem, diferenca, id_ajuste_estoque) " +
                "VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itens.getProduto().getId());
            ps.setBigDecimal(2, itens.getEstoque().getQuantidade());
            ps.setBigDecimal(3, itens.getContagem());
            ps.setBigDecimal(4, itens.getDiferenca());
            ps.setLong(5, idAjusteEstoque);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
