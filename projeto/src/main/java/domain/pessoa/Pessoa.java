package domain.pessoa;

import domain.documento.CPF;
import domain.documento.Documento;
import domain.endereco.Endereco;

public class Pessoa {
    private int id;
    private String nome;
    private Documento documento;
    private Endereco endereco;

    private Pessoa(String nome, Documento documento) {
        this.documento = documento;
        setNome(nome);
    }

    private Pessoa(String nome, Documento documento, Endereco endereco) {
        this(nome, documento);
        this.endereco = endereco;
    }

    public static PessoaBuilder builder() {
        return new PessoaBuilder();
    }

    public static final class PessoaBuilder {
        private String nome;
        private Documento documento;
        private Endereco endereco;

        public PessoaBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public PessoaBuilder documento(Documento documento) {
            this.documento = documento;
            return this;
        }

        public PessoaBuilder endereco(Endereco endereco) {
            this.endereco = endereco;
            return this;
        }

        public Pessoa build() {
            return new Pessoa(nome, documento, endereco);
        }
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vázio");
        }
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", documento=" + documento +
                ", enderecos=" + endereco +
                '}';
    }
}