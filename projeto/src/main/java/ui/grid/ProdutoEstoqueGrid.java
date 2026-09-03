package ui.grid;

import domain.ajusteestoque.AjusteEstoque;
import domain.ajusteestoque.AjusteEstoqueItens;
import dto.ProdutoEstoqueDTO;
import util.ConsoleUtils;

import java.util.List;

public final class ProdutoEstoqueGrid {
    private ProdutoEstoqueGrid() {

    }

    public static void menu() {
        System.out.println("\n|****** Ajuste  Estoque ******|");
        System.out.println("|1 - Novo               - [] X|");
        System.out.println("|2 - Remover                  |");
        System.out.println("|3 - Atualizar                |");
        System.out.println("|4 - Criar Movimento          |");
        System.out.println("|5 - Listar                   |");
        System.out.println("|0 - Voltar                   |");
        System.out.println("|*****************************|");
    }

    public static void exibiCabecalhoGirdProdutoEstoque() {
        System.out.println("=======================================");
        System.out.println("                PRODUTOS               ");
        System.out.println("=======================================");
        System.out.printf(
                "%-3s | %-20s | %-5s %n",
                "ID", "PRODUTO", "QUANTIDADE"
        );
        System.out.println("---------------------------------------");
    }


    public static void exibirtGirdProdutoEstoque(List<ProdutoEstoqueDTO> produtoEstoqueDTOS) {
        String id;
        String descricao;
        String quantidade;

        if (produtoEstoqueDTOS.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }

        exibiCabecalhoGirdProdutoEstoque();

        for (ProdutoEstoqueDTO produtoEstoqueDTO : produtoEstoqueDTOS) {
            id = ConsoleUtils.formatarColuna(String.valueOf(produtoEstoqueDTO.getIdProduto()), 3);
            descricao = ConsoleUtils.formatarColuna(String.valueOf(produtoEstoqueDTO.getDescricao()), 20);
            quantidade = ConsoleUtils.formatarColuna(String.valueOf(produtoEstoqueDTO.getQuantidade()), 10);

            System.out.printf("%s | %s | %s  %n", id, descricao, quantidade);
        }
        System.out.println("---------------------------------------");

    }

    public static void exibirCabecalhoGridItens() {
        System.out.println("====================================================");
        System.out.println("                  AJUSTE DE ESTOQUE                 ");
        System.out.println("====================================================");
        System.out.printf(
                "%-3s | %-15s | %-5s | %-8s | %-11s%n",
                "ID", "PRODUTO", "SALDO", "CONTAGEM", "DIFERENCA"
        );
    }

    public static void exibirGridItens(List<AjusteEstoqueItens> itens) {
        String id;
        String produto;
        String saldoAtual;
        String contagem;
        String diferenca;


        if (itens.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }

        exibirCabecalhoGridItens();

        for (AjusteEstoqueItens i : itens) {
            id = ConsoleUtils.formatarColuna(String.valueOf(i.getProduto().getId()), 3);
            produto = ConsoleUtils.formatarColuna(String.valueOf(i.getProduto().getDescricao()), 15);
            saldoAtual = ConsoleUtils.formatarColuna(i.getEstoque().getQuantidade().toString(), 5);
            contagem = ConsoleUtils.formatarColuna(i.getContagem().toString(), 8);
            diferenca = ConsoleUtils.formatarColuna(i.getDiferenca().toString(), 11);


            System.out.printf("%s | %s | %s | %s | %s %n", id, produto, saldoAtual, contagem, diferenca);

        }
        System.out.println("----------------------------------------------------");
    }

    public static void exibirGridAjusteEstoqueCabecalho() {
        System.out.println("===============================================");
        System.out.println("                AJUSTE DE ESTOQUE              ");
        System.out.println("===============================================");
        System.out.printf(
                "%-3s | %-15s | %-10s | %-10s%n",
                "ID", "TÍTULO", "DATA", "STATUS"
        );
    }

    public static void exibirGridAjusteEstoque(List<AjusteEstoque> ajusteEstoques) {
        String id;
        String titulo;
        String data;
        String status;

        if (ajusteEstoques.isEmpty()) {
            System.out.println("Nenhum Ajuste de Estoque encontrado.");
            return;
        }

        exibirGridAjusteEstoqueCabecalho();

        for (AjusteEstoque ajuste : ajusteEstoques) {
            id = ConsoleUtils.formatarColuna(String.valueOf(ajuste.getId()), 3);
            titulo = ConsoleUtils.formatarColuna(String.valueOf(ajuste.getTitulo()), 15);
            data = ConsoleUtils.formatarColuna(String.valueOf(ajuste.getDateHora()), 10);
            status = ConsoleUtils.formatarColuna(String.valueOf(ajuste.getStatus()), 10);

            System.out.printf("%s | %s | %s | %s %n", id, titulo, data, status);
        }
        System.out.println("-----------------------------------------------");

    }
}
