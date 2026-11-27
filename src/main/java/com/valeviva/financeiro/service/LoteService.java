package com.valeviva.financeiro.service;

import com.valeviva.financeiro.dto.LoteRequestDTO;
import com.valeviva.financeiro.dto.LoteResponseDTO;
import com.valeviva.financeiro.dto.MovimentacaoRastreioDTO;
import com.valeviva.financeiro.dto.QualidadeUpdateDTO;
import com.valeviva.financeiro.dto.RastreabilidadeResponseDTO;
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

    public LoteService(
            LoteRepository loteRepository,
            ProdutoRepository produtoRepository,
            OrdemProducaoRepository ordemProducaoRepository,
            SaldoEstoqueRepository saldoEstoqueRepository,
            MovimentacaoEstoqueRepository movimentacaoEstoqueRepository
    ) {
        this.loteRepository = loteRepository;
        this.produtoRepository = produtoRepository;
        this.ordemProducaoRepository = ordemProducaoRepository;
        this.saldoEstoqueRepository = saldoEstoqueRepository;
        this.movimentacaoEstoqueRepository = movimentacaoEstoqueRepository;
    }

    @Transactional(readOnly = true)
    public List<LoteResponseDTO> listar() {
        return loteRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public LoteResponseDTO criar(LoteRequestDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do lote são obrigatórios.");
        }
        if (dto.codigo() == null || dto.codigo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código do lote é obrigatório.");
        }
        if (dto.produtoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto do lote é obrigatório.");
        }
        if (dto.dataFabricacao() == null || dto.dataValidade() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datas de fabricação e validade são obrigatórias.");
        }
        if (dto.dataValidade().isBefore(dto.dataFabricacao())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Data de validade não pode ser anterior à data de fabricação.");
        }

        String codigoLimpo = dto.codigo().trim();
        if (loteRepository.existsByCodigoIgnoreCase(codigoLimpo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um lote com o código informado.");
        }

        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado."));

        OrdemProducao ordemOrigem = null;
        if (dto.ordemOrigemId() != null) {
            ordemOrigem = ordemProducaoRepository.findById(dto.ordemOrigemId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ordem de produção não encontrada."));
        }

        Lote lote = new Lote();
        lote.setCodigo(codigoLimpo);
        lote.setProduto(produto);
        lote.setOrdemOrigem(ordemOrigem);
        lote.setDataFabricacao(dto.dataFabricacao());
        lote.setDataValidade(dto.dataValidade());
        lote.setStatusQualidade(StatusQualidade.QUARENTENA);

        Lote salvo = loteRepository.save(lote);
        return toResponse(salvo);
    }

    @Transactional
    public LoteResponseDTO atualizarQualidade(String codigo, QualidadeUpdateDTO dto) {
        if (dto == null || dto.statusQualidade() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status de qualidade é obrigatório.");
        }

        Lote lote = buscarPorCodigo(codigo);
        lote.setStatusQualidade(dto.statusQualidade());
        lote.setObservacaoQualidade(dto.observacao() != null ? dto.observacao().trim() : null);

        Lote salvo = loteRepository.save(lote);
        return toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public void validarLoteParaExpedicao(Long loteId) {
        if (loteId == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Lote é obrigatório para expedição.");
        }

        Lote lote = loteRepository.findById(loteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado."));

        if (lote.isVencido()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Lote vencido não pode ser expedido.");
        }

        if (lote.getStatusQualidade() != StatusQualidade.APROVADO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Lote precisa estar APROVADO na qualidade para expedição.");
        }

        if (isAnvisaVencida(lote.getProduto())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Produto do lote está com registro ANVISA vencido.");
        }
    }

    @Transactional(readOnly = true)
    public RastreabilidadeResponseDTO rastreabilidade(String codigo) {
        Lote lote = buscarPorCodigo(codigo);
        LoteResponseDTO loteDto = toResponse(lote);

        BigDecimal saldoAtual = saldoEstoqueRepository.findAll().stream()
                .filter(s -> lote.getId().equals(s.getLoteId()))
                .map(s -> s.getQuantidade() != null ? s.getQuantidade() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<MovimentacaoEstoque> movs = movimentacaoEstoqueRepository.findByLoteId(lote.getId());

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

    private Lote buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código do lote é obrigatório.");
        }
        return loteRepository.findByCodigoIgnoreCase(codigo.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote não encontrado: " + codigo));
    }

    private boolean isAnvisaVencida(Produto produto) {
        if (produto == null || produto.getVencimento_anvisa() == null) {
            return false;
        }
        return produto.getVencimento_anvisa().isBefore(LocalDate.now());
    }

    private LoteResponseDTO toResponse(Lote lote) {
        Produto produto = lote.getProduto();
        boolean anvisaVencida = isAnvisaVencida(produto);

        return new LoteResponseDTO(
                lote.getId(),
                lote.getCodigo(),
                produto != null ? produto.getId() : null,
                produto != null ? produto.getNome() : null,
                produto != null ? produto.getRegistro_anvisa() : null,
                produto != null ? produto.getVencimento_anvisa() : null,
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