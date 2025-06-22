package com.reporteVenta.reporteVenta.Assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.reporteVenta.reporteVenta.controller.ReporteController;
import com.reporteVenta.reporteVenta.model.Reporte;

@Component
public class ReporteModelAssembler implements RepresentationModelAssembler<Reporte, EntityModel<Reporte>> {

    @Override
    public EntityModel<Reporte> toModel(Reporte reporte) {
        return EntityModel.of(reporte,
            linkTo(methodOn(ReporteController.class).obtenerReportePorId(reporte.getIdReporte())).withSelfRel(),
            linkTo(methodOn(ReporteController.class).listarReportes()).withRel("todos-los-reportes"),
            linkTo(methodOn(ReporteController.class).crearReporte(null)).withRel("crear"),
            linkTo(methodOn(ReporteController.class).actualizarReporte(reporte.getIdReporte(), reporte)).withRel("actualizar"),
            linkTo(methodOn(ReporteController.class).eliminarReporte(reporte.getIdReporte())).withRel("eliminar")
        );
    }
}
