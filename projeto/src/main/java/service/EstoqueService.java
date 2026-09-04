package service;

import domain.estoque.Estoque;
import dto.ProdutoEstoqueDTO;
import repository.EstoqueRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public class EstoqueService {
    private final EstoqueRepository estoqueRepository;

    public EstoqueService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public void salvarEstoque(Connection conn, int idProduto, BigDecimal saldo) {
        Estoque estoque = estoqueRepository.buscarPorIdProduto(conn, idProduto);

        if (estoque == null) {
            estoqueRepository.inserirEstoque(conn, idProduto, saldo);
            return;
        }
        estoqueRepository.atualizarEstoque(conn, idProduto, saldo);
    }

    public Estoque buscarPorIdProduto(Connection conn, int idProduto) {
        Estoque estoque = estoqueRepository.buscarPorIdProduto(conn, idProduto);
        if (estoque != null) {
            return estoque;
        }
        return Estoque.builder().idProduto(idProduto).quantidade(BigDecimal.ZERO).build();
    }

    public Estoque buscarPorIdProduto(int idProduto) {
        Estoque estoque = estoqueRepository.buscarPorIdProduto(idProduto);
        if (estoque != null) {
            return estoque;
        }
        return Estoque.builder().idProduto(idProduto).quantidade(BigDecimal.ZERO).build();
    }

    public List<ProdutoEstoqueDTO> buscarProdutosEstoque() {
        return estoqueRepository.buscarProdutosEstoque();
    }
}
