package worker.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaResponseDTO {

    private Long id;
    private ClienteDTO cliente;
    private UsuarioDTO usuario;
    private InformacionVentaDTO informacionVenta;
    private CalculosDTO calculos;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<HistorialDTO> historial;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClienteDTO {
        private Long clienteId;
        private String nombre;
        private String docId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UsuarioDTO {
        private Long usuarioId;
        private String nombre;
        private String rol;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InformacionVentaDTO {
        private String numero;
        private LocalDateTime fecha;
        private String metodoPago;
        private String tipo;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CalculosDTO {
        private BigDecimal subtotal;
        private BigDecimal impuestos;
        private BigDecimal total;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HistorialDTO {
        private Long id;
        private LocalDateTime fechaModificacion;
        private String usuarioModifico;
        private String cambios;
        private Object estadoAnterior;
        private Object estadoActual;
    }
}