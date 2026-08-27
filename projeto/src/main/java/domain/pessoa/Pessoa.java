package domain.pessoa;

import domain.documento.Documento;
import domain.endereco.Endereco;

import java.util.ArrayList;
import java.util.List;

public class Pessoa {
    private int id;
    private String nome;
    private Documento documento;
    private Endereco endereco;
    private List<PessoaPapel> papeis;
    private boolean ativo;

    public static  final int ID_PESSOA_PADRAO = 1;

    private Pessoa(int id, String nome, Documento documento, List<PessoaPapel> papeis, boolean ativo) {
        this.id = id;
        this.documento = documento;
        setNome(nome);
        setPapeis(papeis);
        this.ativo = ativo;
    }

    private Pessoa(int id, String nome, Documento documento, List<PessoaPapel> papeis, boolean ativo, Endereco endereco) {
        this(id, nome, documento, papeis, ativo);
        this.endereco = endereco;
    }

    public static PessoaBuilder builder() {
        return new PessoaBuilder();
    }

    public static final class PessoaBuilder {
        private int id;
        private String nome;
        private Documento documento;
        private Endereco endereco;
        private List<PessoaPapel> papeis = new ArrayList<>();
        private boolean ativo = true;

        public PessoaBuilder id(int id) {
            this.id = id;
            return this;
        }

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

        public PessoaBuilder papeis(List<PessoaPapel> papeis) {
            this.papeis = papeis;
            return this;
        }

        public PessoaBuilder ativo(boolean ativo) {
            this.ativo = ativo;
            return this;
        }

        public Pessoa build() {

            if (nome == null || nome.isBlank()) {
                throw new IllegalArgumentException("Nome não pode ser vázio");
            }

            if (documento == null || documento.getValor().isBlank()) {
                throw new IllegalArgumentException("Documento não pode ser vázio");
            }

            return new Pessoa(id, nome, documento, papeis, ativo, endereco);
        }
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vázio");
        }
        this.nome = nome;
    }

    public int getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setPapeis(List<PessoaPapel> papeis) {
        this.papeis = papeis;
    }

    public List<PessoaPapel> getPessoaPapel() {
        return this.papeis;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", documento=" + documento +
                ", endereco=" + endereco +
                ", papeis=" + papeis +
                ", ativo=" + ativo +
                '}';
    }
}