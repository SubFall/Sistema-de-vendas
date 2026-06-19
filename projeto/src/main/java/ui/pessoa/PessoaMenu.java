package ui.pessoa;

import domain.documento.CNPJ;
import domain.documento.CPF;
import domain.endereco.Endereco;
import domain.pessoa.Pessoa;
import domain.pessoa.PessoaPapel;
import service.PessoaService;
import util.ConsoleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PessoaMenu {
    private Scanner scanner = new Scanner(System.in);
    private PessoaService pessoaService;

    public PessoaMenu(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    public void iniciar() {
        int opcao;

        do {
            System.out.println("\n|********** Pessoas **********|");
            System.out.println("|1 - Cadastrar          - [] X|");
            System.out.println("|2 - Remover                  |");
            System.out.println("|3 - Atualizar                |");
            System.out.println("|4 - Detalhes                 |");
            System.out.println("|5 - Listar                   |");
            System.out.println("|0 - Sair                     |");
            System.out.println("|*****************************|");

            System.out.print("Opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    do {
                        System.out.println("1 - Cadastrar Pessoa Física");
                        System.out.println("2 - Cadastrar Pessoa Jurídica");

                        System.out.print("Opção: ");
                        opcao = scanner.nextInt();
                        scanner.nextLine();
                    } while (opcao != 1 && opcao != 2);

                    if (opcao == 1) {
                        cadastrarPessoaFisica();
                    } else {
                        cadastrarPessoaJuridica();
                        System.out.println();
                    }
                    break;
                case 2:
                    removerPessoa();
                    break;
                case 3:
                    atualizarPessoa();
                    break;
                case 4:
                    System.out.println("Digite o ID da pessoa: ");
                    try {
                        detalhesPessoa(ConsoleUtils.lerInteiro(scanner, "ID"));
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    listarPessoa();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }

        } while (opcao != 0);
    }

    private void cadastrarPessoaFisica() {
        String nome;
        String documento;
        String opcao;
        List<PessoaPapel> pessoaPapels;
        Endereco endereco = null;

        System.out.print("Nome: ");
        nome = scanner.nextLine();

        System.out.print("CPF: ");
        documento = scanner.nextLine();

        pessoaPapels = cadastrarPapel();

        if (ConsoleUtils.confirmar(scanner, "Deseja cadastrar o Endereço :")) {
            endereco = cadastrarEndereco();
        }

        Pessoa pessoa = Pessoa.builder()
                .nome(nome)
                .documento(new CPF(documento))
                .papeis(pessoaPapels)
                .endereco(endereco)
                .build();

        pessoaService.inserirPessoa(pessoa);

        System.out.println("Pessoa cadastrada com sucesso!");
    }

    private void cadastrarPessoaJuridica() {
        String nome;
        String documento;
        String opcao;
        List<PessoaPapel> pessoaPapels;
        Endereco endereco = null;

        System.out.print("Nome: ");
        nome = scanner.nextLine();

        System.out.print("CNPJ: ");
        documento = scanner.nextLine();

        pessoaPapels = cadastrarPapel();

        if (ConsoleUtils.confirmar(scanner, "Deseja cadastrar o Endereço :")) {
            endereco = cadastrarEndereco();
        }

        Pessoa pessoa = Pessoa.builder()
                .nome(nome)
                .documento(new CNPJ(documento))
                .papeis(pessoaPapels)
                .endereco(endereco)
                .build();

        pessoaService.inserirPessoa(pessoa);

        System.out.println("Pessoa cadastrada com sucesso!");
    }

    public void removerPessoa() {
        int id;
        listarPessoa();

        System.out.print("Digite o ID da pessoa para remover:");
        id = ConsoleUtils.lerInteiro(scanner, "ID");

        try {
            Pessoa pessoa = pessoaService.buscarPorId(id);

            if (!ConsoleUtils.confirmar(scanner, "Tem certeza que deseja Excluir " + pessoa.getNome() + " ?")) {
                return;
            }
            boolean deletado = pessoaService.deletarPessoa(id);

            if (deletado) {
                System.out.println("Pessoa deletada com sucesso!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void atualizarPessoa() {
        listarPessoa();
        System.out.println("Selecione um ID para atualizar");
        Pessoa pessoa = pessoaService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID pessoa"));

        System.out.println("Caso queira deixar o valor antigo, aperta apenas ENTER");
        System.out.println("Nome Atual: " + pessoa.getNome());
        System.out.println("Novo Nome:");

        String nome = scanner.nextLine();
        pessoa.setNome(nome.isEmpty() ? pessoa.getNome() : nome);

        if (pessoa.getDocumento().getTipo().getCodigo() == 0) {
            System.out.println("CPF Atual: " + pessoa.getDocumento());
        } else {
            System.out.println("CNPJ Atual: " + pessoa.getDocumento());
        }
        int op;
        do {

            System.out.println("Atualizar Documento");
            System.out.println("1 - Física");
            System.out.println("2 - Jurídica");
            System.out.println("3 - Mnater Documento");

            op = ConsoleUtils.lerInteiro(scanner, "valor");
        } while (op != 1 && op != 2 && op != 3);

        switch (op) {
            case 1:
                System.out.println("Novo CPF:");
                pessoa.setDocumento(new CPF(scanner.nextLine()));
                break;
            case 2:
                System.out.println("Novo CNPJ:");
                pessoa.setDocumento(new CNPJ(scanner.nextLine()));
                break;
        }

        if (ConsoleUtils.confirmar(scanner, "Deseja alterar os papéis ?")) {
            pessoa.setPapeis(cadastrarPapel());
        }

        do {

            System.out.println("Deseja alterar o Endereço ?");
            System.out.println("1 - Atualizar Endereço");
            System.out.println("2 - Adicionar Endereço");
            System.out.println("3 - Remover Endereço");
            System.out.println("4 - Manter Endereço atual");

            op = ConsoleUtils.lerInteiro(scanner, "valor");
        } while (op != 1 && op != 2 && op != 3 && op != 4);

        switch (op) {
            case 1:
            case 2:
                if (pessoa.getEndereco() != null) {
                    atualizaEndereco(pessoa.getEndereco());
                } else {
                    pessoa.setEndereco(cadastrarEndereco());
                }
                break;
            case 3:
                pessoa.setEndereco(null);
        }

        pessoaService.atualizarPessoa(pessoa);
    }

    public void detalhesPessoa(int id) {
        Pessoa pessoa = pessoaService.buscarPorId(id);
        System.out.println("ID: " + pessoa.getId());
        System.out.println("Nome: " + pessoa.getNome());
        System.out.println("Documento: " + pessoa.getDocumento());
        System.out.println("Tipo: " + pessoa.getDocumento().getTipo());

        System.out.println();

        System.out.println("Papéis:");
        for (PessoaPapel pessoaPapel : pessoa.getPessoaPapel()) {
            System.out.println("- " + pessoaPapel.getDescricao());
        }

        System.out.println();

        if (pessoa.getEndereco() != null) {
            System.out.println("Endereço:");
            System.out.println("Rua: " + pessoa.getEndereco().getLogradouro());
            System.out.println("Número: " + pessoa.getEndereco().getNumero());
            System.out.println(pessoa.getEndereco().getBairro());
            System.out.println(pessoa.getEndereco().getCidade() + " - " + pessoa.getEndereco().getUf());
            System.out.println("CEP " + pessoa.getEndereco().getCep());
        } else {
            System.out.println("Endereço não cadastrado.");
        }

    }

    public void listarPessoa() {
        List<Pessoa> pessoas = pessoaService.buscarTodos();
        String id;
        String nome;
        String doc;
        String tipo;

        System.out.printf(
                "%-5s | %-20s | %-14s | %-10s%n",
                "ID", "NOME", "DOCUMENTO", "TIPO"
        );
        for (Pessoa p : pessoas) {
            id = ConsoleUtils.formatarColuna(String.valueOf(p.getId()), 5);
            nome = ConsoleUtils.formatarColuna(p.getNome(), 20);
            doc = ConsoleUtils.formatarColuna(p.getDocumento().getValor(), 14);
            tipo = p.getDocumento().getTipo().getCodigo() == 0 ? "Física" : "Jurídica";

            System.out.println(id + " | " + nome + " | " + doc + " | " + tipo);

        }
    }

    private Endereco cadastrarEndereco() {
        String cep;
        String logradouro;
        String numero;
        String bairro;
        String cidade;
        String uf;

        System.out.print("Digite o CEP:");
        cep = scanner.nextLine();

        System.out.print("Digite o logradouro:");
        logradouro = scanner.nextLine();

        System.out.print("Digite o número:");
        numero = scanner.nextLine();

        System.out.print("Digite o bairro:");
        bairro = scanner.nextLine();

        System.out.print("Digite a cidade:");
        cidade = scanner.nextLine();

        System.out.print("Digite a UF:");
        uf = scanner.nextLine();

        return Endereco.builder()
                .cep(cep)
                .logradouro(logradouro)
                .numero(numero)
                .bairro(bairro)
                .cidade(cidade)
                .uf(uf)
                .build();
    }

    private void atualizaEndereco(Endereco endereco) {

        System.out.print("Digite o CEP:");
        endereco.setCep(scanner.nextLine());

        System.out.print("Digite o logradouro:");
        endereco.setLogradouro(scanner.nextLine());

        System.out.print("Digite o número:");
        endereco.setNumero(scanner.nextLine());

        System.out.print("Digite o bairro:");
        endereco.setBairro(scanner.nextLine());

        System.out.print("Digite a cidade:");
        endereco.setCidade(scanner.nextLine());

        System.out.print("Digite a UF:");
        endereco.setUf(scanner.nextLine());

    }

    private List<PessoaPapel> cadastrarPapel() {
        List<PessoaPapel> pessoaPapels = new ArrayList<>();

        for (PessoaPapel papel : PessoaPapel.values()) {
            System.out.println("A pessoa é " + papel.getDescricao() + " ?");
            System.out.println("1 - Sim");
            System.out.println("2 - Não");
            if (ConsoleUtils.lerInteiro(scanner, "valor") == 1) {
                pessoaPapels.add(papel);
            }
        }
        return pessoaPapels;
    }
}

