package com.GestionInventario.Assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.GestionInventario.Controller.ProductoController;
import com.GestionInventario.Model.Producto;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,
            linkTo(methodOn(ProductoController.class).obtenerProducto(producto.getId())).withSelfRel(),
            linkTo(methodOn(ProductoController.class).listarProductos()).withRel("todos-los-productos"),
            linkTo(methodOn(ProductoController.class).actualizarProducto(producto.getId(), producto)).withRel("actualizar"),
            linkTo(methodOn(ProductoController.class).eliminarProducto(producto.getId())).withRel("eliminar")
        );
    }
}