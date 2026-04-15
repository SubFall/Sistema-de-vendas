package dev.thalysom.pessoas.service;

import dev.thalysom.pessoas.domain.Endereco;
import dev.thalysom.pessoas.domain.Pessoa;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CadastroPessoa {
    private List<Pessoa> pessoas = new ArrayList<>();

    public void adicionar(Pessoa pessoa) {
        if (documentoExiste(pessoa.getDocumento())) {
            throw new IllegalArgumentException("Documento já cadastrado!");
        }
        pessoas.add(pessoa);
    }

    public void remover(String documento) {
        documento = documento.replaceAll("\\D", "");

        String finalDocumento = documento;
        boolean removido = pessoas.removeIf(p -> p.getDocumento().equals(finalDocumento));

        if (removido) {
            System.out.println("Pessoa removida com sucesso!");
        } else {
            System.out.println("Pessoa não encontrada.");
        }
    }

    public Boolean documentoExiste(String documento) {
        documento = documento.replaceAll("\\D", "");

        for (Pessoa pessoa : pessoas) {
            if (pessoa.getDocumento().equals(documento)) {
                return true;
            }
        }
        return false;
    }

    public void listar() {
        if (pessoas.isEmpty()) {
            System.out.println("Nenhuma pessoa cadastrada.");
            return;
        }

        for (Pessoa pessoa : pessoas) {
            pessoa.mostrarDados();
        }

    }

    public List<Pessoa> buscar(Predicate<Pessoa> filtro ) {
        List<Pessoa> resultado = new ArrayList<>();

        for (Pessoa p : pessoas) {
            if (filtro.test(p)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public void adicionarEndereco(Pessoa pessoa, Endereco endereco) {
        pessoa.setEnderecos(endereco);
    }
}
