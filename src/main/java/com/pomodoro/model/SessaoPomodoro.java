package com.pomodoro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "sessoes_pomodoro")
@Data                   
@Builder               
@NoArgsConstructor      
@AllArgsConstructor     
public class SessaoPomodoro {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(name = "tempo_minutos", nullable = false)
    private Integer tempoMinutos;

    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSessao status;

    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

   
    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    
    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    
    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
        if (this.status == null) {
            this.status = StatusSessao.EM_ANDAMENTO;
        }
    }
}
