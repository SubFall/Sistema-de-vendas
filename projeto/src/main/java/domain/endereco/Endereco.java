package domain.endereco;

import java.util.Objects;

public class Endereco {
    private int id;
    private String logradouro;
    private String cidade;
    private String uf;
    private String bairro;
    private String numero;
    private String cep;

    private Endereco(int id, String logradouro, String cidade, String uf, String bairro, String numero, String cep) {
        this.id = id;
        this.logradouro = logradouro;
        this.cidade = cidade;
        this.uf = uf;
        this.bairro = bairro;
        this.numero = numero;
        cep = cep.replaceAll("\\D", "");
        validaCep(cep);
        this.cep = cep;
    }

    public static EnderecoBuilder builder() {
        return new EnderecoBuilder();
    }

    public static final class EnderecoBuilder {
        private int id;
        private String logradouro;
        private String cidade;
        private String uf;
        private String bairro;
        private String numero;
        private String cep;

        public EnderecoBuilder id(int id) {
            this.id = id;
            return this;
        }
        public EnderecoBuilder logradouro(String logradouro) {
            this.logradouro = logradouro;
            return this;
        }

        public EnderecoBuilder cidade(String cidade) {
            this.cidade = cidade;
            return this;
        }

        public EnderecoBuilder uf(String uf) {
            this.uf = uf;
            return this;
        }

        public EnderecoBuilder bairro(String bairro) {
            this.bairro = bairro;
            return this;
        }

        public EnderecoBuilder numero(String numero) {
            this.numero = numero;
            return this;
        }

        public EnderecoBuilder cep(String cep) {
            this.cep = cep;
            return this;
        }

        public Endereco build() {
            return new Endereco(id, logradouro, cidade, uf, bairro, numero, cep);
        }
    }

    private void validaCep(String cep) {
        if (cep.length() != 8) {
            throw new IllegalArgumentException("Cep Inválido!");
        }
    }

    public int getId() { return id; }

    public String getLogradouro() {
        return logradouro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getUf() {
        return uf;
    }

    public String getBairro() {
        return bairro;
    }

    public String getNumero() {
        return numero;
    }

    public String getCep() {
        return cep;
    }

    @Override
    public String toString() {
        return logradouro + ", " + numero + " - " + cidade + "/" + uf + " - Bairro: " + bairro + " CEP: " + cep;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return Objects.equals(logradouro, endereco.logradouro)
                && Objects.equals(cidade, endereco.cidade)
                && Objects.equals(uf, endereco.uf)
                && Objects.equals(numero, endereco.numero)
                && Objects.equals(cep, endereco.cep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logradouro, cidade, uf, numero, cep);
    }
}
