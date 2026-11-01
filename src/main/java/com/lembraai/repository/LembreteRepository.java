package com.lembraai.repository;

import com.lembraai.model.Lembrete;
import com.lembraai.model.enums.StatusLembrete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LembreteRepository extends JpaRepository<Lembrete, Long> {

    List<Lembrete> findByMedicamentoPacienteId(Long pacienteId);

    List<Lembrete> findByMedicamentoPacienteIdAndStatus(Long pacienteId, StatusLembrete status);

    List<Lembrete> findByStatusAndHorarioPrevistoBefore(StatusLembrete status, LocalDateTime limite);

    List<Lembrete> findByMedicamentoPacienteIdAndHorarioPrevistoBetween(
            Long pacienteId, LocalDateTime inicio, LocalDateTime fim);

    boolean existsByMedicamentoIdAndHorarioPrevisto(Long medicamentoId, LocalDateTime horarioPrevisto);

    void deleteByMedicamentoId(Long medicamentoId);

    void deleteByMedicamentoIdAndStatus(Long medicamentoId, StatusLembrete status);
}