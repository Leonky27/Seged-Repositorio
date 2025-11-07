package worker.dto;


import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaRequestDTO {

    private ClienteDTO cliente;
    private UsuarioDTO usuario;
    private InformacionVentaDTO informacionVenta;
    private CalculosDTO calculos;
    private String estado;

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
}