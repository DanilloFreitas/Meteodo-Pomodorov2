package com.pomodoro.service;

import com.pomodoro.dto.PomodoroDTO;
import com.pomodoro.exception.PomodoroException;
import com.pomodoro.model.SessaoPomodoro;
import com.pomodoro.model.StatusSessao;
import com.pomodoro.repository.SessaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PomodoroService {

    private static final int TEMPO_PADRAO_MINUTOS = 25;

    private final SessaoRepository sessaoRepository;

    
    @Transactional
    public PomodoroDTO.SessaoResponse iniciarSessao(PomodoroDTO.IniciarRequest request) {

        
        sessaoRepository
                .findFirstByStatusOrderByDataCriacaoDesc(StatusSessao.EM_ANDAMENTO)
                .ifPresent(s -> { throw new PomodoroException.SessaoJaAtiva(); });

        
        int tempo = (request.getTempoMinutos() != null)
                ? request.getTempoMinutos()
                : TEMPO_PADRAO_MINUTOS;

     
        SessaoPomodoro sessao = SessaoPomodoro.builder()
                .tempoMinutos(tempo)
                .status(StatusSessao.EM_ANDAMENTO)
                .dataInicio(LocalDateTime.now())
                .build();
        

        SessaoPomodoro salva = sessaoRepository.save(sessao);
        log.info("Sessão Pomodoro iniciada: id={}, tempo={}min", salva.getId(), tempo);

        return toResponse(salva, "Sessão iniciada! Foco total por " + tempo + " minutos.");
    }

    
    @Transactional
    public PomodoroDTO.SessaoResponse consultarStatus() {

        SessaoPomodoro sessao = sessaoRepository
                .findFirstByStatusOrderByDataCriacaoDesc(StatusSessao.EM_ANDAMENTO)
                .orElseThrow(PomodoroException.SessaoNaoEncontrada::new);

        long segundosDecorridos = ChronoUnit.SECONDS.between(
                sessao.getDataInicio(), LocalDateTime.now());
        long totalSegundos      = (long) sessao.getTempoMinutos() * 60;
        long segundosRestantes  = Math.max(0, totalSegundos - segundosDecorridos);

        
        if (segundosRestantes == 0) {
            sessao.setStatus(StatusSessao.CONCLUIDO);
            sessao.setDataFim(LocalDateTime.now());
            sessaoRepository.save(sessao);
            log.info("Sessão id={} concluída automaticamente.", sessao.getId());
            return toResponse(sessao, segundosRestantes, totalSegundos,
                    "🎉 Pomodoro concluído! Hora de uma pausa.");
        }

        return toResponse(sessao, segundosRestantes, totalSegundos,
                "Em andamento — " + formatarTempo(segundosRestantes) + " restantes.");
    }

    
    @Transactional
    public PomodoroDTO.SessaoResponse pararSessao() {

        SessaoPomodoro sessao = sessaoRepository
                .findFirstByStatusOrderByDataCriacaoDesc(StatusSessao.EM_ANDAMENTO)
                .orElseThrow(PomodoroException.SessaoNaoEncontrada::new);

        sessao.setStatus(StatusSessao.CANCELADO);
        sessao.setDataFim(LocalDateTime.now());
        SessaoPomodoro salva = sessaoRepository.save(sessao);

        log.info("Sessão id={} cancelada pelo usuário.", salva.getId());
        return toResponse(salva, 0L, (long) salva.getTempoMinutos() * 60,
                "Sessão encerrada antes do tempo.");
    }

    
    @Transactional(readOnly = true)
    public List<PomodoroDTO.SessaoResponse> historico() {
        return sessaoRepository
                .findAllByStatusOrderByDataCriacaoDesc(StatusSessao.CONCLUIDO)
                .stream()
                .map(s -> toResponse(s, 0L, (long) s.getTempoMinutos() * 60, null))
                .collect(Collectors.toList());
    }

    
    @Transactional(readOnly = true)
    public PomodoroDTO.ResumoResponse resumo() {
        long concluidas = sessaoRepository.countByStatus(StatusSessao.CONCLUIDO);
        long canceladas = sessaoRepository.countByStatus(StatusSessao.CANCELADO);
        long total      = sessaoRepository.count();

        
        long minutosFocados = sessaoRepository
                .findAllByStatusOrderByDataCriacaoDesc(StatusSessao.CONCLUIDO)
                .stream()
                .mapToLong(SessaoPomodoro::getTempoMinutos)
                .sum();

        return PomodoroDTO.ResumoResponse.builder()
                .totalSessoes(total)
                .sessoesConcluidaas(concluidas)
                .sessoesCanceladas(canceladas)
                .totalMinutosFocados(minutosFocados)
                .build();
    }

    
    private PomodoroDTO.SessaoResponse toResponse(SessaoPomodoro s, String mensagem) {
        long total     = (long) s.getTempoMinutos() * 60;
        long restantes = s.getDataInicio() != null
                ? Math.max(0, total - ChronoUnit.SECONDS.between(s.getDataInicio(), LocalDateTime.now()))
                : total;
        return toResponse(s, restantes, total, mensagem);
    }

    private PomodoroDTO.SessaoResponse toResponse(SessaoPomodoro s,
                                                   long segundosRestantes,
                                                   long totalSegundos,
                                                   String mensagem) {
        long decorridos     = totalSegundos - segundosRestantes;
        int  percentual     = totalSegundos > 0
                ? (int) Math.min(100, (decorridos * 100) / totalSegundos)
                : 0;

        return PomodoroDTO.SessaoResponse.builder()
                .id(s.getId())
                .tempoMinutos(s.getTempoMinutos())
                .status(s.getStatus())
                .dataCriacao(s.getDataCriacao())
                .dataInicio(s.getDataInicio())
                .dataFim(s.getDataFim())
                .segundosRestantes(segundosRestantes)
                .percentualConcluido(percentual)
                .mensagem(mensagem)
                .build();
    }

    
    private String formatarTempo(long segundos) {
        return String.format("%02d:%02d", segundos / 60, segundos % 60);
    }
}
