package service;

import conn.ConnectionProvider;
import domain.ajusteestoque.AjusteEstoque;
import domain.ajusteestoque.AjusteEstoqueItens;
import repository.AjusteEstoqueItemRepository;
import repository.AjusteEstoqueRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class AjusteEstoqueService {
    private final AjusteEstoqueRepository estoqueRepository;
    private final AjusteEstoqueItemRepository ajusteEstoqueItemRepository;
    private final ConnectionProvider connectionProvider;

    public AjusteEstoqueService(AjusteEstoqueRepository estoqueRepository,
                                AjusteEstoqueItemRepository ajusteEstoqueItemRepository,
                                ConnectionProvider connectionProvider) {
        this.estoqueRepository = estoqueRepository;
        this.ajusteEstoqueItemRepository = ajusteEstoqueItemRepository;
        this.connectionProvider = connectionProvider;
    }

    public void inserirAjusteEstoque(AjusteEstoque estoque) {
        Connection conn = null;

        try {
            conn = connectionProvider.getConnection();
            conn.setAutoCommit(false);

            Long idAjusteEstoque = estoqueRepository.inserirAjusteEstoque(conn, estoque);

            for (AjusteEstoqueItens item : estoque.getAjusteEstoqueItens()) {
                boolean isInseriu = ajusteEstoqueItemRepository.inserirAjusteEstoqueItem(conn, idAjusteEstoque, item);

                if (!isInseriu) {
                    throw new IllegalArgumentException("Erro ao inserir item do Ajuste Estoque");
                }
            }

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);

            throw new RuntimeException("Erro ao inserir movimento", e);
        } catch (IllegalArgumentException e) {
            rollback(conn);

            throw e;
        }finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar a conexão", e);
            }
        }
    }

    private void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Erro ao realizar rollback", ex);
        }
    }
}
