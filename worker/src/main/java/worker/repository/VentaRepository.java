package worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import worker.model.EstadoVenta;
import worker.model.Ventas;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Ventas, Long> {

    // ---- Búsquedas por campos embebidos (derived queries)
    Optional<Ventas> findByInformacionVentaNumero(String numero);

    // Estado es un enum, no String
    List<Ventas> findByEstado(EstadoVenta estado);

    // ClienteInfo.clienteId (Embeddable)
    List<Ventas> findByClienteClienteId(Long clienteId);

    // UsuarioInfo.usuarioId (Embeddable)
    List<Ventas> findByUsuarioUsuarioId(Long usuarioId);

    // Rango de fechas (InformacionVenta.fecha)
    List<Ventas> findByInformacionVentaFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Método de pago (InformacionVenta.metodoPago)
    List<Ventas> findByInformacionVentaMetodoPago(String metodoPago);

    // ---- Traer historial con fetch join (usar el nombre de la entidad: Ventas)
    @Query("select v from Ventas v left join fetch v.historial where v.id = :id")
    Optional<Ventas> findByIdWithHistorial(@Param("id") Long id);
}
