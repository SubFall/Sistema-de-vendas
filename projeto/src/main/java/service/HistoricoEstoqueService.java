package service;

import domain.estoque.Estoque;
import repository.EstoqueRepository;
import repository.HistoricoEstoqueRepository;

import java.math.BigDecimal;
import java.sql.Connection;

public class HistoricoEstoqueService {
//    private HistoricoEstoqueRepository historicoEstoqueRepository = new HistoricoEstoqueRepository();
//    private EstoqueRepository estoqueRepository = new EstoqueRepository();
//
//    public void inserirSaida(
//            Connection conn, int idProduto, int idMoivmento, int tipoMovimento, BigDecimal quantidade
//    ) {
//        Estoque estoque = estoqueRepository.buscarPorProduto(idProduto);
//        BigDecimal saldoAnterior = estoque.getQuantidade();
//        BigDecimal saldoAtual = estoque.getQuantidade().subtract(quantidade);
//
//          historicoEstoqueRepository.inserirHistoricoEstoque(
//                  conn, idProduto, idMoivmento, tipoMovimento, quantidade, saldoAnterior, saldoAtual
//          );
//    }
//
//    public void inserirEntrada(
//            Connection conn, int idProduto, int idMoivmento, int tipoMovimento, BigDecimal quantidade
//    ) {
//        Estoque estoque = estoqueRepository.buscarPorProduto(idProduto);
//        BigDecimal saldoAnterior = estoque.getQuantidade();
//        BigDecimal saldoAtual = estoque.getQuantidade().add(quantidade);
//
//        historicoEstoqueRepository.inserirHistoricoEstoque(
//                conn, idProduto, idMoivmento, tipoMovimento, quantidade, saldoAnterior, saldoAtual
//        );
//    }

}
