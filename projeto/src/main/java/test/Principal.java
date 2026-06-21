package test;

import domain.documento.CPF;
import domain.pessoa.Pessoa;
import domain.pessoa.PessoaPapel;
import service.CategoriaService;
import service.PessoaService;
import service.ProdutoService;
import ui.categoria.CategoriaMenu;
import ui.pessoa.PessoaMenu;
import ui.produto.ProdutoMenu;

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
        pessoaMenu.iniciar();


    }
}