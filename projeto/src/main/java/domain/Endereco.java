package domain;

public class Endereco {
    private String logradouro;
    private String cidade;
    private String uf;
    private String numero;
    private String cep;

    public Endereco() {
    }

    public Endereco(String logradouro, String cidade, String uf, String numero, String cep) {
        this.logradouro = logradouro;
        this.cidade = cidade;
        this.uf = uf;
        this.numero = numero;
        this.cep = cep;
    }

    @Override
    public String toString() {
        return logradouro + ", " + numero + " - " + cidade + "/" + uf + " CEP: " + cep;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setCep(String cep) {
        String c = cep.replaceAll("\\D", "");

        if (c.length() != 8) {
            throw new IllegalArgumentException("Cep Inválido!");
        }
        this.cep = c;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getUf() {
        return uf;
    }

    public String getNumero() {
        return numero;
    }

    public String getCep() {
        return cep;
    }
}
