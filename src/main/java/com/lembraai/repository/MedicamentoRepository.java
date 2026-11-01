package com.lembraai.repository;

import com.lembraai.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    List<Medicamento> findByPacienteId(Long pacienteId);
    List<Medicamento> findByAtivoTrue();
}