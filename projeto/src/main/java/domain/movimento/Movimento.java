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
    private Pessoa funcionario;
    private StatusMovimento statusMovimento;
    private LocalDateTime dataMovimento;
    private List<MovimentoItem> movimentoItens;

    public Movimento(int id, Pessoa pessoa, Pessoa funcionario, StatusMovimento statusMovimento,
                     LocalDateTime dataMovimento, List<MovimentoItem> movimentoItens) {
        this.id = id;
        this.pessoa = pessoa;
        this.funcionario = funcionario;
        this.statusMovimento = statusMovimento;
        this.dataMovimento = dataMovimento;
        this.movimentoItens = new ArrayList<>(movimentoItens);
    }

    public static MovimentoBuilder builder() {
        return new MovimentoBuilder();
    }

    public static final class MovimentoBuilder {
        private int id;
        private Pessoa pessoa;
        private Pessoa funcionario;
        private StatusMovimento statusMovimento = StatusMovimento.ABERTO;
        private LocalDateTime dataMovimento = LocalDateTime.now();
        private List<MovimentoItem> movimentoItens;

        public MovimentoBuilder id(int id) {
            this.id = id;
            return this;
        }

        public MovimentoBuilder pessoa(Pessoa pessoa) {
            this.pessoa = pessoa;
            return this;
        }

        public MovimentoBuilder funcionario(Pessoa funcionario) {
            this.funcionario = funcionario;
            return this;
        }

        public MovimentoBuilder statusMovimento(StatusMovimento statusMovimento) {
            this.statusMovimento = statusMovimento;
            return this;
        }

        public MovimentoBuilder dataMovimento(LocalDateTime dataMovimento) {
            this.dataMovimento = dataMovimento;
            return this;
        }

        public MovimentoBuilder movimentoItens(List<MovimentoItem> itens) {
            this.movimentoItens = new ArrayList<>(itens);
            return this;
        }

        public Movimento build() {

            if (pessoa == null) {
                throw new IllegalArgumentException("Pessoa obrigatória");
            }

            if (funcionario == null) {
                throw new IllegalArgumentException("Funcionário obrigatório");
            }

            if (movimentoItens.isEmpty()) {
                throw new IllegalArgumentException("Movimento sem itens");
            }
            return new Movimento(id, pessoa, funcionario, statusMovimento, dataMovimento, movimentoItens );
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

    public Pessoa getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Pessoa funcionario) {
        this.funcionario = funcionario;
    }

    public StatusMovimento getStatusMovimento() {
        return statusMovimento;
    }

    public void setStatusMovimento(StatusMovimento statusMovimento) {
        this.statusMovimento = statusMovimento;
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
                ", cliente=" + getPessoa().getNome() +
                ", vendedor=" + getFuncionario().getNome() +
                ", status=" + getStatusMovimento() +
                ", dataMovimento=" + dataMovimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
                ", valorTotal=" + getValorTotal() +
                ", quantidadeTotal=" + getQuantidadeTotal() +
                '}';
    }
}