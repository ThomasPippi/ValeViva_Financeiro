package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroOrdemProducao;
import com.valeviva.financeiro.service.OrdemProducaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ordens-producao")
public class OrdemProducaoController {

    private final OrdemProducaoService ordemProducaoService;

    public OrdemProducaoController(OrdemProducaoService ordemProducaoService) {
        this.ordemProducaoService = ordemProducaoService;
    }

    public record CriarOrdemRequest(
            Long produtoId,
            Integer quantidade,
            String prazo,
            Integer prioridade
    ) {}

    @PostMapping
    public ResponseEntity<OrdemProducaoService.OrdemProducaoDetalheDTO> criar(
            @RequestBody @Valid CriarOrdemRequest req) {
        CadastroOrdemProducao dto = new CadastroOrdemProducao(req.produtoId(), req.quantidade());
        LocalDate prazoData = (req.prazo() != null && !req.prazo().isBlank())
                ? LocalDate.parse(req.prazo().sliceOrTrim())
                : null;
        OrdemProducaoService.OrdemProducaoDetalheDTO criada =
                ordemProducaoService.criar(dto, prazoData, req.prioridade());
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @GetMapping
    public ResponseEntity<List<OrdemProducaoService.OrdemProducaoDetalheDTO>> listar(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(ordemProducaoService.listar(status));
    }

    @PostMapping("/{id}/confirmar-materiais")
    public ResponseEntity<OrdemProducaoService.OrdemProducaoDetalheDTO> confirmarMateriais(
            @PathVariable Long id) {
        return ResponseEntity.ok(ordemProducaoService.confirmarMateriais(id));
    }
}