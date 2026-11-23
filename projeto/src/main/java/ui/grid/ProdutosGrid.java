package ui.grid;

import domain.produto.Produto;
import util.console.ConsoleUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;

public final class ProdutosGrid {
    public static void menu() {
        System.out.println("\n+-----------------------------------+");
        System.out.println("|              PRODUTOS             |");
        System.out.println("+-----------------------------------+");
        System.out.println("| 1 - Cadastrar                     |");
        System.out.println("| 2 - Inativar                      |");
        System.out.println("| 3 - Ativar                        |");
        System.out.println("| 4 - Atualizar                     |");
        System.out.println("| 5 - Detalhes                      |");
        System.out.println("| 6 - Listar                        |");
        System.out.println("|                                   |");
        System.out.println("| 0 - Voltar                        |");
        System.out.println("+-----------------------------------+");
    }

    public static void exibirCabecalhoGridProdutos() {
        System.out.println("\n+----------------------------------------------------------------+");
        System.out.println("|                            PRODUTOS                            |");
        System.out.println("+----------------------------------------------------------------+");
        System.out.printf(
                "%-5s | %-20s | %-10s | %-10s | %-10s%n",
                "| ID", "DESCRICAO", "PRECO VENDA", "PRECO CUSTO", "ATIVO |"
        );
        System.out.println("+----------------------------------------------------------------+");
    }

    public static void exibirGridProdutos(List<Produto> produtos) {
        String id;
        String descricao;
        String precoVenda;
        String precoCusto;
        String ativo;

        if (produtos.isEmpty()) {
            System.out.println("\nNenhum produto encontrado.");
            return;
        }

        exibirCabecalhoGridProdutos();

        for (Produto p : produtos) {
            id = ConsoleUtils.formatarColuna(String.valueOf(p.getId()), 3);
            descricao = ConsoleUtils.formatarColuna(p.getDescricao(), 20);
            precoVenda = ConsoleUtils.formatarColuna(ConsoleUtils.formatarMoeda(p.getPrecoVenda()), 11);
            precoCusto = ConsoleUtils.formatarColuna(ConsoleUtils.formatarMoeda(p.getPrecoCusto()), 11);
            ativo = p.isAtivo() ? "SIM" : "NAO";

            System.out.printf("| %s | %s | %s | %s | %s   |%n", id, descricao, precoVenda, precoCusto, ativo);
            System.out.println("+----------------------------------------------------------------+");

        }
    }

    public static void exibirDetalhesProduto(Produto produto) {
        String id;
        String descricao;
        String precoVenda;
        String precoCusto;
        String ativo;
        String createdAt;
        String modifiedOn;
        String categoria;

        id = ConsoleUtils.formatarColuna(String.valueOf(produto.getId()), 3);
        descricao = ConsoleUtils.formatarColuna(produto.getDescricao(), 41);
        precoCusto = ConsoleUtils.formatarColuna(ConsoleUtils.formatarMoeda(produto.getPrecoCusto()), 11);
        precoVenda = ConsoleUtils.formatarColuna(ConsoleUtils.formatarMoeda(produto.getPrecoVenda()), 23);
        createdAt = ConsoleUtils.formatarColuna(produto.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), 49);

        var dataModifiedOn = produto.getModifiedOn();
        if (dataModifiedOn != null) {
            modifiedOn = ConsoleUtils.formatarColuna(dataModifiedOn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), 47);
        } else {
            modifiedOn = null;
        }
        ativo = ConsoleUtils.formatarColuna(produto.isAtivo() ? "SIM" : "NAO", 55);
        categoria = ConsoleUtils.formatarColuna(produto.getCategoria().getDescricao(), 51);

        System.out.println("+----------------------------------------------------------------+");
        System.out.printf("| ID: %s   Descricao: %s |\n", id, descricao);
        System.out.println("|                                                                |");
        System.out.printf("| Preco Custo: %s  Preco Venda: %s |\n", precoCusto, precoVenda);
        System.out.printf("| Categoria: %s |\n", categoria);
        System.out.println("|                                                                |");
        System.out.println("|                                                                |");

        if (modifiedOn != null) {
            System.out.printf("| Criado Em: %s   |\n", createdAt);
            System.out.printf("| Modificado Em: %s |\n", modifiedOn);
        } else {
            System.out.printf("| Criado Em: %s   |\n", createdAt);

        }
        System.out.printf("| Ativo: %s |\n", ativo);
        System.out.println("+----------------------------------------------------------------+");
    }
}
