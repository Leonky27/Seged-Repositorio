package worker.model;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_cambios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class HistorialCambio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @Column(name = "fecha_modificacion", nullable = false)
    private LocalDateTime fechaModificacion;

    @Column(name = "usuario_modifico", length = 255)
    private String usuarioModifico;

    @Column(columnDefinition = "TEXT")
    private String cambios;

    @Column(name = "estado_anterior", columnDefinition = "JSON")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode estadoAnterior;

    @Column(name = "estado_actual", columnDefinition = "JSON")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode estadoActual;

    @PrePersist
    protected void onCreate() {
        if (fechaModificacion == null) {
            fechaModificacion = LocalDateTime.now();
        }
    }
}
