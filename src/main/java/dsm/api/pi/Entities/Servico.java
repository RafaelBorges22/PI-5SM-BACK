package dsm.api.pi.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import dsm.api.pi.Enum.MetodoPagamento;
import dsm.api.pi.Enum.StatusPagamento;
import dsm.api.pi.Enum.Unidade;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_app_servicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate dataServico;

    private String produto;

    private String servico;

    @Column(name = "nome_cliente")
    private String nomeCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barbeiro_id", nullable = false)
    @JsonBackReference
    private Barbeiro barbeiro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento statusPagamento;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    @Column(name = "pix_txid")
    private String pixTxid;

    @Column(name = "pix_qrcode", columnDefinition = "TEXT")
    private String pixQrCode;

    @Column(name = "pix_imagem_qrcode", columnDefinition = "TEXT")
    private String pixImagemQrCode;

    @Column(name = "pix_loc_id")
    private String pixLocId;
}