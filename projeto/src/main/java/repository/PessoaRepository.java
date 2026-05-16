package repository;

import conn.ConnectionFactory;
import domain.documento.CNPJ;
import domain.documento.CPF;
import domain.documento.Documento;
import domain.documento.TipoPessoa;
import domain.pessoa.Pessoa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaRepository {

    public void inserirPessoa(Pessoa pessoa) {
        String sql = "INSERT INTO pessoa (`descricao`, `documento`, `tipo`) VALUES (?, ?, ?);";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getDocumento().getValor());
            ps.setInt(3, pessoa.getDocumento().getTipo().getCodigo());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deletarPessoa(int id) {
        String sql = "DELETE FROM pessoa WHERE (id_pessoa = ?);";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int atualizarPessoa(Pessoa pessoa) {
        String sql = "UPDATE pessoa SET descricao = ?, documento = ?, tipo = ? WHERE id_pessoa = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getDocumento().getValor());
            ps.setInt(3, pessoa.getDocumento().getTipo().getCodigo());
            ps.setInt(4, pessoa.getId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Pessoa buscarPorDocumento(String documentoBusca) {
        String sql = "SELECT id_pessoa, descricao, documento, tipo FROM pessoa WHERE documento = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {

            ps.setString(1, documentoBusca);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPessoa(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Pessoa> buscarPorNome(String nome) {
        String sql = "SELECT id_pessoa, descricao, documento, tipo FROM pessoa WHERE descricao LIKE ?;";
        List<Pessoa> pessoaList = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + nome + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pessoaList.add(mapearPessoa(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pessoaList;
    }

    public List<Pessoa> buscarTodos() {
        String sql = "SELECT id_pessoa, descricao, documento, tipo FROM pessoa;";
        List<Pessoa> pessoasList = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();) {

            while (rs.next()) {
                pessoasList.add(mapearPessoa(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pessoasList;
    }

    private Pessoa mapearPessoa(ResultSet rs) throws SQLException {
        int tipo = rs.getInt("tipo");

        Documento documento;

        if (tipo == TipoPessoa.FISICA.getCodigo()) {
            documento = new CPF(rs.getString("documento"));
        } else {
            documento = new CNPJ(rs.getString("documento"));
        }

        return Pessoa.builder()
                .id(rs.getInt("id_pessoa"))
                .nome(rs.getString("descricao"))
                .documento(documento)
                .build();
    }

    public boolean existeDocumento(String documento) {
        String sql = "SELECT 1 FROM pessoa WHERE documento = ? LIMIT 1;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, documento);

            try (ResultSet rs = ps.executeQuery();) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existeDocumentoPorOutroId(String documento, int id) {
        String sql = "SELECT 1 FROM pessoa WHERE documento = ? AND id_pessoa <> ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, documento);
            ps.setInt(2, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}