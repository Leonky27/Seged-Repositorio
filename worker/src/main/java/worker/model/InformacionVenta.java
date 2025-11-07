package worker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacionVenta {
    @Column(unique = true, length = 50)
    private String numero;

    private LocalDateTime fecha;

    @Column(length = 50)
    private String metodoPago;

    @Column(length = 50)
    private String tipo;
}
