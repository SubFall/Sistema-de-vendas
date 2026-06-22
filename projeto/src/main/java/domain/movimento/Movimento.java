package domain.movimento;

import domain.pessoa.Pessoa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Movimento {
    private int id;
    private Pessoa pessoa;
    private LocalDateTime dataMovimento;
    private List<MovimentoItem> movimentoItens;

    public Movimento(int id, Pessoa pessoa, LocalDateTime dataMovimento, List<MovimentoItem> movimentoItens) {
        this.id = id;
        this.pessoa = pessoa;
        this.dataMovimento = dataMovimento;
        this.movimentoItens = new ArrayList<>(movimentoItens);
    }

    public static MovimentoBuilder builder() {
        return new MovimentoBuilder();
    }

    public static final class MovimentoBuilder {
        private int id;
        private Pessoa pessoa;
        private LocalDateTime dataMovimento;
        private List<MovimentoItem> movimentoItens = new ArrayList<>();

        public MovimentoBuilder id(int id) {
            this.id = id;
            return this;
        }

        public MovimentoBuilder pessoa(Pessoa pessoa) {
            this.pessoa = pessoa;
            return this;
        }

        public MovimentoBuilder dataMovimento(LocalDateTime dataMovimento) {
            this.dataMovimento = LocalDateTime.now();
            return this;
        }

        public MovimentoBuilder movimentoItens(MovimentoItem item) {
            this.movimentoItens.add(item);
            return this;
        }

        public Movimento build() {

            if (pessoa == null) {
                throw new IllegalArgumentException("Pessoa obrigatória");
            }

            if (movimentoItens.isEmpty()) {
                throw new IllegalArgumentException("Movimento sem itens");
            }
            return new Movimento(id, pessoa, dataMovimento, movimentoItens);
        }
    }

    public int getId() {
        return id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public LocalDateTime getDataMovimento() {
        return dataMovimento;
    }

    public BigDecimal getValorTotal() {
        return movimentoItens.stream()
                .map(MovimentoItem::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getQuantidadeTotal() {
        return movimentoItens.stream()
                .map(MovimentoItem::getQuantidade)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MovimentoItem> getMovimentoItens() {
        return movimentoItens;
    }

    public void adicionarItem(MovimentoItem item) {
        this.movimentoItens.add(item);
    }

    @Override
    public String toString() {
        return "Movimento{" +
                "id=" + id +
                ", pessoa=" + getPessoa().getNome() +
                ", dataMovimento=" + dataMovimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
                ", valorTotal=" + getValorTotal() +
                ", quantidadeTotal=" + getQuantidadeTotal() +
                '}';
    }
}