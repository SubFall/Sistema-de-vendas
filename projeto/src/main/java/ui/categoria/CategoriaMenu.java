package ui.categoria;

import domain.categoria.Categoria;
import service.CategoriaService;
import util.ConsoleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CategoriaMenu {
    private Scanner scanner = new Scanner(System.in);
    private CategoriaService categoriaService;

    public CategoriaMenu(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public void iniciar() {
        int opcao;

        do {
            System.out.println("\n|********** Categoria **********|");
            System.out.println("|1 - Cadastrar            - [] X|");
            System.out.println("|2 - Remover                    |");
            System.out.println("|3 - Atualizar                  |");
            System.out.println("|4 - Listar                     |");
            System.out.println("|0 - Sair                       |");
            System.out.println("|*******************************|");

            System.out.print("Opção: ");
            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");

            switch (opcao) {
                case 1:
                    cadastrar();
                    break;
                case 2:
                    remover();
                    break;
                case 3:
                    atualizar();
                    break;
                case 4:
                    int op;
                    do {
                        System.out.println("1 - Listar todas categoriras");
                        System.out.println("2 - Listar categora por descricao");
                        System.out.println("3 - Listar categora por id");

                        op = ConsoleUtils.lerInteiro(scanner, "Opção");

                    } while (op != 1 && op != 2 && op != 3);

                    listar(op);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }

        } while (opcao != 0);
    }

    private void cadastrar() {

        try {
            System.out.print("Descricao: ");
            Categoria categoria = Categoria.builder()
                    .descricao(scanner.nextLine())
                    .build();

            categoriaService.inserirCategoria(categoria);
            System.out.println("Categoria " + categoria.getDescricao() + " cadastrada com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void remover() {
        listar(1);
        try {
            System.out.print("Digite o ID da categoria para remover:");
            Categoria categoria = categoriaService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID"));

            if (!ConsoleUtils.confirmar(scanner, "Tem certeza que deseja Excluir " + categoria.getDescricao() + " ?")) {
                return;
            }
            int i = categoriaService.deletarInativarCategoria(categoria.getId());

            if (i == 0) {
                System.out.println("Categoria inativada.");
            } else {
                System.out.println("Categoria deletada com sucesso!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void atualizar() {
        listar(1);
        System.out.println("Selecione um ID para atualizar");

        try {
            Categoria categoria = categoriaService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID Categoria"));


            System.out.println("Caso queira deixar o valor antigo, aperta apenas ENTER");
            System.out.println("Descricao Atual: " + categoria.getDescricao());
            System.out.println("Nova Descricao:");

            String descricao = scanner.nextLine();
            categoria.setDescricao(descricao.isBlank() ? categoria.getDescricao() : descricao);

            int op;
            do {
                System.out.println("Ativo: " + (categoria.isAtivo() ? "SIM" : "NÃO"));
                System.out.println("Deseja mudar ?:");
                System.out.println("1 - Ativo");
                System.out.println("2 - Inativo");

                op = ConsoleUtils.lerInteiro(scanner, "Opção");
            } while (op != 1 && op != 2);

            categoria.setAtivo(op == 1);

            categoriaService.atualizarCategoria(categoria);

            System.out.println("Categoria " + categoria.getDescricao() + " atualizado com sucesso!");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void listar(int idLista) {
        List<Categoria> categorias = new ArrayList<>();
        switch (idLista) {
            case 1:
                categorias = categoriaService.buscarTodos();
                break;
            case 2:
                System.out.print("Digite a descrição: ");
                categorias = categoriaService.buscarPorDescricao(scanner.nextLine());
                break;
            case 3:
                System.out.print("Digite o ID: ");

                try {
                    categorias.add(categoriaService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID")));
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }

                break;
            default:
                System.out.println("opção inválida!");
        }

        exibirGrid(categorias);
    }

    private void exibirCabecalhoGrid() {
        System.out.printf(
                "%-5s | %-20s | %-10s%n",
                "ID", "DESCRICAO", "ATIVO"
        );
    }

    private void exibirGrid(List<Categoria> categorias) {
        String id;
        String descricao;
        String ativo;

        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria encontrado.");
            return;
        }

        exibirCabecalhoGrid();

        for (Categoria c : categorias) {
            id = ConsoleUtils.formatarColuna(String.valueOf(c.getId()), 5);
            descricao = ConsoleUtils.formatarColuna(c.getDescricao(), 20);
            ativo = c.isAtivo() ? "SIM" : "NÃO";

            System.out.println(id + " | " + descricao + " | " + ativo);
        }
    }
}
