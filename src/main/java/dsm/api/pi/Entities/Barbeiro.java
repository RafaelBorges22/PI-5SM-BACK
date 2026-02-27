package dsm.api.pi.Entities;

import dsm.api.pi.Enum.StatusPagamento;
import dsm.api.pi.Enum.Unidade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_app_barbeiro")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unidade unidade;
}