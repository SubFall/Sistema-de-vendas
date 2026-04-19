package dev.thalysom.pessoas.repository;

import dev.thalysom.pessoas.domain.Pessoa;

import java.util.*;
import java.util.function.Predicate;

public class PessoaRepository {

    private Map<String, Pessoa> pessoas = new HashMap<>();

    public void salvar(Pessoa pessoa) {
        String doc = normalizar(pessoa.getDocumento());

        if (pessoas.containsKey(doc)) {
            throw new IllegalArgumentException("Documento já cadastrado!");
        }

        pessoas.put(doc, pessoa);
    }

    public void remover(String documento) {
        String doc = normalizar(documento);

        if (pessoas.remove(doc) == null) {
            throw new IllegalArgumentException("Pessoa não encontrada");
        }
    }

    public Optional<Pessoa> buscarPorDocumento(String documento) {
        return Optional.ofNullable(pessoas.get(normalizar(documento)));
    }

    public List<Pessoa> buscar(Predicate<Pessoa> filtro) {
        return pessoas.values().stream()
                .filter(filtro)
                .toList();
    }

    public List<Pessoa> listar() {
        return new ArrayList<>(pessoas.values());
    }

    private String normalizar(String doc) {
        return doc.replaceAll("\\D", "");
    }

}
