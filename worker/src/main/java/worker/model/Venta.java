package worker.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "clienteId", column = @Column(name = "cliente_id")),
            @AttributeOverride(name = "nombre", column = @Column(name = "cliente_nombre")),
            @AttributeOverride(name = "docId", column = @Column(name = "cliente_doc_id"))
    })
    private ClienteInfo cliente;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "usuarioId", column = @Column(name = "usuario_id")),
            @AttributeOverride(name = "nombre", column = @Column(name = "usuario_nombre")),
            @AttributeOverride(name = "rol", column = @Column(name = "usuario_rol"))
    })
    private UsuarioInfo usuario;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "numero", column = @Column(name = "venta_numero")),
            @AttributeOverride(name = "fecha", column = @Column(name = "venta_fecha")),
            @AttributeOverride(name = "metodoPago", column = @Column(name = "metodo_pago")),
            @AttributeOverride(name = "tipo", column = @Column(name = "venta_tipo"))
    })
    private InformacionVenta informacionVenta;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "subtotal", column = @Column(name = "subtotal", precision = 19, scale = 2)),
            @AttributeOverride(name = "impuestos", column = @Column(name = "impuestos", precision = 19, scale = 2)),
            @AttributeOverride(name = "total", column = @Column(name = "total", precision = 19, scale = 2))
    })
    private Calculos calculos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVenta estado;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HistorialCambio> historial = new ArrayList<>();

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetalleVenta> detalles = new ArrayList<>();

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (estado == null) {
            estado = EstadoVenta.BORRADOR;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    public void agregarHistorial(HistorialCambio cambio) {
        if (cambio != null) {
            historial.add(cambio);
            cambio.setVenta(this);
        }
    }

    public void agregarDetalle(DetalleVenta detalle) {
        if (detalle != null) {
            detalles.add(detalle);
            detalle.setVenta(this);
        }
    }


    public void eliminarDetalle(DetalleVenta detalle) {
        if (detalle != null) {
            detalles.remove(detalle);
            detalle.setVenta(null);
        }
    }
}
