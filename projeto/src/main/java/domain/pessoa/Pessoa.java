package domain.pessoa;

import domain.documento.CPF;
import domain.documento.Documento;
import domain.endereco.Endereco;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Pessoa {
    private int id;
    private String nome;
    private Documento documento;
    private Endereco endereco;
    private List<PessoaPapel> papeis;
    private boolean ativo;

    public static final int ID_PESSOA_PADRAO = 1;

    @Builder
    private Pessoa(int id, String nome, Documento documento, List<PessoaPapel> papeis, boolean ativo, Endereco endereco) {

        if (this.id == 1) {
            this.nome = "PADRAO";
            this.documento = new CPF("00000000000");
        }

        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome não pode ser vázio");

        if (documento == null || documento.getValor().isBlank())
            throw new IllegalArgumentException("Documento não pode ser vázio");

        this.id = id;
        this.documento = documento;
        setNome(nome);
        setPapeis(papeis);
        this.ativo = ativo;
        this.endereco = endereco;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vázio");
        }
        this.nome = nome;
    }
}