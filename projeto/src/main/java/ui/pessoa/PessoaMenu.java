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
            System.out.println("|0 - Voltar                   |");
            System.out.println("|*****************************|");

            System.out.print("Opção: ");
            opcao = ConsoleUtils.lerInteiro(scanner, "Opção");

            switch (opcao) {
                case 1 -> {
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
                }
                case 2 -> {
//                    removerPessoa();
                    inativarPessoa();
                }
                case 3 -> atualizarPessoa();
                case 4 -> {
                    System.out.println("Digite o ID da pessoa: ");

                    try {
                        detalhesPessoa(ConsoleUtils.lerInteiro(scanner, "ID"));
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 5 -> {
                    int op;
                    do {
                        System.out.println("1 - Listar todoas as pessoas");
                        System.out.println("2 - Listar pessoas por descricao");
                        System.out.println("3 - Listar pessoa por id");
                        System.out.println("4 - Listar somente ativos");

                        op = ConsoleUtils.lerInteiro(scanner, "Opção");

                    } while (op != 1 && op != 2 && op != 3 && op != 4);

                    listar(op);
                }
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida");
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
        listar(4); //somente ativo

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

    public void inativarPessoa() {
        listar(4); //somente ativo

        try {
            System.out.print("Digite o ID da pessoa para inativar:");
            Pessoa pessoa = pessoaService.buscarPorIdAtivo(ConsoleUtils.lerInteiro(scanner, "ID"));

            if (!ConsoleUtils.confirmar(scanner, "Tem certeza que deseja Inativar " + pessoa.getNome() + " ?")) {
                return;
            }
            boolean inativar = pessoaService.inativarPessoa(pessoa.getId());

            if (inativar) {
                System.out.printf("Pessoa %s inativado com sucesso!", pessoa.getNome());
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void atualizarPessoa() {
        listar(1); //todos

        try {
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
                case 1, 2 -> {
                    if (pessoa.getEndereco() != null) {
                        atualizaEndereco(pessoa.getEndereco());
                    } else {
                        pessoa.setEndereco(cadastrarEndereco());
                    }
                }
                case 3 -> pessoa.setEndereco(null);
            }

            do {
                System.out.println("Ativo: " + (pessoa.isAtivo() ? "SIM" : "NÃO"));
                System.out.println("Deseja mudar ?:");
                System.out.println("1 - Ativo");
                System.out.println("2 - Inativo");
                System.out.println("3 - Manter atual");

                op = ConsoleUtils.lerInteiro(scanner, "Opção");
            } while (op != 1 && op != 2 && op != 3);

            if (op == 1) {
                pessoa.setAtivo(true);
            } else if (op == 2) {
                pessoa.setAtivo(false);
            }

            if (pessoaService.atualizarPessoa(pessoa)) {
                System.out.println("Pessoa " + pessoa.getNome() + " atualizado com sucesso!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void detalhesPessoa(int id) {
        Pessoa pessoa = pessoaService.buscarPorId(id);
        System.out.println("ID: " + pessoa.getId());
        System.out.println("Nome: " + pessoa.getNome());
        System.out.println("Documento: " + pessoa.getDocumento());
        System.out.println("Tipo: " + pessoa.getDocumento().getTipo());
        System.out.println("Ativo: " + (pessoa.isAtivo() ? "SIM" : "NÃO"));

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

    public void listar(int idLista) {
        List<Pessoa> pessoas = new ArrayList<>();
        switch (idLista) {
            case 1 -> pessoas = pessoaService.buscarTodos();
            case 2 -> {
                System.out.print("Digite o nome: ");
                pessoas = pessoaService.buscarPorNome(scanner.nextLine());
            }
            case 3 -> {
                System.out.print("Digite o ID: ");
                try {
                    pessoas.add(pessoaService.buscarPorId(ConsoleUtils.lerInteiro(scanner, "ID")));
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 4 -> pessoas = pessoaService.buscarTodosAtivo();
            default -> System.out.println("opção inválida!");
        }
        exibirGrid(pessoas);
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

    private void exibirCabecalhoGrid() {
        System.out.printf(
                "%-5s | %-20s | %-14s | %-10s%n",
                "ID", "NOME", "DOCUMENTO", "TIPO"
        );
    }

    public void exibirGrid(List<Pessoa> pessoas) {
        String id;
        String nome;
        String doc;
        String tipo;
        String ativo;

        if (pessoas.isEmpty()) {
            System.out.println("Nenhuma pessoa encontrado.");
            return;
        }
        exibirCabecalhoGrid();
        for (Pessoa p : pessoas) {
            id = ConsoleUtils.formatarColuna(String.valueOf(p.getId()), 5);
            nome = ConsoleUtils.formatarColuna(p.getNome(), 20);
            doc = ConsoleUtils.formatarColuna(p.getDocumento().getValor(), 14);
            tipo = ConsoleUtils.formatarColuna(p.getDocumento().getTipo().getCodigo() == 0 ? "Física" : "Jurídica", 10);
            ativo = ConsoleUtils.formatarColuna(p.isAtivo() ? "SIM" : "NÃO", 3);

            System.out.printf("%s | %s | %s | %s | %s%n", id, nome, doc, tipo, ativo);
        }
    }
}

