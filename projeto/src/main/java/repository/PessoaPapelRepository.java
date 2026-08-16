package repository;

import conn.ConnectionFactory;
import conn.ConnectionProvider;
import domain.pessoa.PessoaPapel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaPapelRepository {
    private final ConnectionProvider connectionProvider = new ConnectionFactory();

    public void inserirPessoaPapel(Connection conn, int idPessoa, int idPapel) {
        String sql = "INSERT INTO pessoa_papel (id_pessoa, id_papel) VALUES (?, ?);";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPessoa);
            ps.setInt(2, idPapel);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletarPessoaPapel(Connection conn, int idPessoa) {
        String sql = "DELETE FROM pessoa_papel WHERE id_pessoa = ?;";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPessoa);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PessoaPapel> buscarPorIdPessoa(int idPessoa) {
        String sql = """
                SELECT p.id_papel
                FROM pessoa_papel pp
                JOIN papel p ON p.id_papel = pp.id_papel
                WHERE pp.id_pessoa = ?;
                """;

        List<PessoaPapel> pessoaPapels = new ArrayList<>();

        try (Connection conn = connectionProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPessoa);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pessoaPapels.add(PessoaPapel.fromCodigo(rs.getInt("id_papel")));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pessoaPapels;
    }
}
