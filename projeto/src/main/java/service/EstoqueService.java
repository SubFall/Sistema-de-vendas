package service;

import domain.estoque.Estoque;
import dto.ProdutoEstoqueDTO;
import lombok.RequiredArgsConstructor;
import repository.EstoqueRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class EstoqueService {
    private final EstoqueRepository estoqueRepository;

    public void salvarEstoque(Connection conn, Long idProduto, BigDecimal saldo) {
        Estoque estoque = estoqueRepository.buscarPorIdProduto(conn, idProduto);

        if (estoque == null) {
            estoqueRepository.inserirEstoque(conn, idProduto, saldo);
            return;
        }
        estoqueRepository.atualizarEstoque(conn, idProduto, saldo);
    }

    public Estoque buscarPorIdProduto(Connection conn, Long idProduto) {
        Estoque estoque = estoqueRepository.buscarPorIdProduto(conn, idProduto);
        if (estoque != null) {
            return estoque;
        }
        return Estoque.builder().idProduto(idProduto).quantidade(BigDecimal.ZERO).build();
    }

    public Estoque buscarPorIdProduto(Long idProduto) {
        return estoqueRepository.buscarPorIdProduto(idProduto);
    }

    public List<ProdutoEstoqueDTO> buscarTodosProdutosEstoque() {
        List<ProdutoEstoqueDTO> produtoEstoqueDTOList = new ArrayList<>();
        try {
            produtoEstoqueDTOList = estoqueRepository.buscarTodosProdutosEstoque();

            return produtoEstoqueDTOList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return produtoEstoqueDTOList;
    }
}
