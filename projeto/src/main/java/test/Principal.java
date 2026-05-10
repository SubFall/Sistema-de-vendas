package test;


import repository.PessoaRepository;
import service.PessoaService;
import ui.ConsoleMenu;

public class Principal {
    public static void main(String[] args) {
        PessoaRepository repository = new PessoaRepository();
        PessoaService service = new PessoaService(repository);

        ConsoleMenu menu = new ConsoleMenu(service);
        menu.iniciar();
    }
}