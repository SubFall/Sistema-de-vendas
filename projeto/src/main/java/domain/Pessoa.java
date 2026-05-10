package domain;

import java.util.ArrayList;
import java.util.List;

public abstract class Pessoa {
    private String nome;
    private String documento;
    private List<Endereco> enderecos = new ArrayList<>();


    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vázio");
        }
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }

    public void adicionarEndereco(Endereco endereco) {
        this.enderecos.add(endereco);
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setDocumento(String documento) {
        validarDocumento(documento);
        this.documento = documento;
    }

    public String getDocumento() {
        return this.documento;
    }

    public abstract void validarDocumento(String documento);

    public abstract void mostrarDados();

}