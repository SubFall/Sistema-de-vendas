package test;

import domain.documento.CPF;
import domain.movimento.Movimento;
import domain.movimento.MovimentoItem;
import domain.movimento.StatusMovimento;
import domain.pessoa.Pessoa;
import domain.pessoa.PessoaPapel;
import domain.produto.Produto;
import service.CategoriaService;
import service.MovimentoService;
import service.PessoaService;
import service.ProdutoService;
import ui.categoria.CategoriaMenu;
import ui.movimento.MovimentoMenu;
import ui.pessoa.PessoaMenu;
import ui.produto.ProdutoMenu;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Principal {
    static void main(String[] args) {

        PessoaService pessoaService = new PessoaService();
        PessoaMenu pessoaMenu = new PessoaMenu(pessoaService);
        CategoriaService categoriaService = new CategoriaService();
        CategoriaMenu categoriaMenu = new CategoriaMenu(categoriaService);
        ProdutoService produtoService = new ProdutoService();
        ProdutoMenu produtoMenu = new ProdutoMenu(produtoService, categoriaService, categoriaMenu);
        MovimentoService movimentoService = new MovimentoService();
        MovimentoMenu movimentoMenu = new MovimentoMenu(movimentoService);

//        produtoMenu.iniciar();
//        pessoaMenu.iniciar();

        movimentoMenu.iniciar();
    }

}