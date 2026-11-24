package ui.categoria;

import domain.categoria.Categoria;
import domain.exception.categoria.*;
import domain.exception.produto.FalhaAoBuscarCategoriaException;
import lombok.RequiredArgsConstructor;
import service.CategoriaService;
import ui.grid.CategoriaGrid;
import util.console.ConsoleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class CategoriaMenu {
    private final Scanner scanner;
    private final CategoriaService categoriaService;

    public void iniciar() {
        int opcao;

        do {
            CategoriaGrid.menu();
            System.out.print("Digite o numero da opcao: ");
            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");

            switch (opcao) {
                case 1 -> cadastrarCategoria();
                case 2 -> removerCategoria();
                case 3 -> atualizarCategoria();
                case 4 -> {
                    int op;
                    do {
                        CategoriaGrid.listar();
                        System.out.print("Digite o numero da opcao: ");
                        op = ConsoleUtils.lerInteiro(scanner, "Digite o numero da opcao:");
                    } while (op != 1 && op != 2 && op != 3);

                    listar(op);
                }
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
            }
        } while (opcao != 0);
    }

    private void cadastrarCategoria() {
        try {
            System.out.print("Descricao: ");
            var categoria = Categoria.builder().descricao(scanner.nextLine()).build();

            var isCategoriaCadastrada = categoriaService.inserirCategoria(categoria);

            if (isCategoriaCadastrada != null && Categoria.ID_SEM_CATEGORIA.equals(isCategoriaCadastrada.getId())) {
                System.out.println("Categoria padrao utilizada: " + isCategoriaCadastrada.getDescricao());
            } else if (isCategoriaCadastrada != null) {
                System.out.println("Categoria " + isCategoriaCadastrada.getDescricao() + " cadastrada com sucesso!");
            } else {
                System.out.println("Erro ao inserir categoria: " + categoria.getDescricao());
            }
        } catch (FalhaAoInserirCategoriaException | FalhaCategoriaNaoExisteException | FalhaCategoriaJaExisteException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removerCategoria() {
        try {
            listar(1);

            System.out.print("Digite o ID da categoria para remover:");
            var categoria = categoriaService.buscarPorId(ConsoleUtils.lerLong(scanner, "ID"));

            if (!ConsoleUtils.confirmar(scanner, "Tem certeza que deseja Excluir " + categoria.getDescricao() + " ?")) {
                return;
            }
            var i = categoriaService.deletarInativarCategoria(categoria);

            if (i == 0) {
                System.out.println("Categoria inativada.");
            } else if (i == 1) {
                System.out.println("Categoria deletada com sucesso!");
            } else {
                System.out.println("Erro ao inativar/deletar");
            }
        } catch (FalhaAoDeletarOuInativarCategoriaException | FalhaCategoriaPadraoNaoPodeAlterarException e) {
            System.out.println(e.getMessage());
        }
    }

    private void atualizarCategoria() {
        try {
            listar(1);
            System.out.println("Selecione um ID para atualizar");

            var categoria = categoriaService.buscarPorId(ConsoleUtils.lerLong(scanner, "ID Categoria"));

            System.out.println("Caso queira deixar o valor antigo, aperta apenas ENTER");

            atualizarDescricao(categoria);
            atualizarStatus(categoria);

            var isCategoriaAtualizado = categoriaService.atualizarCategoria(categoria);

            if (isCategoriaAtualizado) {
                System.out.println("Categoria " + categoria.getDescricao() + " atualizado com sucesso!");
            } else {
                System.out.println("Ops! Erro ao Atualizar Categoria " + categoria.getDescricao());
            }
        } catch (FalhaAoAtualizarCategoriaException | FalhaCategoriaJaExisteException | FalhaCategoriaPadraoNaoPodeAlterarException e) {
            System.out.println(e.getMessage());
        }
    }

    private void atualizarDescricao(Categoria categoria) {
        System.out.println("Descricao Atual: " + categoria.getDescricao());
        System.out.println("Nova Descricao:");

        var descricao = scanner.nextLine();
        categoria.setDescricao(descricao.isBlank() ? categoria.getDescricao() : descricao);
    }

    private void atualizarStatus(Categoria categoria) {
        int op;
        do {
            System.out.println("Ativo: " + (categoria.isAtivo() ? "SIM" : "NÃO"));
            System.out.println("Deseja mudar ?:");
            System.out.println("1 - Ativo");
            System.out.println("2 - Inativo");

            op = ConsoleUtils.lerInteiro(scanner, "Opção");
        } while (op != 1 && op != 2);

        categoria.setAtivo(op == 1);
    }

    public void listar(int idLista) {
        List<Categoria> categorias = new ArrayList<>();
        try {
            switch (idLista) {
                case 1 -> categorias = categoriaService.buscarTodos();
                case 2 -> {
                    System.out.print("Digite a descrição: ");
                    categorias = categoriaService.buscarPorDescricao(scanner.nextLine());
                }
                case 3 -> {
                    System.out.print("Digite o ID: ");

                    categorias.add(categoriaService.buscarPorId(ConsoleUtils.lerLong(scanner, "ID")));
                }
                default -> System.out.println("opção inválida!");
            }
        } catch (FalhaAoBuscarCategoriaException | FalhaCategoriaNaoExisteException e) {
            System.out.println(e.getMessage());
        }
        CategoriaGrid.exibirGridCategoria(categorias);
    }
}
