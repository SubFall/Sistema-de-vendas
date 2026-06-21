package repository;

import conn.ConnectionFactory;
import domain.documento.CNPJ;
import domain.documento.CPF;
import domain.documento.Documento;
import domain.documento.TipoPessoa;
import domain.pessoa.Pessoa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaRepository {

    public int inserirPessoa(Connection conn, Pessoa pessoa) {
        String sql = "INSERT INTO pessoa (`descricao`, `documento`, `tipo`, `ativo`) VALUES (?, ?, ?, ?);";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getDocumento().getValor());
            ps.setInt(3, pessoa.getDocumento().getTipo().getCodigo());
            ps.setBoolean(4, pessoa.isAtivo());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new IllegalArgumentException("Erro ao inserir");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //TODO Voltar  nesse método quando movimento estiver pronto
    public int deletarPessoa(Connection conn, int id) {
        String sql = "DELETE FROM pessoa WHERE (id_pessoa = ?);";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean inativarPessoa(boolean ativo, int idPessoa) {
        String sql = "UPDATE pessoa SET ativo = ? WHERE id_pessoa = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, ativo);
            ps.setInt(2, idPessoa);

            return ps.executeUpdate() > 0;
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

    public int atualizarPessoa(Connection conn, Pessoa pessoa) {
        String sql = "UPDATE pessoa SET descricao = ?, documento = ?, tipo = ?, ativo = ? WHERE id_pessoa = ?;";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getDocumento().getValor());
            ps.setInt(3, pessoa.getDocumento().getTipo().getCodigo());
            ps.setBoolean(4, pessoa.isAtivo());
            ps.setInt(5, pessoa.getId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Pessoa buscarPorDocumento(String documentoBusca) {
        String sql = "SELECT id_pessoa, descricao, documento, tipo, ativo FROM pessoa WHERE documento = ?;";

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

    public Pessoa buscarPorId(int id) {
        String sql = "SELECT id_pessoa, descricao, documento, tipo, ativo FROM pessoa WHERE id_pessoa = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);) {

            ps.setInt(1, id);

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
        String sql = "SELECT id_pessoa, descricao, documento, tipo, ativo FROM pessoa WHERE descricao LIKE ?;";
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
        String sql = "SELECT id_pessoa, descricao, documento, tipo, ativo FROM pessoa;";
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

    public List<Pessoa> buscarTodosAtivo() {
        String sql = "SELECT id_pessoa, descricao, documento, tipo, ativo FROM pessoa WHERE ativo = 1;";
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
                .ativo(rs.getBoolean("ativo"))
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