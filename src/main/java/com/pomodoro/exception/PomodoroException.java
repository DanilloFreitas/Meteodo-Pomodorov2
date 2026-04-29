package com.pomodoro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class PomodoroException {

  
    @ResponseStatus(HttpStatus.CONFLICT)
    public static class SessaoJaAtiva extends RuntimeException {
        public SessaoJaAtiva() {
            super("Já existe uma sessão Pomodoro em andamento. " +
                  "Encerre-a antes de iniciar uma nova.");
        }
    }

   
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class SessaoNaoEncontrada extends RuntimeException {
        public SessaoNaoEncontrada() {
            super("Nenhuma sessão Pomodoro ativa encontrada.");
        }
        public SessaoNaoEncontrada(Long id) {
            super("Sessão com id=" + id + " não encontrada.");
        }
    }

    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class OperacaoInvalida extends RuntimeException {
        public OperacaoInvalida(String detalhe) {
            super("Operação inválida: " + detalhe);
        }
    }
}
