package repository;

import domain.ajusteestoque.AjusteEstoqueItens;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AjusteEstoqueItemRepository {
    public boolean inserirAjusteEstoqueItens(Connection conn, Long idAjusteEstoque, List<AjusteEstoqueItens> itens) {
        String sql = "INSERT INTO ajuste_estoque_itens (id_produto, saldo, contagem, diferenca, id_ajuste_estoque) " +
                "VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (AjusteEstoqueItens item : itens) {
                ps.setInt(1, item.getProduto().getId());
                ps.setBigDecimal(2, item.getEstoque().getQuantidade());
                ps.setBigDecimal(3, item.getContagem());
                ps.setBigDecimal(4, item.getDiferenca());
                ps.setLong(5, idAjusteEstoque);

                ps.addBatch();
            }

            int[] resultados = ps.executeBatch();

            return resultados.length == itens.size();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
