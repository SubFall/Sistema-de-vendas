package repository;

import conn.ConnectionFactory;
import domain.Pessoa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public static void test() {
        String sql = "SELECT * FROM eclipse_net.pessoa;";

        try(Connection conn =  ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
            System.out.println("Banco funfou");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
