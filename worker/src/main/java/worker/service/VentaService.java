package worker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import worker.dto.VentaRequestDTO;
import worker.dto.VentaResponseDTO;
import worker.model.Calculos;
import worker.model.ClienteInfo;
import worker.model.EstadoVenta;
import worker.model.HistorialCambio;
import worker.model.InformacionVenta;
import worker.model.UsuarioInfo;
import worker.model.Ventas;
import worker.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ObjectMapper objectMapper;

    // ===========================
    // CREAR VENTA
    // ===========================
    @Transactional
    public VentaResponseDTO crearVenta(VentaRequestDTO request) {
        Ventas venta = Ventas.builder()
                .cliente(mapToClienteInfo(request.getCliente()))
                .usuario(mapToUsuarioInfo(request.getUsuario()))
                .informacionVenta(mapToInformacionVenta(request.getInformacionVenta()))
                .calculos(mapToCalculos(request.getCalculos()))
                .estado(safeEstado(request.getEstado(), EstadoVenta.BORRADOR))
                .build();

        Ventas ventaGuardada = ventaRepository.save(venta);
        return mapToResponseDTO(ventaGuardada);
    }

    // ===========================
    // OBTENER POR ID (con historial)
    // ===========================
    @Transactional(readOnly = true)
    public VentaResponseDTO obtenerVentaPorId(Long id) {
        Ventas venta = ventaRepository.findByIdWithHistorial(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + id));
        return mapToResponseDTO(venta);
    }

    // ===========================
    // LISTAR TODAS
    // ===========================
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerTodasLasVentas() {
        return ventaRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ===========================
    // FILTRAR POR ESTADO (enum)
    // ===========================
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerVentasPorEstado(String estado) {
        EstadoVenta ev = safeEstado(estado, null);
        return ventaRepository.findByEstado(ev).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ===========================
    // FILTRAR POR CLIENTE (embeddable)
    // ===========================
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerVentasPorCliente(Long clienteId) {
        return ventaRepository.findByClienteClienteId(clienteId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ===========================
    // ACTUALIZAR VENTA
    // ===========================
    @Transactional
    public VentaResponseDTO actualizarVenta(Long id, VentaRequestDTO request, String usuarioModifico) {
        Ventas venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + id));

        // Estado anterior (snapshot)
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
            venta.setEstado(safeEstado(request.getEstado(), venta.getEstado()));
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

        Ventas ventaActualizada = ventaRepository.save(venta);
        return mapToResponseDTO(ventaActualizada);
    }

    // ===========================
    // ELIMINAR VENTA
    // ===========================
    @Transactional
    public void eliminarVenta(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada con id: " + id);
        }
        ventaRepository.deleteById(id);
    }

    // ===========================
    // CAMBIAR ESTADO
    // ===========================
    @Transactional
    public VentaResponseDTO cambiarEstado(Long id, String nuevoEstado, String usuarioModifico) {
        Ventas venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + id));

        String estadoAnteriorStr = venta.getEstado() != null ? venta.getEstado().name() : "NULO";
        JsonNode estadoAnterior = objectMapper.valueToTree(venta);

        EstadoVenta evNuevo = safeEstado(nuevoEstado, venta.getEstado());
        venta.setEstado(evNuevo);

        JsonNode estadoActual = objectMapper.valueToTree(venta);

        HistorialCambio historial = HistorialCambio.builder()
                .fechaModificacion(LocalDateTime.now())
                .usuarioModifico(usuarioModifico)
                .cambios(String.format("Cambio de estado de %s a %s", estadoAnteriorStr, evNuevo.name()))
                .estadoAnterior(estadoAnterior)
                .estadoActual(estadoActual)
                .build();

        venta.agregarHistorial(historial);

        Ventas ventaActualizada = ventaRepository.save(venta);
        return mapToResponseDTO(ventaActualizada);
    }

    // ===========================
    // AUX: convertir String -> EstadoVenta con default
    // ===========================
    private EstadoVenta safeEstado(String raw, EstadoVenta defecto) {
        if (raw == null) return defecto;
        try {
            return EstadoVenta.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Estado inválido: " + raw);
        }
    }

    // ===========================
    // MAPEOS REQUEST -> ENTIDAD
    // ===========================
    private ClienteInfo mapToClienteInfo(VentaRequestDTO.ClienteDTO dto) {
        if (dto == null) return null;
        return ClienteInfo.builder()
                .clienteId(dto.getClienteId())
                .nombre(dto.getNombre())
                .docId(dto.getDocId())
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

    private InformacionVenta mapToInformacionVenta(VentaRequestDTO.InformacionVentaDTO dto) {
        if (dto == null) return null;
        return InformacionVenta.builder()
                .numero(dto.getNumero())
                .fecha(dto.getFecha())
                .metodoPago(dto.getMetodoPago())
                .tipo(dto.getTipo())
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

    // ===========================
    // MAPEOS ENTIDAD -> RESPONSE
    // ===========================
    private VentaResponseDTO mapToResponseDTO(Ventas venta) {
        var historialList = venta.getHistorial() == null ? List.<HistorialCambio>of() : venta.getHistorial();

        return VentaResponseDTO.builder()
                .id(venta.getId())
                .cliente(mapToClienteDTO(venta.getCliente()))
                .usuario(mapToUsuarioDTO(venta.getUsuario()))
                .informacionVenta(mapToInformacionVentaDTO(venta.getInformacionVenta()))
                .calculos(mapToCalculosDTO(venta.getCalculos()))
                .estado(venta.getEstado() != null ? venta.getEstado().name() : null)
                .fechaCreacion(venta.getFechaCreacion())
                .fechaActualizacion(venta.getFechaActualizacion())
                .historial(historialList.stream()
                        .map(this::mapToHistorialDTO)
                        .collect(Collectors.toList()))
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

    private VentaResponseDTO.UsuarioDTO mapToUsuarioDTO(UsuarioInfo usuario) {
        if (usuario == null) return null;
        return VentaResponseDTO.UsuarioDTO.builder()
                .usuarioId(usuario.getUsuarioId())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
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
}
