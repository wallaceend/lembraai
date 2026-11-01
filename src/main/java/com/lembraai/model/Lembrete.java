package com.lembraai.model;

import com.lembraai.model.enums.StatusLembrete;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lembretes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lembrete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medicamento_id")
    private Medicamento medicamento;

    @Column(nullable = false)
    private LocalDateTime horarioPrevisto;

    // preenchido quando o paciente confirma "Tomei"
    private LocalDateTime horarioConfirmacao;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusLembrete status = StatusLembrete.PENDENTE;
}