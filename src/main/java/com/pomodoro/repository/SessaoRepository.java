package com.pomodoro.repository;

import com.pomodoro.model.SessaoPomodoro;
import com.pomodoro.model.StatusSessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SessaoRepository extends JpaRepository<SessaoPomodoro, Long> {

    
    Optional<SessaoPomodoro> findFirstByStatusOrderByDataCriacaoDesc(StatusSessao status);

    List<SessaoPomodoro> findAllByStatusOrderByDataCriacaoDesc(StatusSessao status);

    
    long countByStatus(StatusSessao status);
}
