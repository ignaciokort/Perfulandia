package com.reporteVenta.reporteVenta.controller;

import com.reporteVenta.reporteVenta.model.*;
import com.reporteVenta.reporteVenta.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @PostMapping
    public ResponseEntity<Reporte> crearReporte(@RequestBody Reporte reporteVenta) {
        return ResponseEntity.ok(reporteService.crearReporte(reporteVenta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerReportePorId(@PathVariable Long id) {
        Optional<Reporte> reporte = reporteService.obtenerReportePorId(id);
        return reporte.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarReporte(@PathVariable Long id, @RequestBody Reporte reporteVenta) {
        Reporte actualizado = reporteService.actualizarReporte(id, reporteVenta);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.ok("Reporte eliminado correctamente.");
    }

    @GetMapping
    public List<Reporte> listarReportes() {
        return reporteService.listarReportes();
    }
}