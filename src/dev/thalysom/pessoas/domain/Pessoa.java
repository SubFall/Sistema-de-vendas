package dev.thalysom.pessoas.domain;

public abstract class Pessoa {
    private String nome;
    private String documento;


    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vázio");
        }
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
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