package dev.thalysom.pessoas.domain;

import java.util.ArrayList;
import java.util.List;

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
}
