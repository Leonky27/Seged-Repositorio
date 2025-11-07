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
public class UsuarioInfo {
    private Long usuarioId;

    @Column(length = 255)
    private String nombre;

    @Column(length = 50)
    private String rol;
}
