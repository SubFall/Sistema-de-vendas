package repository;

import domain.estoque.HistoricoEstoque;
import domain.movimento.Movimento;
import domain.movimento.Tipo;
import domain.produto.Produto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoricoEstoqueRepository {
    private ProdutoRepository produtoRepository = new ProdutoRepository();
    private MovimentoRepository movimentoRepository = new MovimentoRepository();

    public boolean inserirHistoricoEstoque(
            Connection conn, int idProduto, int idMoivmento, int tipo,
            BigDecimal quantidade, BigDecimal saldoAnterior, BigDecimal saldoAtual
    ) {
        String sql = """
                INSERT INTO historico_estoque (id_produto, id_movimento, tipo, quantidade, saldo_anterior, saldo_atual)
                VALUES (?, ?, ?, ?, ?, ?);
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProduto);
            ps.setInt(2, idMoivmento);
            ps.setInt(3, tipo);
            ps.setBigDecimal(4, quantidade);
            ps.setBigDecimal(5, saldoAnterior);
            ps.setBigDecimal(6, saldoAtual);

            return ps.executeUpdate() > 0;
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public HistoricoEstoque buscarPorId(Connection conn, int idProduto) {
        String sql = """
                SELECT id_produto, id_movimento, tipo, quantidade, saldo_anterior, saldo_atual 
                FROM historico_estoque WHERE id_produto = ?;
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProduto);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Produto produto = produtoRepository.buscarPorId(rs.getInt("id_produto"));
                Movimento movimento = movimentoRepository.buscarPorId(rs.getInt("id_movimento"));

                return HistoricoEstoque.builder()
                        .produto(produto)
                        .movimento(movimento)
                        .tipo(Tipo.porCodigo(rs.getInt("tipo")))
                        .quantidade(rs.getBigDecimal("quantidade"))
                        .saldoAnterior(rs.getBigDecimal("saldo_anterior"))
                        .saldoAtual(rs.getBigDecimal("saldo_atual"))
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
