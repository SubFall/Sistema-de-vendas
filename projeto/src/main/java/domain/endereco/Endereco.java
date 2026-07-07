package domain.endereco;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Endereco {
    private int id;
    private String logradouro;
    private String cidade;
    private String uf;
    private String bairro;
    private String numero;
    private String cep;
    private int idPessoa;

    private Endereco(int id, String logradouro, String cidade, String uf, String bairro, String numero, String cep, int idPessoa) {
        this.id = id;
        this.logradouro = logradouro;
        this.cidade = cidade;
        this.uf = validaUF(uf);
        this.bairro = bairro;
        this.numero = numero;
        cep = cep.replaceAll("\\D", "");
        validaCep(cep);
        this.cep = cep;
        this.idPessoa = idPessoa;
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
        private int idPessoa;

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

        public EnderecoBuilder idPessoa(int idPessoa) {
            this.idPessoa = idPessoa;
            return this;
        }

        public Endereco build() {
            return new Endereco(id, logradouro, cidade, uf, bairro, numero, cep, idPessoa);
        }
    }

    private void validaCep(String cep) {
        if (cep.length() != 8) {
            throw new IllegalArgumentException("Cep Inválido!");
        }
    }

    private String validaUF(String uf) {
        uf = uf.trim().toUpperCase();

        if (!UFs().contains(uf)) {
            throw new IllegalArgumentException("Estado inválido.");
        }
        return uf;
    }

    public int getId() {
        return id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = validaUF(uf);
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        validaCep(cep);
        this.cep = cep;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    private static Set<String> UFs() {
        return Set.of(
                "AC", "AP", "AM", "PA", "RO", "RR", "TO",
                "MA", "PI", "CE", "RN", "PB", "PE", "AL", "SE", "BA",
                "GO", "MT", "MS", "DF",
                "SP", "RJ", "MG", "ES",
                "PR", "SC", "RS"
        );
    }

    @Override
    public String toString() {
        return logradouro + ", " + numero + " - " + cidade + "/" + uf + " - Bairro: " + bairro + " CEP: " + cep + "  idPessoa: " + idPessoa;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return Objects.equals(logradouro, endereco.logradouro)
                && Objects.equals(cidade, endereco.cidade)
                && Objects.equals(uf, endereco.uf)
                && Objects.equals(numero, endereco.numero)
                && Objects.equals(cep, endereco.cep)
                && Objects.equals(idPessoa, endereco.idPessoa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logradouro, cidade, uf, numero, cep, idPessoa);
    }
}
