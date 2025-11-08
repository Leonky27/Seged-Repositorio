package worker.repository;

import worker.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    Optional<Venta> findByInformacionVentaNumero(String numero);

    @Query("SELECT v FROM Venta v WHERE CAST(v.estado AS string) = :estado")
    List<Venta> findByEstado(@Param("estado") String estado);

    @Query("SELECT v FROM Venta v WHERE v.cliente.clienteId = :clienteId")
    List<Venta> findByClienteId(@Param("clienteId") Long clienteId);

    @Query("SELECT v FROM Venta v WHERE v.usuario.usuarioId = :usuarioId")
    List<Venta> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT v FROM Venta v WHERE v.informacionVenta.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Venta> findByFechaRange(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("SELECT v FROM Venta v WHERE v.informacionVenta.metodoPago = :metodoPago")
    List<Venta> findByMetodoPago(@Param("metodoPago") String metodoPago);

    @Query("SELECT DISTINCT v FROM Venta v LEFT JOIN FETCH v.historial LEFT JOIN FETCH v.detalles WHERE v.id = :id")
    Optional<Venta> findByIdWithHistorial(@Param("id") Long id);
}
