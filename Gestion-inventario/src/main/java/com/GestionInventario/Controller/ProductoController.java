package com.GestionInventario.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.GestionInventario.Model.Producto;
import com.GestionInventario.Service.ProductoService;
import com.GestionInventario.Assembler.*;

@Tag(name="productos", description="Operaciones relacionadas con los productos")
@RestController
@RequestMapping("/api/v2/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoModelAssembler assembler;

    @GetMapping("/{productoId}")
    @Operation(summary="Optener productos", description="Valida si un producto existe")
    public ResponseEntity<EntityModel<Producto>> obtenerProducto(@PathVariable Long productoId) {
        return productoService.obtenerProductoPorId(productoId)
                .map(producto -> ResponseEntity.ok(assembler.toModel(producto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary="Listar productos", description="Lista productos por id")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> listarProductos() {
        List<EntityModel<Producto>> productos = productoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(productos, linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel())
        );
    }

    @PostMapping
    @Operation(summary="Agregar producto", description="Agregar un producto")
    public ResponseEntity<EntityModel<Producto>> agregarProducto(@RequestBody Producto producto) {
        if (producto.getNombre() == null || producto.getCantidad() == null || producto.getPrecio() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Producto creado = productoService.guardarProducto(producto);

        return ResponseEntity
            .created(linkTo(methodOn(ProductoController.class).obtenerProducto(creado.getId())).toUri())
            .body(assembler.toModel(creado));
    }


    @PutMapping("/{id}")
    @Operation(summary="Actualizar productos", description="Actualizar productos por id")
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto actualizado = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar producto", description="Eliminar un productopor id")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
