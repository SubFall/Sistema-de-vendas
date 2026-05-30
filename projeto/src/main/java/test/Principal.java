package test;

import service.PessoaService;
import ui.pessoa.ConsoleMenu;

public class Principal {
    static void main(String[] args) {

        PessoaService service = new PessoaService();
        ConsoleMenu consoleMenu = new ConsoleMenu(service);
        consoleMenu.iniciar();

    }
}