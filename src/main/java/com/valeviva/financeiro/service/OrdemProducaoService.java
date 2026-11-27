package com.valeviva.financeiro.service;

import com.valeviva.financeiro.enums.StatusOrdem;
import com.valeviva.financeiro.model.MateriaPrima;
import com.valeviva.financeiro.model.OrdemProducao;
import com.valeviva.financeiro.model.Produto;
import com.valeviva.financeiro.repository.MateriaPrimaRepository;
import com.valeviva.financeiro.repository.OrdemProducaoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdemProducaoService {

    private final OrdemProducaoRepository ordemProducaoRepository;
    private final MateriaPrimaRepository materiaPrimaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public OrdemProducaoService(OrdemProducaoRepository ordemProducaoRepository,
                                MateriaPrimaRepository materiaPrimaRepository) {
        this.ordemProducaoRepository = ordemProducaoRepository;
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
    public OrdemProducaoDetalheDTO confirmarMateriais(Long id) {
        OrdemProducao op = ordemProducaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ordem de produção não encontrada."));

        String statusAtual = op.getStatus() != null ? op.getStatus() : "EMITIDA";
        if (!"EMITIDA".equals(statusAtual) && !StatusOrdem.AGUARDANDO_MATERIAIS.name().equals(statusAtual)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível confirmar materiais para ordem com status " + statusAtual + "."
            );
        }

        List<ItemNecessidadeDTO> faltas = calcularFaltas(op.getProduto(), op.getQuantidade());

        if (!faltas.isEmpty()) {
            op.setStatus(StatusOrdem.AGUARDANDO_MATERIAIS.name());
            ordemProducaoRepository.save(op);
            return toDetalheDTO(op, faltas);
        }

        int qtdOp = op.getQuantidade() != null ? op.getQuantidade() : 0;
        BigDecimal qtdOpBd = BigDecimal.valueOf(qtdOp);

        for (MateriaPrima mp : op.getProduto().getMateriasPrimas()) {
            BigDecimal qtdPorUnidade = buscarQuantidadeNecessaria(op.getProduto().getId(), mp.getId());
            BigDecimal necessario = qtdPorUnidade.multiply(qtdOpBd);
            BigDecimal disponivel = BigDecimal.valueOf(mp.getQuantidadeEstoque());
            BigDecimal novoSaldo = disponivel.subtract(necessario).max(BigDecimal.ZERO);

            mp.setQuantidadeEstoque(novoSaldo.doubleValue());
            materiaPrimaRepository.save(mp);
        }

        op.setStatus(StatusOrdem.LIBERADA.name());
        OrdemProducao salva = ordemProducaoRepository.save(op);
        return toDetalheDTO(salva, List.of());
    }

    private List<ItemNecessidadeDTO> calcularFaltas(Produto produto, Integer quantidadeOp) {
        List<ItemNecessidadeDTO> faltas = new ArrayList<>();
        BigDecimal qtdOpBd = BigDecimal.valueOf(quantidadeOp != null ? quantidadeOp : 0);

        for (MateriaPrima mp : produto.getMateriasPrimas()) {
            BigDecimal qtdPorUnidade = buscarQuantidadeNecessaria(produto.getId(), mp.getId());
            BigDecimal necessario = qtdPorUnidade.multiply(qtdOpBd);
            BigDecimal disponivel = BigDecimal.valueOf(mp.getQuantidadeEstoque());
            BigDecimal falta = necessario.subtract(disponivel).max(BigDecimal.ZERO);

            if (falta.compareTo(BigDecimal.ZERO) > 0) {
                faltas.add(new ItemNecessidadeDTO(
                        mp.getId(),
                        "MP-" + mp.getId(),
                        mp.getNome(),
                        necessario.doubleValue(),
                        disponivel.doubleValue(),
                        falta.doubleValue()
                ));
            }
        }
        return faltas;
    }

    private BigDecimal buscarQuantidadeNecessaria(Long produtoId, Long materiaPrimaId) {
        List<?> rows = entityManager.createNativeQuery(
                        "SELECT quantidade_necessaria FROM produto_materia_prima WHERE produto_id = ?1 AND materia_prima_id = ?2")
                .setParameter(1, produtoId)
                .setParameter(2, materiaPrimaId)
                .getResultList();

        if (rows.isEmpty() || rows.get(0) == null) {
            return BigDecimal.ONE;
        }
        if (rows.get(0) instanceof BigDecimal bd) {
            return bd;
        }
        return BigDecimal.valueOf(((Number) rows.get(0)).doubleValue());
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
