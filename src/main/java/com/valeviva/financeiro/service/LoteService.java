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