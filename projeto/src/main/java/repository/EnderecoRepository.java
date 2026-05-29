package repository;

import conn.ConnectionFactory;
import domain.endereco.Endereco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoRepository {
    public void inserirEndereco(Connection conn, int idPessoa, Endereco endereco) {
        String sql = "INSERT INTO endereco (logradouro, cidade, uf, bairro, numero, cep, id_pessoa) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, endereco.getLogradouro());
            ps.setString(2, endereco.getCidade());
            ps.setString(3, endereco.getUf());
            ps.setString(4, endereco.getBairro());
            ps.setString(5, endereco.getNumero());
            ps.setString(6, endereco.getCep());
            ps.setInt(7, idPessoa);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int atualizarEndereco(Endereco endereco) {
        String sql = "UPDATE endereco SET logradouro = ?, cidade = ?, uf = ?, bairro = ?, numero = ?, cep = ? WHERE id_endereco = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, endereco.getLogradouro());
            ps.setString(2, endereco.getCidade());
            ps.setString(3, endereco.getUf());
            ps.setString(4, endereco.getBairro());
            ps.setString(5, endereco.getNumero());
            ps.setString(6, endereco.getCep());
            ps.setInt(7, endereco.getId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int atualizarEndereco(Connection conn, Endereco endereco) {
        String sql = "UPDATE endereco SET logradouro = ?, cidade = ?, uf = ?, bairro = ?, numero = ?, cep = ? WHERE id_endereco = ?;";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, endereco.getLogradouro());
            ps.setString(2, endereco.getCidade());
            ps.setString(3, endereco.getUf());
            ps.setString(4, endereco.getBairro());
            ps.setString(5, endereco.getNumero());
            ps.setString(6, endereco.getCep());
            ps.setInt(7, endereco.getId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deletarEndereco(int id) {
        String sql = "DELETE FROM endereco WHERE id_endereco = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deletarEndereco(Connection conn, int id) {
        String sql = "DELETE FROM endereco WHERE id_pessoa = ?;";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Endereco> buscarTodos() {
        String sql = "SELECT id_endereco, logradouro, cidade, uf, bairro, numero, cep FROM endereco;";
        List<Endereco> enderecoList = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enderecoList.add(mapearEndereco(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return enderecoList;
    }

    public Endereco buscarPorId(int id) {
        String sql = "SELECT id_endereco, logradouro, cidade, uf, bairro, numero, cep FROM endereco WHERE id_endereco = ?;";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearEndereco(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Endereco> buscarPorCidade(String cidade) {
        String sql = "SELECT id_endereco, logradouro, cidade, uf, bairro, numero, cep FROM endereco WHERE cidade LIKE ?;";
        List<Endereco> enderecoList = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + cidade + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enderecoList.add(mapearEndereco(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return enderecoList;
    }

    public List<Endereco> buscarPorCep(String cep) {
        String sql = "SELECT id_endereco, logradouro, cidade, uf, bairro, numero, cep FROM endereco WHERE cep = ?;";
        List<Endereco> enderecoList = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cep);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enderecoList.add(mapearEndereco(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return enderecoList;
    }

    private Endereco mapearEndereco(ResultSet rs) throws SQLException {
        return Endereco.builder()
                .id(rs.getInt("id_endereco"))
                .logradouro(rs.getString("logradouro"))
                .cidade(rs.getString("cidade"))
                .uf(rs.getString("uf"))
                .bairro(rs.getString("bairro"))
                .numero(rs.getString("numero"))
                .cep(rs.getString("cep"))
                .build();
    }

    public boolean existePessoaEndereco(Connection conn, int idPessoa) {
        String sql = "SELECT 1 FROM endereco WHERE id_pessoa = ?;";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPessoa);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
