package com.lembraai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "vinculos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"cuidador_id", "paciente_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vinculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cuidador_id")
    private Usuario cuidador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id")
    private Usuario paciente;
}