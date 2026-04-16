package dev.thalysom.pessoas.repository;

import dev.thalysom.pessoas.domain.Endereco;
import dev.thalysom.pessoas.domain.Pessoa;

import java.util.*;
import java.util.function.Predicate;

public class PessoaRepository {
//    private List<Pessoa> pessoas = new ArrayList<>();
    private Map<String, Pessoa> pessoas = new HashMap<>();

//    public void salvar(Pessoa pessoa) {
//        if (documentoExiste(pessoa.getDocumento())) {
//            throw new IllegalArgumentException("Documento já cadastrado!");
//        }
//        pessoas.add(pessoa);
//    }

    public void salvar(Pessoa pessoa) {
        String doc = normalizar(pessoa.getDocumento());

        if (pessoas.containsKey(doc)) {
            throw new IllegalArgumentException("Documento já cadastrado!");
        }

        pessoas.put(doc, pessoa);
    }

    public void remover(Pessoa pessoa) {
        String doc = normalizar(pessoa.getDocumento());

        if (pessoas.remove(doc) == null) {
            throw new IllegalArgumentException("Pessoa não encontrada");
        }
    }

//    public void remover(String documento) {
//        documento = documento.replaceAll("\\D", "");
//
//        String finalDocumento = documento;
//        boolean removido = pessoas.removeIf(p -> p.getDocumento().equals(finalDocumento));
//
//        if (removido) {
//            System.out.println("Pessoa removida com sucesso!");
//        } else {
//            System.out.println("Pessoa não encontrada.");
//        }
//    }

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

//    public Boolean documentoExiste(String documento) {
//        documento = documento.replaceAll("\\D", "");
//
//        for (Pessoa pessoa : pessoas) {
//            if (pessoa.getDocumento().equals(documento)) {
//                return true;
//            }
//        }
//        return false;
//    }

//    public void listar() {
//        if (pessoas.isEmpty()) {
//            System.out.println("Nenhuma pessoa cadastrada.");
//            return;
//        }
//
//        for (Pessoa pessoa : pessoas) {
//            pessoa.mostrarDados();
//        }
//
//    }

//    public List<Pessoa> buscar(Predicate<Pessoa> filtro ) {
//        List<Pessoa> resultado = new ArrayList<>();
//
//        for (Pessoa p : pessoas) {
//            if (filtro.test(p)) {
//                resultado.add(p);
//            }
//        }
//        return resultado;
//    }

//    public void adicionarEndereco(Pessoa pessoa, Endereco endereco) {
//        pessoa.setEnderecos(endereco);
//    }

    private String normalizar(String doc) {
        return doc.replaceAll("\\D", "");
    }
}
