package service;

import domain.estoque.Estoque;
import repository.EstoqueRepository;

import java.math.BigDecimal;
import java.sql.Connection;

public class EstoqueService {
    private final EstoqueRepository estoqueRepository = new EstoqueRepository();

    public void salvarEstoque(Connection conn, int idProduto, BigDecimal saldo) {
        Estoque estoque = estoqueRepository.buscarPorIdProduto(conn, idProduto);

        if (estoque == null) {
            estoqueRepository.inserirEstoque(conn, idProduto, saldo);
            return;
        }
        estoqueRepository.atualizarEstoque(conn, idProduto, saldo);
    }

    public Estoque buscarPorIdProduto(Connection conn, int idProduto) {
        Estoque estoque = estoqueRepository.buscarPorIdProduto(conn, idProduto);
        if (estoque != null) {
            return estoque;
        }
        return Estoque.builder().idProduto(idProduto).quantidade(BigDecimal.ZERO).build();
    }
}
