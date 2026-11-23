package com.valeviva.financeiro.controllers;

import com.valeviva.financeiro.dto.CadastroNotificacao;
import com.valeviva.financeiro.model.Notificacao;
import com.valeviva.financeiro.repository.NotificacaoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<Notificacao> criar(@RequestBody @Valid CadastroNotificacao dados) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTitulo(dados.titulo());
        notificacao.setMensagem(dados.mensagem());
        notificacao.setDataEnvio(LocalDateTime.now());
        notificacao.setLida(false);
        Notificacao novaNotificacao = repository.save(notificacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaNotificacao);
    }

    @GetMapping
    public ResponseEntity<List<Notificacao>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PutMapping("/{id}/ler")
    @Transactional
    public ResponseEntity<?> marcarComoLida(@PathVariable Long id) {
        Optional<Notificacao> optionalNotificacao = repository.findById(id);

        if (optionalNotificacao.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notificação não encontrada.");
        }

        Notificacao notificacao = optionalNotificacao.get();
        notificacao.setLida(true);
        repository.save(notificacao);

        return ResponseEntity.ok(notificacao);
    }
}
