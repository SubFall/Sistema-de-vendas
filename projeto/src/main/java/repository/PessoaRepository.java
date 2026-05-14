package repository;

import conn.ConnectionFactory;
import domain.documento.CNPJ;
import domain.documento.CPF;
import domain.documento.Documento;
import domain.documento.TipoPessoa;
import domain.pessoa.Pessoa;

import javax.print.Doc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PessoaRepository {

    private Map<String, Pessoa> pessoas = new HashMap<>();
    private List<Pessoa> pessoasList = new ArrayList<>();

    public void inserirPessoa(Pessoa pessoa) {
        String sql = "INSERT INTO pessoa (`descricao`, `documento`, `tipo`) VALUES (?, ?, ?);";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getDocumento().getValor());
            ps.setInt(3, pessoa.getDocumento().getTipo().getCodigo());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existeDocumento(String documento) {
        String sql = "SELECT 1 FROM pessoa WHERE documento = ? LIMIT 1;";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, documento);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Pessoa buscarPorDocumento(String documentoBusca) {
        String sql = "SELECT descricao, documento, tipo FROM pessoa WHERE documento = ?;";

        try(Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, documentoBusca);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int tipo = rs.getInt("tipo");

                Documento documento;

                if (tipo == TipoPessoa.FISICA.getCodigo()) {
                    documento = new CPF(rs.getString("documento"));
                }else {
                    documento = new CNPJ(rs.getString("documento"));
                }
                return Pessoa.builder()
                        .nome(rs.getString("descricao"))
                        .documento(documento)
                        .build();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


//    public void salvar(Pessoa pessoa) {
//        String doc = normalizar(pessoa.getDocumento());
//
//        if (pessoas.containsKey(doc)) {
//            throw new IllegalArgumentException("Documento já cadastrado!");
//        }
//
//        pessoas.put(doc, pessoa);
//    }
//
//    public void remover(String documento) {
//        String doc = normalizar(documento);
//
//        if (pessoas.remove(doc) == null) {
//            throw new IllegalArgumentException("Pessoa não encontrada");
//        }
//    }
//
//    public Optional<Pessoa> buscarPorDocumento(String documento) {
//        return Optional.ofNullable(pessoas.get(normalizar(documento)));
//    }
//
//    public List<Pessoa> buscar(Predicate<Pessoa> filtro) {
//        return pessoas.values().stream()
//                .filter(filtro)
//                .toList();
//    }
//
//    public List<Pessoa> listar() {
//        return new ArrayList<>(pessoas.values());
//    }
//
//    private String normalizar(String doc) {
//        return doc.replaceAll("\\D", "");
//    }

//    public static void test() {
//        String sql = "SELECT id_pessoa, descricao, documento, tipo FROM eclipse_net.pessoa;";
//
//        try(Connection conn =  ConnectionFactory.getConnection();
//            PreparedStatement ps = conn.prepareStatement(sql)) {
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                new Pessoa(rs.getInt("id_pessoa"), )
//            }
//            System.out.println("Banco funfou");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
