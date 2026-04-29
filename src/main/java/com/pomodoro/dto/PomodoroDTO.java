package com.pomodoro.dto;

import com.pomodoro.model.StatusSessao;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


public class PomodoroDTO {

    // iniciar sessão

   
    @Data
    public static class IniciarRequest {

        @Min(value = 1,  message = "Tempo mínimo: 1 minuto")
        @Max(value = 60, message = "Tempo máximo: 60 minutos")
        private Integer tempoMinutos = 25; // padrão Pomodoro
    }

    // estado da sessão 

    
    @Data
    @Builder
    public static class SessaoResponse {
        private Long          id;
        private Integer       tempoMinutos;
        private StatusSessao  status;
        private LocalDateTime dataCriacao;
        private LocalDateTime dataInicio;
        private LocalDateTime dataFim;

        
        private long segundosRestantes;

        
        private int percentualConcluido;

        
        private String mensagem;
    }

    
    @Data
    @Builder
    public static class ResumoResponse {
        private long totalSessoes;
        private long sessoesConcluidaas;
        private long sessoesCanceladas;
        private long totalMinutosFocados;
    }
}
