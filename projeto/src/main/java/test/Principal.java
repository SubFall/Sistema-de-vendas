package test;

import service.EnderecoService;
import service.PessoaService;
import ui.pessoa.ConsoleMenu;

public class Principal {
    static void main(String[] args) {

        PessoaService service = new PessoaService();
        EnderecoService enderecoService = new EnderecoService();
        ConsoleMenu consoleMenu = new ConsoleMenu(service);

        consoleMenu.iniciar();

    }
}