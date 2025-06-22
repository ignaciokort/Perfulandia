package com.reporteVenta.reporteVenta.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reporteVenta.reporteVenta.model.Reporte;
import com.reporteVenta.reporteVenta.service.ReporteService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/ventas")
    @Operation(summary="Listar ventas", description="Obtener un reporte de ventas")
    public ResponseEntity<Reporte> obtenerReporteVentas() {
         Reporte reporte = new Reporte(null, "Reporte de Ventas", 10, 1500.0, null);
         return ResponseEntity.ok(reporte);
    }

    @PostMapping
    @Operation(summary="Crear reporte", description="Crea un nuevo reporte de ventas")
    public ResponseEntity<Reporte> crearReporte(@RequestBody Reporte reporteVenta) {
        return ResponseEntity.ok(reporteService.crearReporte(reporteVenta));
    }

    @GetMapping("/{id}")
    @Operation(summary="Obtener reportes", description="Obtiene los reportes por id")
    public ResponseEntity<?> obtenerReportePorId(@PathVariable Long id) {
        Optional<Reporte> reporte = reporteService.obtenerReportePorId(id);
        return reporte.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary="Actualizar reportes", description="Actualiza un reporte de ventas por id")
    public ResponseEntity<?> actualizarReporte(@PathVariable Long id, @RequestBody Reporte reporteVenta) {
        Reporte actualizado = reporteService.actualizarReporte(id, reporteVenta);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar reportes", description="Elimina un reporte de vetas por id")
    public ResponseEntity<?> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.ok("Reporte eliminado correctamente.");
    }

    @GetMapping
    @Operation(summary="Listar reportes", description="Lista todos los reportes de ventas")
    public List<Reporte> listarReportes() {
        return reporteService.listarReportes();
    }
}