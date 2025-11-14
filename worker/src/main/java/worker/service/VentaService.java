package worker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import worker.dto.DetalleVentaDTO;
import worker.dto.VentaRequestDTO;
import worker.dto.VentaResponseDTO;
import worker.model.*;
import worker.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public VentaResponseDTO crearVenta(VentaRequestDTO request) {
        Venta venta = Venta.builder()
                .cliente(mapToClienteInfo(request.getCliente()))
                .usuario(mapToUsuarioInfo(request.getUsuario()))
                .informacionVenta(mapToInformacionVenta(request.getInformacionVenta()))
                .calculos(mapToCalculos(request.getCalculos()))
                .estado(EstadoVenta.valueOf(request.getEstado() != null ?
                        request.getEstado().toUpperCase() : "BORRADOR"))
                .build();

        if (request.getDetalles() != null && !request.getDetalles().isEmpty()) {
            for (DetalleVentaDTO detalleDTO : request.getDetalles()) {
                DetalleVenta detalle = mapToDetalleVenta(detalleDTO);
                venta.agregarDetalle(detalle);
            }
        }

        Venta ventaGuardada = ventaRepository.save(venta);
        return mapToResponseDTO(ventaGuardada);
    }

    @Transactional(readOnly = true)
    public VentaResponseDTO obtenerVentaPorId(Long id) {
        Venta venta = ventaRepository.findByIdWithHistorial(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + id));
        return mapToResponseDTO(venta);
    }
    @Transactional
    public void eliminarVentaPorNumero(String numero) {
        Venta venta = ventaRepository.findByInformacionVentaNumero(numero)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con numero: " + numero));
        ventaRepository.delete(venta);
    }


    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerTodasLasVentas() {
        return ventaRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerVentasPorEstado(String estado) {
        return ventaRepository.findByEstado(estado.toUpperCase()).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerVentasPorCliente(Long clienteId) {
        return ventaRepository.findByClienteId(clienteId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VentaResponseDTO actualizarVenta(Long id, VentaRequestDTO request, String usuarioModifico) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + id));

        JsonNode estadoAnterior = objectMapper.valueToTree(venta);

        if (request.getCliente() != null) {
            venta.setCliente(mapToClienteInfo(request.getCliente()));
        }
        if (request.getUsuario() != null) {
            venta.setUsuario(mapToUsuarioInfo(request.getUsuario()));
        }
        if (request.getInformacionVenta() != null) {
            venta.setInformacionVenta(mapToInformacionVenta(request.getInformacionVenta()));
        }
        if (request.getCalculos() != null) {
            venta.setCalculos(mapToCalculos(request.getCalculos()));
        }
        if (request.getEstado() != null) {
            venta.setEstado(EstadoVenta.valueOf(request.getEstado().toUpperCase()));
        }

        if (request.getDetalles() != null) {
            venta.getDetalles().clear();

            for (DetalleVentaDTO detalleDTO : request.getDetalles()) {
                DetalleVenta detalle = mapToDetalleVenta(detalleDTO);
                venta.agregarDetalle(detalle);
            }
        }

        JsonNode estadoActual = objectMapper.valueToTree(venta);

        HistorialCambio historial = HistorialCambio.builder()
                .fechaModificacion(LocalDateTime.now())
                .usuarioModifico(usuarioModifico)
                .cambios("Actualización de venta")
                .estadoAnterior(estadoAnterior)
                .estadoActual(estadoActual)
                .build();

        venta.agregarHistorial(historial);

        Venta ventaActualizada = ventaRepository.save(venta);
        return mapToResponseDTO(ventaActualizada);
    }

    @Transactional
    public void eliminarVenta(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada con id: " + id);
        }
        ventaRepository.deleteById(id);
    }

    @Transactional
    public VentaResponseDTO cambiarEstado(Long id, String nuevoEstado, String usuarioModifico) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + id));

        String estadoAnteriorStr = venta.getEstado().name();
        JsonNode estadoAnterior = objectMapper.valueToTree(venta);

        venta.setEstado(EstadoVenta.valueOf(nuevoEstado.toUpperCase()));

        JsonNode estadoActual = objectMapper.valueToTree(venta);

        HistorialCambio historial = HistorialCambio.builder()
                .fechaModificacion(LocalDateTime.now())
                .usuarioModifico(usuarioModifico)
                .cambios(String.format("Cambio de estado de %s a %s", estadoAnteriorStr, nuevoEstado))
                .estadoAnterior(estadoAnterior)
                .estadoActual(estadoActual)
                .build();

        venta.agregarHistorial(historial);

        Venta ventaActualizada = ventaRepository.save(venta);
        return mapToResponseDTO(ventaActualizada);
    }


    private ClienteInfo mapToClienteInfo(VentaRequestDTO.ClienteDTO dto) {
        if (dto == null) return null;
        return ClienteInfo.builder()
                .clienteId(dto.getClienteId())
                .nombre(dto.getNombre())
                .docId(dto.getDocId())
                .build();
    }

    private VentaResponseDTO.ClienteDTO mapToClienteDTO(ClienteInfo cliente) {
        if (cliente == null) return null;
        return VentaResponseDTO.ClienteDTO.builder()
                .clienteId(cliente.getClienteId())
                .nombre(cliente.getNombre())
                .docId(cliente.getDocId())
                .build();
    }

    private UsuarioInfo mapToUsuarioInfo(VentaRequestDTO.UsuarioDTO dto) {
        if (dto == null) return null;
        return UsuarioInfo.builder()
                .usuarioId(dto.getUsuarioId())
                .nombre(dto.getNombre())
                .rol(dto.getRol())
                .build();
    }

    private VentaResponseDTO.UsuarioDTO mapToUsuarioDTO(UsuarioInfo usuario) {
        if (usuario == null) return null;
        return VentaResponseDTO.UsuarioDTO.builder()
                .usuarioId(usuario.getUsuarioId())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
                .build();
    }

    private InformacionVenta mapToInformacionVenta(VentaRequestDTO.InformacionVentaDTO dto) {
        if (dto == null) return null;
        return InformacionVenta.builder()
                .numero(dto.getNumero())
                .fecha(dto.getFecha())
                .metodoPago(dto.getMetodoPago())
                .tipo(dto.getTipo())
                .build();
    }

    private VentaResponseDTO.InformacionVentaDTO mapToInformacionVentaDTO(InformacionVenta info) {
        if (info == null) return null;
        return VentaResponseDTO.InformacionVentaDTO.builder()
                .numero(info.getNumero())
                .fecha(info.getFecha())
                .metodoPago(info.getMetodoPago())
                .tipo(info.getTipo())
                .build();
    }

    private Calculos mapToCalculos(VentaRequestDTO.CalculosDTO dto) {
        if (dto == null) return null;
        return Calculos.builder()
                .subtotal(dto.getSubtotal())
                .impuestos(dto.getImpuestos())
                .total(dto.getTotal())
                .build();
    }

    private VentaResponseDTO.CalculosDTO mapToCalculosDTO(Calculos calculos) {
        if (calculos == null) return null;
        return VentaResponseDTO.CalculosDTO.builder()
                .subtotal(calculos.getSubtotal())
                .impuestos(calculos.getImpuestos())
                .total(calculos.getTotal())
                .build();
    }

    private VentaResponseDTO.HistorialDTO mapToHistorialDTO(HistorialCambio historial) {
        if (historial == null) return null;
        return VentaResponseDTO.HistorialDTO.builder()
                .id(historial.getId())
                .fechaModificacion(historial.getFechaModificacion())
                .usuarioModifico(historial.getUsuarioModifico())
                .cambios(historial.getCambios())
                .estadoAnterior(historial.getEstadoAnterior())
                .estadoActual(historial.getEstadoActual())
                .build();
    }

    private DetalleVenta mapToDetalleVenta(DetalleVentaDTO dto) {
        if (dto == null) return null;
        return DetalleVenta.builder()
                .productoId(dto.getProductoId())
                .nombreProducto(dto.getNombreProducto())
                .cantidad(dto.getCantidad())
                .precioUnitario(dto.getPrecioUnitario())
                .descuento(dto.getDescuento() != null ? dto.getDescuento() : BigDecimal.ZERO)
                .build();
    }

    private DetalleVentaDTO mapToDetalleVentaDTO(DetalleVenta detalle) {
        if (detalle == null) return null;
        return DetalleVentaDTO.builder()
                .id(detalle.getId())
                .productoId(detalle.getProductoId())
                .nombreProducto(detalle.getNombreProducto())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .descuento(detalle.getDescuento())
                .subtotal(detalle.getSubtotal())
                .build();
    }

    private VentaResponseDTO mapToResponseDTO(Venta venta) {
        return VentaResponseDTO.builder()
                .id(venta.getId())
                .cliente(mapToClienteDTO(venta.getCliente()))
                .usuario(mapToUsuarioDTO(venta.getUsuario()))
                .informacionVenta(mapToInformacionVentaDTO(venta.getInformacionVenta()))
                .calculos(mapToCalculosDTO(venta.getCalculos()))
                .estado(venta.getEstado().name())
                .fechaCreacion(venta.getFechaCreacion())
                .fechaActualizacion(venta.getFechaActualizacion())
                .historial(venta.getHistorial().stream()
                        .map(this::mapToHistorialDTO)
                        .collect(Collectors.toList()))
                .detalles(venta.getDetalles().stream()
                        .map(this::mapToDetalleVentaDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}
