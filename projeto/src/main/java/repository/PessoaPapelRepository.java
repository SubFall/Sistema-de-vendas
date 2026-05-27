package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PessoaPapelRepository {
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
}
