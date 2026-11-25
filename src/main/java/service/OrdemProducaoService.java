package com.valeviva.financeiro.service;

import com.valeviva.financeiro.dto.CadastroOrdemProducao;
import com.valeviva.financeiro.model.MateriaPrima;
import com.valeviva.financeiro.model.OrdemProducao;
import com.valeviva.financeiro.model.Produto;
import com.valeviva.financeiro.repository.MateriaPrimaRepository;
import com.valeviva.financeiro.repository.OrdemProducaoRepository;
import com.valeviva.financeiro.repository.ProdutoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdemProducaoService {

    private final OrdemProducaoRepository ordemProducaoRepository;
    private final ProdutoRepository produtoRepository;
    private final MateriaPrimaRepository materiaPrimaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public OrdemProducaoService(OrdemProducaoRepository ordemProducaoRepository,
                                ProdutoRepository produtoRepository,
                                MateriaPrimaRepository materiaPrimaRepository) {
        this.ordemProducaoRepository = ordemProducaoRepository;
        this.produtoRepository = produtoRepository;
        this.materiaPrimaRepository = materiaPrimaRepository;
    }

    public record ItemNecessidadeDTO(
            Long materialId,
            String codigoMaterial,
            String descricaoMaterial,
            double necessario,
            double disponivel,
            double falta
    ) {}

    public record OrdemProducaoDetalheDTO(
            Long id,
            String numero,
            Long produtoId,
            String codigoProduto,
            String descricaoProduto,
            Integer quantidade,
            String prazo,
            Integer prioridade,
            String status,
            String emitidaEm,
            List<ItemNecessidadeDTO> faltas
    ) {}

    @Transactional
    public OrdemProducaoDetalheDTO criar(CadastroOrdemProducao dto, LocalDate prazo, Integer prioridade) {
        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado."));

        OrdemProducao op = new OrdemProducao();
        op.setProduto(produto);
        op.setQuantidade(dto.quantidade());
        op.setDataCriacao(LocalDateTime.now());
        op.setPrazo(prazo);
        op.setPrioridade(prioridade != null ? prioridade : 1);

        List<ItemNecessidadeDTO> faltas = calcularFaltas(produto, dto.quantidade());
        op.setStatus(faltas.isEmpty() ? "LIBERADA" : "AGUARDANDO_MATERIAIS");

        OrdemProducao salva = ordemProducaoRepository.save(op);
        return toDetalheDTO(salva, faltas);
    }

    public List<OrdemProducaoDetalheDTO> listar(String status) {
        return ordemProducaoRepository.findAll().stream()
                .filter(op -> status == null || status.isBlank() || "TODAS".equalsIgnoreCase(status)
                        || status.equalsIgnoreCase(op.getStatus()))
                .map(op -> toDetalheDTO(op, calcularFaltas(op.getProduto(), op.getQuantidade())))
                .toList();
    }

    @Transactional
    public OrdemProducaoDetalheDTO confirmarMateriais(Long id) {
        OrdemProducao op = ordemProducaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ordem de produção não encontrada."));

        List<ItemNecessidadeDTO> faltas = calcularFaltas(op.getProduto(), op.getQuantidade());

        if (!faltas.isEmpty()) {
            op.setStatus("AGUARDANDO_MATERIAIS");
            ordemProducaoRepository.save(op);
            return toDetalheDTO(op, faltas);
        }

        // Baixa os insumos conforme quantidade_necessaria * quantidade da OP
        for (MateriaPrima mp : op.getProduto().getMateriasPrimas()) {
            double qtdPorUnidade = buscarQuantidadeNecessaria(op.getProduto().getId(), mp.getId());
            double necessario = qtdPorUnidade * op.getQuantidade();
            double novoSaldo = Math.max(0.0, mp.getQuantidadeEstoque() - necessario);
            mp.setQuantidadeEstoque(novoSaldo);
            materiaPrimaRepository.save(mp);
        }

        op.setStatus("LIBERADA");
        OrdemProducao salva = ordemProducaoRepository.save(op);
        return toDetalheDTO(salva, List.of());
    }

    private List<ItemNecessidadeDTO> calcularFaltas(Produto produto, Integer quantidadeOp) {
        List<ItemNecessidadeDTO> faltas = new ArrayList<>();
        int qtd = quantidadeOp != null ? quantidadeOp : 0;

        for (MateriaPrima mp : produto.getMateriasPrimas()) {
            double qtdPorUnidade = buscarQuantidadeNecessaria(produto.getId(), mp.getId());
            double necessario = qtdPorUnidade * qtd;
            double disponivel = mp.getQuantidadeEstoque();
            double falta = Math.max(0.0, necessario - disponivel);

            if (falta > 0) {
                faltas.add(new ItemNecessidadeDTO(
                        mp.getId(),
                        "MP-" + mp.getId(),
                        mp.getNome(),
                        necessario,
                        disponivel,
                        falta
                ));
            }
        }
        return faltas;
    }

    private double buscarQuantidadeNecessaria(Long produtoId, Long materiaPrimaId) {
        List<?> rows = entityManager.createNativeQuery(
                        "SELECT quantidade_necessaria FROM produto_materia_prima WHERE produto_id = ?1 AND materia_prima_id = ?2")
                .setParameter(1, produtoId)
                .setParameter(2, materiaPrimaId)
                .getResultList();

        if (rows.isEmpty() || rows.get(0) == null) {
            return 1.0;
        }
        return ((Number) rows.get(0)).doubleValue();
    }

    private OrdemProducaoDetalheDTO toDetalheDTO(OrdemProducao op, List<ItemNecessidadeDTO> faltas) {
        Produto p = op.getProduto();
        return new OrdemProducaoDetalheDTO(
                op.getId(),
                "OP-" + op.getId(),
                p != null ? p.getId() : null,
                p != null ? p.getCodigo() : null,
                p != null ? p.getNome() : null,
                op.getQuantidade(),
                op.getPrazo() != null ? op.getPrazo().toString() : "",
                op.getPrioridade() != null ? op.getPrioridade() : 1,
                op.getStatus() != null ? op.getStatus() : "EMITIDA",
                op.getDataCriacao() != null ? op.getDataCriacao().toString() : null,
                faltas
        );
    }
}