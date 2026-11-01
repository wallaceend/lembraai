package com.lembraai.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "medicamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 50)
    private String dosagem;

    // de quantas em quantas horas deve tomar (ex.: 8 = a cada 8h)
    @Column(nullable = false)
    private Integer frequenciaHoras;

    // horário da primeira dose do dia
    @Column(nullable = false)
    private LocalTime horarioInicial;

    @Column(nullable = false)
    private LocalDate dataInicio;

    private LocalDate dataFim;

    @Column(length = 255)
    private String observacoes;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id")
    private Usuario paciente;
}