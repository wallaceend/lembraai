package com.lembraai.repository;

import com.lembraai.model.Vinculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VinculoRepository extends JpaRepository<Vinculo, Long> {
    List<Vinculo> findByCuidadorId(Long cuidadorId);
    List<Vinculo> findByPacienteId(Long pacienteId);
    boolean existsByCuidadorIdAndPacienteId(Long cuidadorId, Long pacienteId);
}