package com.pomodoro.controller;

import com.pomodoro.dto.PomodoroDTO;
import com.pomodoro.service.PomodoroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/pomodoro")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permite acesso de qualquer origem (ajuste em produção)
public class PomodoroController {

    private final PomodoroService pomodoroService;

    
    @PostMapping("/iniciar")
    public ResponseEntity<PomodoroDTO.SessaoResponse> iniciar(
            @Valid @RequestBody(required = false) PomodoroDTO.IniciarRequest request) {

        // Se o body for omitido, usa objeto com valores padrão
        if (request == null) request = new PomodoroDTO.IniciarRequest();

        PomodoroDTO.SessaoResponse response = pomodoroService.iniciarSessao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

   
    @GetMapping("/status")
    public ResponseEntity<PomodoroDTO.SessaoResponse> status() {
        return ResponseEntity.ok(pomodoroService.consultarStatus());
    }

   
    @PutMapping("/parar")
    public ResponseEntity<PomodoroDTO.SessaoResponse> parar() {
        return ResponseEntity.ok(pomodoroService.pararSessao());
    }

    
    @GetMapping("/historico")
    public ResponseEntity<List<PomodoroDTO.SessaoResponse>> historico() {
        return ResponseEntity.ok(pomodoroService.historico());
    }

    
    @GetMapping("/resumo")
    public ResponseEntity<PomodoroDTO.ResumoResponse> resumo() {
        return ResponseEntity.ok(pomodoroService.resumo());
    }
}
