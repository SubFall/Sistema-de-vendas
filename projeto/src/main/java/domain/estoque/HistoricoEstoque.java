package domain.estoque;

import domain.movimento.Movimento;
import domain.movimento.Tipo;
import domain.produto.Produto;

import java.math.BigDecimal;

public class HistoricoEstoque {
    private Produto produto;
    private Movimento movimento;
    private Tipo tipo;
    private BigDecimal quantidade;
    private BigDecimal saldoAnterior;
    private BigDecimal saldoAtual;

    private HistoricoEstoque(Produto produto, Movimento movimento, Tipo tipo, BigDecimal quantidade, BigDecimal saldoAnterior, BigDecimal saldoAtual) {
        this.produto = produto;
        this.movimento = movimento;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.saldoAnterior = saldoAnterior;
        this.saldoAtual = saldoAtual;
    }

    public static HistoricoEstoqueBuilder builder() {
        return new HistoricoEstoqueBuilder();
    }

    public static final class HistoricoEstoqueBuilder {
        private Produto produto;
        private Movimento movimento;
        private Tipo tipo;
        private BigDecimal quantidade;
        private BigDecimal saldoAnterior;
        private BigDecimal saldoAtual;

        public HistoricoEstoqueBuilder produto(Produto produto) {
            this.produto = produto;
            return this;
        }

        public HistoricoEstoqueBuilder movimento(Movimento movimento) {
            this.movimento = movimento;
            return this;
        }

        public HistoricoEstoqueBuilder tipo(Tipo tipo) {
            this.tipo = tipo;
            return this;
        }

        public HistoricoEstoqueBuilder quantidade(BigDecimal quantidade) {
            this.quantidade = quantidade;
            return this;
        }

        public HistoricoEstoqueBuilder saldoAnterior(BigDecimal saldoAnterior) {
            this.saldoAnterior = saldoAnterior;
            return this;
        }

        public HistoricoEstoqueBuilder saldoAtual(BigDecimal saldoAtual) {
            this.saldoAtual = saldoAtual;
            return this;
        }

        public HistoricoEstoque build() {
            return new HistoricoEstoque(produto, movimento, tipo, quantidade, saldoAnterior, saldoAtual);
        }
    }

    public Produto getProduto() {
        return produto;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public Tipo getTipoMovimento() {
        return tipo;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public BigDecimal getSaldoAnterior() {
        return saldoAnterior;
    }

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }
}
