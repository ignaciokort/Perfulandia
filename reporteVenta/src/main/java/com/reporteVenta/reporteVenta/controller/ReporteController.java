package com.reporteVenta.reporteVenta.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reporteVenta.reporteVenta.Assembler.*;
import com.reporteVenta.reporteVenta.model.Reporte;
import com.reporteVenta.reporteVenta.service.ReporteService;

import io.swagger.v3.oas.annotations.Operation;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private ReporteModelAssembler assembler;

    @GetMapping("/ventas")
    @Operation(summary = "Listar ventas", description = "Obtener un reporte de ventas")
    public ResponseEntity<EntityModel<Reporte>> obtenerReporteVentas() {
        Reporte reporte = new Reporte(null, "Reporte de Ventas", 10, 1500.0, null);
        return ResponseEntity.ok(assembler.toModel(reporte));
    }

    @PostMapping
    @Operation(summary = "Crear reporte", description = "Crea un nuevo reporte de ventas")
    public ResponseEntity<EntityModel<Reporte>> crearReporte(@RequestBody Reporte reporteVenta) {
        Reporte creado = reporteService.crearReporte(reporteVenta);
        return ResponseEntity
            .created(linkTo(methodOn(ReporteController.class).obtenerReportePorId(creado.getIdReporte())).toUri())
            .body(assembler.toModel(creado));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reporte por ID", description = "Obtiene un reporte específico por su ID")
    public ResponseEntity<EntityModel<Reporte>> obtenerReportePorId(@PathVariable Long id) {
        Optional<Reporte> reporte = reporteService.obtenerReportePorId(id);
        return reporte.map(r -> ResponseEntity.ok(assembler.toModel(r)))
                      .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reporte", description = "Actualiza un reporte existente")
    public ResponseEntity<EntityModel<Reporte>> actualizarReporte(@PathVariable Long id, @RequestBody Reporte reporteVenta) {
        Reporte actualizado = reporteService.actualizarReporte(id, reporteVenta);
        return actualizado != null
            ? ResponseEntity.ok(assembler.toModel(actualizado))
            : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reporte", description = "Elimina un reporte existente")
    public ResponseEntity<?> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar todos los reportes", description = "Obtiene todos los reportes disponibles")
    public ResponseEntity<CollectionModel<EntityModel<Reporte>>> listarReportes() {
        List<Reporte> reportes = reporteService.listarReportes();
        if (reportes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<Reporte>> listaConLinks = reportes.stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(listaConLinks,
                linkTo(methodOn(ReporteController.class).listarReportes()).withSelfRel())
        );
    }
}
