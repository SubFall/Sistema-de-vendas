package service;

import domain.estoque.Estoque;
import domain.movimento.Tipo;
import repository.EstoqueRepository;
import repository.HistoricoEstoqueRepository;

import java.math.BigDecimal;
import java.sql.Connection;

public class HistoricoEstoqueService {
    private final HistoricoEstoqueRepository historicoEstoqueRepository = new HistoricoEstoqueRepository();
    private final EstoqueRepository estoqueRepository = new EstoqueRepository();
    private final EstoqueService estoqueService = new EstoqueService();


    public void movimentar(
            Connection conn, int idProduto, int idMoivmento, BigDecimal quantidade, Tipo tipo
    ) {
        Estoque estoque = estoqueRepository.buscarPorIdProduto(conn, idProduto);
        BigDecimal saldoAnterior = estoque.getQuantidade();
        BigDecimal saldoAtual = tipo == Tipo.SAIDA
                ? estoque.getQuantidade().subtract(quantidade)
                : estoque.getQuantidade().add(quantidade);

        historicoEstoqueRepository.inserirHistoricoEstoque(
                conn, idProduto, idMoivmento, tipo.getCodigo(), quantidade, saldoAnterior, saldoAtual
        );

        estoqueService.salvarEstoque(conn, idProduto, saldoAtual);
    }

}
