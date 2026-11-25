package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroOrdemProducao;
<<<<<<< HEAD
import com.valeviva.financeiro.service.OrdemProducaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
=======
import com.valeviva.financeiro.dto.ListaOrdemProducao;
import com.valeviva.financeiro.model.OrdemProducao;
import com.valeviva.financeiro.model.Produto;
import com.valeviva.financeiro.repository.OrdemProducaoRepository;
import com.valeviva.financeiro.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
import java.util.List;

@RestController
@RequestMapping("/ordens-producao")
public class OrdemProducaoController {

<<<<<<< HEAD
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
=======
    @Autowired
    private OrdemProducaoRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> criar(@RequestBody @Valid CadastroOrdemProducao dto) {
        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElse(null);

        if (produto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        OrdemProducao op = new OrdemProducao(produto, dto.quantidade());
        op.setPrazo(dto.prazo());
        op.setPrioridade(dto.prioridade());
        OrdemProducao novaOp = repository.save(op);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ListaOrdemProducao(novaOp));
    }

    @GetMapping
    public ResponseEntity<List<ListaOrdemProducao>> listar() {
        List<ListaOrdemProducao> lista = repository.findAll().stream().map(ListaOrdemProducao::new).toList();
        return ResponseEntity.ok(lista);
    }
}
>>>>>>> 7bb94aec591c99e543c810d90cde183cb02baceb
