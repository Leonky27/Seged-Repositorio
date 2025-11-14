package worker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import worker.dto.VentaRequestDTO;
import worker.dto.VentaResponseDTO;
import worker.service.VentaService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    public ResponseEntity<VentaResponseDTO> crearVenta(@RequestBody VentaRequestDTO request) {
        VentaResponseDTO venta = ventaService.crearVenta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(venta);
    }
    @DeleteMapping("/numero/{numero}")
    public ResponseEntity<Void> eliminarVentaPorNumero(@PathVariable String numero) {
        ventaService.eliminarVentaPorNumero(numero);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> obtenerVenta(@PathVariable Long id) {
        VentaResponseDTO venta = ventaService.obtenerVentaPorId(id);
        return ResponseEntity.ok(venta);
    }

    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> obtenerTodasLasVentas() {
        List<VentaResponseDTO> ventas = ventaService.obtenerTodasLasVentas();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<VentaResponseDTO>> obtenerVentasPorEstado(@PathVariable String estado) {
        List<VentaResponseDTO> ventas = ventaService.obtenerVentasPorEstado(estado);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VentaResponseDTO>> obtenerVentasPorCliente(@PathVariable Long clienteId) {
        List<VentaResponseDTO> ventas = ventaService.obtenerVentasPorCliente(clienteId);
        return ResponseEntity.ok(ventas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> actualizarVenta(
            @PathVariable Long id,
            @RequestBody VentaRequestDTO request,
            @RequestParam(defaultValue = "sistema") String usuarioModifico) {
        VentaResponseDTO venta = ventaService.actualizarVenta(id, request, usuarioModifico);
        return ResponseEntity.ok(venta);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<VentaResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @RequestParam(defaultValue = "sistema") String usuarioModifico) {
        String nuevoEstado = body.get("estado");
        VentaResponseDTO venta = ventaService.cambiarEstado(id, nuevoEstado, usuarioModifico);
        return ResponseEntity.ok(venta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
        return ResponseEntity.noContent().build();
    }
}
