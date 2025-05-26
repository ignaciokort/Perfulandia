package com.reporteVenta.reporteVenta.repository;

import com.reporteVenta.reporteVenta.model.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByProducto(String producto);
    List<Reporte> findByFechaVenta(String fechaVenta);
}