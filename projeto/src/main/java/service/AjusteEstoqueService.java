package service;

import conn.ConnectionFactory;
import domain.ajusteestoque.AjusteEstoque;
import domain.ajusteestoque.AjusteEstoqueItens;
import repository.AjusteEstoqueItemRepository;
import repository.AjusteEstoqueRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class AjusteEstoqueService {
    private final AjusteEstoqueRepository estoqueRepository = new AjusteEstoqueRepository();
    private final AjusteEstoqueItemRepository ajusteEstoqueItemRepository = new AjusteEstoqueItemRepository();

    public void inserirAjusteEstoque(AjusteEstoque estoque) {
        Connection conn = null;

        try {
            conn = ConnectionFactory.getConnection();
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
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                throw new RuntimeException("Erro ao realizar rollback", ex);
            }

            throw new RuntimeException("Erro ao inserir movimento", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar a conexão", e);
            }
        }
    }
}
