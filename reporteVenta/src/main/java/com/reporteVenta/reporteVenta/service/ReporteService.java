package com.reporteVenta.reporteVenta.service;

import com.reporteVenta.reporteVenta.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Reporte crearReporte(Reporte reporteVenta) {
        entityManager.persist(reporteVenta);
        return reporteVenta;
    }

    public Optional<Reporte> obtenerReportePorId(Long id) {
        return Optional.ofNullable(entityManager.find(Reporte.class, id));
    }

    public List<Reporte> listarReportes() {
        return entityManager.createQuery("SELECT r FROM ReporteVenta r", Reporte.class).getResultList();
    }

    @Transactional
    public Reporte actualizarReporte(Long id, Reporte reporteVenta) {
        Reporte reporteExistente = entityManager.find(Reporte.class, id);
        if (reporteExistente != null) {
            reporteExistente.setProducto(reporteVenta.getProducto());
            reporteExistente.setCantidadVendida(reporteVenta.getCantidadVendida());
            reporteExistente.setTotalVentas(reporteVenta.getTotalVentas());
            reporteExistente.setFechaVenta(reporteVenta.getFechaVenta());
            return entityManager.merge(reporteExistente);
        }
        return null;
    }

    @Transactional
    public void eliminarReporte(Long id) {
        Reporte reporteVenta = entityManager.find(Reporte.class, id);
        if (reporteVenta != null) {
            entityManager.remove(reporteVenta);
        }
    }
}