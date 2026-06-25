package test;

import domain.documento.CPF;
import domain.movimento.Movimento;
import domain.movimento.MovimentoItem;
import domain.movimento.StatusMovimento;
import domain.pessoa.Pessoa;
import domain.pessoa.PessoaPapel;
import domain.produto.Produto;
import repository.MovimentoRepository;
import service.CategoriaService;
import service.MovimentoService;
import service.PessoaService;
import service.ProdutoService;
import ui.categoria.CategoriaMenu;
import ui.pessoa.PessoaMenu;
import ui.produto.ProdutoMenu;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Principal {
    static void main(String[] args) {

        PessoaService pessoaService = new PessoaService();
        PessoaMenu pessoaMenu = new PessoaMenu(pessoaService);
        CategoriaService categoriaService = new CategoriaService();
        CategoriaMenu categoriaMenu = new CategoriaMenu(categoriaService);
        ProdutoService produtoService = new ProdutoService();
        ProdutoMenu produtoMenu = new ProdutoMenu(produtoService, categoriaService, categoriaMenu);

//        produtoMenu.iniciar();
//        pessoaMenu.iniciar();

        Pessoa teste = Pessoa.builder()
                .id(41)
                .nome("Teste")
                .documento(new CPF("06032055183"))
                .papeis(List.of(PessoaPapel.FUNCIONARIO, PessoaPapel.CLIENTE))
                .build();

        Pessoa funcionario = Pessoa.builder()
                .id(20)
                .nome("Teste")
                .documento(new CPF("06032055183"))
                .papeis(List.of(PessoaPapel.CLIENTE, PessoaPapel.FUNCIONARIO))
                .build();


        Produto build = Produto.builder().id(5).descricao("teste 1").precoVenda(new BigDecimal("19.99")).precoCusto(new BigDecimal("9.99")).build();
        Produto build1 = Produto.builder().id(9).descricao("teste 2").precoVenda(new BigDecimal("15.99")).precoCusto(new BigDecimal("7.99")).build();
        Produto build2 = Produto.builder().id(11).descricao("teste 3").precoVenda(new BigDecimal("17.99")).precoCusto(new BigDecimal("11.99")).build();

        Movimento movimento = Movimento.builder()
                .id(1)
                .pessoa(teste)
                .funcionario(funcionario)
                .dataMovimento(LocalDateTime.now())
                .movimentoItens(List.of(MovimentoItem.builder().produto(build).valorUnitario(build.getPrecoVenda()).quantidade(new BigDecimal("2")).build(),
                                MovimentoItem.builder().produto(build1).valorUnitario(build1.getPrecoVenda()).quantidade(new BigDecimal("4")).build()))
                .build();

//        MovimentoItem mi1 = MovimentoItem.builder().produto(build).valorUnitario(build.getPrecoVenda()).quantidade(new BigDecimal("2")).build();
//        MovimentoItem mi2 = MovimentoItem.builder().produto(build1).valorUnitario(build1.getPrecoVenda()).quantidade(new BigDecimal("4")).build();

//        List<MovimentoItem> movimentoItems = new ArrayList<>();
//        movimentoItems.add(mi1);
//        movimentoItems.add(mi2);
//        movimento.setMovimentoItens(movimentoItems);

        movimento.adicionarItem(MovimentoItem.builder().produto(build2).valorUnitario(build1.getPrecoVenda()).quantidade(new BigDecimal("1")).build());

        MovimentoService movimentoService = new MovimentoService();

//        movimentoService.inserirMovimento(movimento);

//        Movimento movimento1 = movimentoService.buscarPorId(7);

        System.out.println(movimentoService.estornarMovimento(7));


    }
}