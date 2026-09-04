package service;

import domain.estoque.Estoque;
import domain.estoque.HistoricoEstoque;
import domain.movimento.Movimento;
import domain.movimento.Tipo;
import domain.produto.Produto;
import repository.HistoricoEstoqueRepository;

import java.math.BigDecimal;
import java.sql.Connection;

public class HistoricoEstoqueService {
    private final HistoricoEstoqueRepository historicoEstoqueRepository;
    private final EstoqueService estoqueService;

    public HistoricoEstoqueService(HistoricoEstoqueRepository historicoEstoqueRepository, EstoqueService estoqueService) {
        this.historicoEstoqueRepository = historicoEstoqueRepository;
        this.estoqueService = estoqueService;

    }

    public void movimentar(
            Connection conn, Produto produto, Movimento movimento, BigDecimal quantidade, Tipo tipo
    ) {

        Estoque estoque = estoqueService.buscarPorIdProduto(conn, produto.getId());

        if (tipo == Tipo.SAIDA && estoque.getQuantidade().compareTo(quantidade) < 0) {
            throw new IllegalArgumentException("Não permititdo! saldo insuficiente para a operação.");
        }

        BigDecimal bigDecimal = tipo == Tipo.SAIDA ? estoque.getQuantidade().subtract(quantidade)
                : estoque.getQuantidade().add(quantidade);

        BigDecimal saldoAnterior = estoque.getQuantidade();

        HistoricoEstoque historicoEstoque = HistoricoEstoque.builder()
                .produto(produto)
                .movimento(movimento)
                .tipo(tipo)
                .quantidade(quantidade)
                .saldoAnterior(saldoAnterior)
                .saldoAtual(bigDecimal)
                .build();

        historicoEstoqueRepository.inserirHistoricoEstoque(conn, historicoEstoque);

        estoqueService.salvarEstoque(conn, historicoEstoque.getProduto().getId(), historicoEstoque.getSaldoAtual());
    }

}
