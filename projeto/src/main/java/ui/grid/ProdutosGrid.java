package ui.grid;

import domain.produto.Produto;
import util.console.ConsoleUtils;

import java.util.List;

public final class ProdutosGrid {
    public static void menu() {
        System.out.println("\n|********** Produto **********|");
        System.out.println("|1 - Cadastrar          - [] X|");
        System.out.println("|2 - Inativar                 |");
        System.out.println("|3 - Atualizar                |");
        System.out.println("|4 - Detalhes                 |");
        System.out.println("|5 - Listar                   |");
        System.out.println("|0 - Voltar                   |");
        System.out.println("|*****************************|");
    }

    public static void exibirCabecalhoGridProdutos() {
        System.out.println("=======================================");
        System.out.println("                PRODUTOS               ");
        System.out.println("=======================================");
        System.out.printf(
                "%-5s | %-20s | %-10s | %-10s | %-10s%n",
                "ID", "DESCRICAO", "PRECO VENDA", "PRECO CUSTO", "ATIVO"
        );
        System.out.println("---------------------------------------");
    }

    public static void exibirGridProdutos(List<Produto> produtos) {
        String id;
        String descricao;
        String precoVenda;
        String precoCusto;
        String ativo;

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }

        exibirCabecalhoGridProdutos();

        for (Produto p : produtos) {
            id = ConsoleUtils.formatarColuna(String.valueOf(p.getId()), 5);
            descricao = ConsoleUtils.formatarColuna(p.getDescricao(), 20);
            precoVenda = ConsoleUtils.formatarColuna(ConsoleUtils.formatarMoeda(p.getPrecoVenda()), 11);
            precoCusto = ConsoleUtils.formatarColuna(ConsoleUtils.formatarMoeda(p.getPrecoCusto()), 11);
            ativo = p.isAtivo() ? "SIM" : "NÃO";

            System.out.printf("%s | %s | %s | %s | %s %n", id, descricao, precoVenda, precoCusto, ativo);
            System.out.println("---------------------------------------");

        }
    }
}
