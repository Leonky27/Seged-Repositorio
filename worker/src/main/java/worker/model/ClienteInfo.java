package worker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteInfo {
    private Long clienteId;

    @Column(length = 255)
    private String nombre;

    @Column(length = 100)
    private String docId;
}
