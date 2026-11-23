package util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class Log {
    private static final Path PASTA_RAIZ = Path.of("Sistema_Vendas", "logError");
    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");

    private Log() {
    }

    public static void registrarErro(String nomeClasse, String nomeMetodo, Throwable erro) {
        var classeSeguro = nomeClasse.replaceAll("[^a-zA-Z0-9_-]", "_");
        var metodoSeguro = nomeMetodo.replaceAll("[^a-zA-Z0-9_-]", "_");
        var pastaMetodo = PASTA_RAIZ.resolve(classeSeguro, metodoSeguro);

        try {
            Files.createDirectories(pastaMetodo);

            var nomeArquivo = "erro-" + LocalDateTime.now().format(FORMATO_DATA)
                    + "-" + UUID.randomUUID() + ".txt";
            var arquivo = pastaMetodo.resolve(nomeArquivo);

            var stackTrace = new StringWriter();
            erro.printStackTrace(new PrintWriter(stackTrace));

            var conteudo = "Data: " + LocalDateTime.now() + System.lineSeparator()
                    + "Método: " + nomeMetodo + System.lineSeparator()
                    + "Erro: " + erro.getMessage() + System.lineSeparator()
                    + System.lineSeparator()
                    + stackTrace;

            Files.writeString(arquivo, conteudo);
        } catch (Exception ex) {
            erro.addSuppressed(ex);
            System.err.println("Não foi possível gravar o log em " + pastaMetodo);
            ex.printStackTrace(System.err);
        }
    }
}
