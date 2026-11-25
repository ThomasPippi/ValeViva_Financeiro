package com.valeviva.financeiro.controller;

import com.valeviva.financeiro.dto.lote.*;
import com.valeviva.financeiro.service.LoteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lotes")
public class LoteController {

    private final LoteService loteService;

    public LoteController(LoteService loteService) {
        this.loteService = loteService;
    }

    @GetMapping
    public List<LoteResponseDTO> listar() {
        return loteService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoteResponseDTO criar(@RequestBody LoteRequestDTO dto) {
        return loteService.criar(dto);
    }

    @PatchMapping("/{codigo}/qualidade")
    public LoteResponseDTO atualizarQualidade(@PathVariable String codigo,
                                              @RequestBody QualidadeUpdateDTO dto) {
        return loteService.atualizarQualidade(codigo, dto);
    }

    @GetMapping("/{codigo}/rastreabilidade")
    public RastreabilidadeResponseDTO rastreabilidade(@PathVariable String codigo) {
        return loteService.rastreabilidade(codigo);
    }
}
