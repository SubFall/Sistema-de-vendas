package repository;

import conn.ConnectionProvider;
import domain.categoria.Categoria;
import domain.produto.Produto;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {
    private final ConnectionProvider connectionProvider;

    public ProdutoRepository(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public boolean inserirProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (descricao, preco_venda, preco_custo, ativo, dt_criacao, id_categoria) VALUES (?, ?, ?, ?, ?, ?);";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, produto.getDescricao());
            ps.setBigDecimal(2, produto.getPrecoVenda());
            ps.setBigDecimal(3, produto.getPrecoCusto());
            ps.setBoolean(4, produto.isAtivo());
            ps.setTimestamp(5, Timestamp.valueOf(produto.getCreatedAt()));
            ps.setLong(6, produto.getCategoria().getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean atualizarStatusProduto(Produto produto) throws SQLException {
        String sql = "UPDATE produtos SET ativo = ?, dt_alteracao = ? WHERE (`id_produto` = ?);";

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, produto.isAtivo());
            ps.setTimestamp(2, Timestamp.valueOf(produto.getModifiedOn()));
            ps.setLong(3, produto.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean atualizarProduto(Produto produto) throws SQLException {
        String sql = """
                    UPDATE produtos SET descricao = ?, preco_venda = ?, preco_custo = ?, ativo = ?, 
                                        dt_alteracao = ?, id_categoria = ? WHERE id_produto = ?;
                """;

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, produto.getDescricao());
            ps.setBigDecimal(2, produto.getPrecoVenda());
            ps.setBigDecimal(3, produto.getPrecoCusto());
            ps.setBoolean(4, produto.isAtivo());
            ps.setTimestamp(5, Timestamp.valueOf(produto.getModifiedOn()));
            ps.setLong(6, produto.getCategoria().getId());
            ps.setLong(7, produto.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public List<Produto> buscarTodos() throws SQLException {
        String sql = """
                SELECT p.id_produto, p.descricao, p.preco_venda, p.preco_custo,
                		p.ativo, p.dt_criacao, p.dt_alteracao, p.id_categoria,c.descricao
                FROM produtos AS p LEFT JOIN categoria AS c ON c.id_categoria = p.id_categoria ORDER BY p.descricao;
                """;
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }
        }
        return produtos;
    }

    public List<Produto> buscarTodosPorStatus(boolean status) throws SQLException {
        String sql = """
                SELECT p.id_produto, p.descricao, p.preco_venda, p.preco_custo,
                		p.ativo, p.dt_criacao, p.dt_alteracao, p.id_categoria,c.descricao
                FROM produtos AS p LEFT JOIN categoria AS c ON c.id_categoria = p.id_categoria 
                WHERE p.ativo = ? ORDER BY p.descricao;
                """;
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, status);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    produtos.add(mapearProduto(rs));
                }
            }
        }
        return produtos;
    }

    public List<Produto> buscarPorDescricao(String descricao) throws SQLException {
        String sql = """
                SELECT p.id_produto, p.descricao, p.preco_venda, p.preco_custo,
                    		p.ativo, p.dt_criacao, p.dt_alteracao, p.id_categoria,c.descricao
                    FROM produtos AS p LEFT JOIN categoria AS c ON c.id_categoria = p.id_categoria
                    WHERE p.descricao LIKE ? ORDER BY p.descricao;
                """;
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + descricao.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    produtos.add(mapearProduto(rs));
                }
            }
        }
        return produtos;
    }

    public Produto buscarPorId(Long idProduto) throws SQLException {
        String sql = """
                        SELECT p.id_produto, p.descricao, p.preco_venda, p.preco_custo,
                            		p.ativo, p.dt_criacao, p.dt_alteracao, p.id_categoria, c.descricao
                            FROM produtos AS p LEFT JOIN categoria AS c ON c.id_categoria = p.id_categoria
                            WHERE id_produto = ?;
                """;

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idProduto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProduto(rs);
                }
            }
        }
        return null;
    }

    private Produto mapearProduto(ResultSet rs) throws SQLException {
        var produto = Produto.builder();

        produto.id(rs.getLong("p.id_produto"));
        produto.descricao(rs.getString("p.descricao"));
        produto.precoVenda(rs.getBigDecimal("p.preco_venda"));
        produto.precoCusto(rs.getBigDecimal("p.preco_custo"));
        produto.ativo(rs.getBoolean("p.ativo"));

        var dataCriacao = rs.getTimestamp("p.dt_criacao");
        if (dataCriacao != null) produto.createdAt(dataCriacao.toLocalDateTime());

        var dataModificacao = rs.getTimestamp("p.dt_alteracao");
        if (dataModificacao != null) produto.modifiedOn(dataModificacao.toLocalDateTime());

        produto.categoria(mapearCategoria(rs));
        return produto.build();
    }

    private Categoria mapearCategoria(ResultSet rs) throws SQLException {
        return Categoria.builder()
                .id(rs.getLong("p.id_categoria"))
                .descricao(rs.getString("c.descricao"))
                .build();
    }

}
