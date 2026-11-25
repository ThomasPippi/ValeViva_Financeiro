package com.valeviva.financeiro.service;

import com.valeviva.financeiro.dto.*;
import com.valeviva.financeiro.enums.StatusQualidade;
import com.valeviva.financeiro.model.Lote;
import com.valeviva.financeiro.model.MovimentacaoEstoque;
import com.valeviva.financeiro.model.OrdemProducao;
import com.valeviva.financeiro.model.Produto;
import com.valeviva.financeiro.repository.LoteRepository;
import com.valeviva.financeiro.repository.MovimentacaoEstoqueRepository;
import com.valeviva.financeiro.repository.OrdemProducaoRepository;
import com.valeviva.financeiro.repository.ProdutoRepository;
import com.valeviva.financeiro.repository.SaldoEstoqueRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoteService {

    private final LoteRepository loteRepository;
    private final ProdutoRepository produtoRepository;
    private final OrdemProducaoRepository ordemProducaoRepository;
    private final SaldoEstoqueRepository saldoEstoqueRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public LoteService(LoteRepository loteRepository,
                       ProdutoRepository produtoRepository,
                       OrdemProducaoRepository ordemProducaoRepository,
                       SaldoEstoqueRepository saldoEstoqueRepository,
                       MovimentacaoEstoqueRepository movimentacaoEstoqueRepository) {
        this.loteRepository = loteRepository;
        this.produtoRepository = produtoRepository;
        this.ordemProducaoRepository = ordemProducaoRepository;
        this.saldoEstoqueRepository = saldoEstoqueRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    public List<LoteResponseDTO> listar() {
        return loteRepository.findAll().stream().map(this::toResponse).toList();
    }

    public Lote buscarPorCodigo(String codigo) {
        return loteRepository.findByCodigoIgnoreCase(codigo.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado: " + codigo));
    }

    @Transactional
    public LoteResponseDTO criar(LoteRequestDTO dto) {
        if (dto.codigo() == null || dto.codigo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código do lote é obrigatório");
        }
        if (loteRepository.existsByCodigoIgnoreCase(dto.codigo().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe lote com código " + dto.codigo());
        }

        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

        Lote lote = new Lote();
        lote.setCodigo(dto.codigo().trim().toUpperCase());
        lote.setProduto(produto);
        lote.setDataFabricacao(dto.dataFabricacao() != null ? dto.dataFabricacao() : LocalDate.now());
        lote.setDataValidade(dto.dataValidade());
        lote.setStatusQualidade(StatusQualidade.QUARENTENA);

        if (dto.ordemOrigemId() != null) {
            OrdemProducao op = ordemProducaoRepository.findById(dto.ordemOrigemId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ordem de produção não encontrada"));
            lote.setOrdemOrigem(op);
        }

        return toResponse(loteRepository.save(lote));
    }

    @Transactional
    public LoteResponseDTO atualizarQualidade(String codigo, QualidadeUpdateDTO dto) {
        Lote lote = buscarPorCodigo(codigo);
        if (dto.statusQualidade() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "statusQualidade é obrigatório");
        }
        lote.setStatusQualidade(dto.statusQualidade());
        lote.setObservacaoQualidade(dto.observacao());
        return toResponse(loteRepository.save(lote));
    }

    public RastreabilidadeResponseDTO rastreabilidade(String codigo) {
        Lote lote = buscarPorCodigo(codigo);
        LoteResponseDTO loteDto = toResponse(lote);

        BigDecimal saldoAtual = saldoEstoqueRepository.findAll().stream()
                .filter(s -> lote.getId().equals(s.getLoteId()))
                .map(s -> s.getQuantidade() != null ? s.getQuantidade() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<MovimentacaoEstoque> movs = movimentacaoEstoqueRepository.findAll().stream()
                .filter(m -> lote.getId().equals(m.getLoteId()))
                .toList();

        List<MovimentacaoRastreioDTO> historico = movs.stream()
                .map(m -> new MovimentacaoRastreioDTO(
                        m.getId(),
                        m.getTipo() != null ? m.getTipo().name() : "",
                        m.getQuantidade() != null ? m.getQuantidade().toPlainString() : "0",
                        m.getDataHora() != null ? m.getDataHora().toString() : "",
                        m.getUsuarioId() != null ? "Usuário #" + m.getUsuarioId() : "—"
                ))
                .toList();

        return new RastreabilidadeResponseDTO(loteDto, saldoAtual, historico);
    }

    /**
     * Bloqueia (HTTP 422 Unprocessable Entity) expedição de lote vencido,
     * não aprovado na qualidade ou com registro ANVISA vencido.
     */
    public void validarLoteParaExpedicao(Long loteId) {
        if (loteId == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Expedição bloqueada: lote não informado.");
        }

        Lote lote = loteRepository.findById(loteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado: " + loteId));

        if (lote.isVencido()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Expedição bloqueada: lote " + lote.getCodigo() + " vencido em " + lote.getDataValidade() + ".");
        }

        if (lote.getStatusQualidade() != StatusQualidade.APROVADO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Expedição bloqueada: lote " + lote.getCodigo() + " está com qualidade " + lote.getStatusQualidade() + ".");
        }

        if (isAnvisaVencida(lote.getProduto().getVencimento_anvisa())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Expedição bloqueada: ANVISA vencida para o produto " + lote.getProduto().getNome() + ".");
        }
    }

    private boolean isAnvisaVencida(LocalDate vencimentoAnvisa) {
        return vencimentoAnvisa != null && vencimentoAnvisa.isBefore(LocalDate.now());
    }

    private LoteResponseDTO toResponse(Lote lote) {
        Produto p = lote.getProduto();
        boolean anvisaVencida = isAnvisaVencida(p.getVencimento_anvisa());
        return new LoteResponseDTO(
                lote.getId(),
                lote.getCodigo(),
                p.getId(),
                p.getNome(),
                p.getRegistro_anvisa(),
                p.getVencimento_anvisa(),
                lote.getOrdemOrigem() != null ? lote.getOrdemOrigem().getId() : null,
                lote.getDataFabricacao(),
                lote.getDataValidade(),
                lote.isVencido(),
                anvisaVencida,
                lote.getStatusQualidade(),
                lote.getObservacaoQualidade()
        );
    }
}