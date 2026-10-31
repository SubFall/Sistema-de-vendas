package ui.grid;

import domain.categoria.Categoria;
import util.console.ConsoleUtils;

import java.util.List;

public final class CategoriaGrid {
    private CategoriaGrid(){
    }

    public static void menu() {
        System.out.println("\n|********* Categoria *********|");
        System.out.println("|1 - Cadastrar          - [] X|");
        System.out.println("|2 - Remover                  |");
        System.out.println("|3 - Atualizar                |");
        System.out.println("|4 - Listar                   |");
        System.out.println("|0 - Voltar                   |");
        System.out.println("|*****************************|");
    }

    public static void exibirCabecalhoGridCategoria() {
        System.out.println("=======================================");
        System.out.println("                CATEGORIA              ");
        System.out.println("=======================================");
        System.out.printf(
                "%-5s | %-20s | %-10s%n",
                "ID", "DESCRICAO", "ATIVO"
        );
        System.out.println("---------------------------------------");
    }

    public static void exibirGridCategoria(List<Categoria> categorias) {
        String id;
        String descricao;
        String ativo;

        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria encontrado.");
            return;
        }

        exibirCabecalhoGridCategoria();

        for (Categoria c : categorias) {
            id = ConsoleUtils.formatarColuna(String.valueOf(c.getId()), 5);
            descricao = ConsoleUtils.formatarColuna(c.getDescricao(), 20);
            ativo = c.isAtivo() ? "SIM" : "NÃO";

            System.out.println(id + " | " + descricao + " | " + ativo);
        }
    }
}
