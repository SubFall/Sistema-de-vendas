package test;

import domain.documento.CPF;
import domain.movimento.Movimento;
import domain.movimento.MovimentoItem;
import domain.pessoa.Pessoa;
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
                .id(20)
                .nome("Teste")
                .documento(new CPF("06032055183"))
                .build();



        Produto build = Produto.builder().id(5).descricao("teste 1").precoVenda(new BigDecimal("19.99")).precoCusto(new BigDecimal("9.99")).build();
        Produto build1 = Produto.builder().id(9).descricao("teste 2").precoVenda(new BigDecimal("15.99")).precoCusto(new BigDecimal("7.99")).build();
        Produto build2 = Produto.builder().id(11).descricao("teste 3").precoVenda(new BigDecimal("17.99")).precoCusto(new BigDecimal("11.99")).build();

        Movimento movimento = Movimento.builder()
                .id(1)
                .pessoa(teste)
                .dataMovimento(LocalDateTime.now())
                .movimentoItens(MovimentoItem.builder().produto(build).valorUnitario(build.getPrecoVenda()).quantidade(new BigDecimal("2")).build())
                .movimentoItens(MovimentoItem.builder().produto(build1).valorUnitario(build1.getPrecoVenda()).quantidade(new BigDecimal("4")).build())
                .build();

//        MovimentoItem mi1 = MovimentoItem.builder().produto(build).valorUnitario(build.getPrecoVenda()).quantidade(new BigDecimal("2")).build();
//        MovimentoItem mi2 = MovimentoItem.builder().produto(build1).valorUnitario(build1.getPrecoVenda()).quantidade(new BigDecimal("4")).build();

//        List<MovimentoItem> movimentoItems = new ArrayList<>();
//        movimentoItems.add(mi1);
//        movimentoItems.add(mi2);
//        movimento.setMovimentoItens(movimentoItems);

        movimento.adicionarItem(MovimentoItem.builder().produto(build2).valorUnitario(build1.getPrecoVenda()).quantidade(new BigDecimal("1")).build());

        MovimentoService movimentoService = new MovimentoService();

        movimentoService.inserirMovimento(movimento);


    }
}