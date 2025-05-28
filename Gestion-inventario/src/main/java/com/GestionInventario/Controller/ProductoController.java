package com.GestionInventario.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.GestionInventario.Model.Producto;
import com.GestionInventario.Service.ProductoService;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;

    @GetMapping("/{productoId}")
    public ResponseEntity<?> obtenerProducto(@PathVariable Long productoId) {
        System.out.println("🔍 Buscando producto con ID: " + productoId);

        Optional<Producto> productoOpt = productoService.obtenerProductoPorId(productoId);
        
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            System.out.println("✅ Producto encontrado: " + producto.getNombre() + " - Cantidad: " + producto.getCantidad());
            return ResponseEntity.ok(producto);
        } else {
            System.out.println(" Producto con ID " + productoId + "no se encontro");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no se encontro.");
        }
    }

    @PostMapping("/actualizar/{productoId}")
    public ResponseEntity<String> actualizarInventario(@PathVariable Long productoId, 
                                                       @RequestParam int cantidad) {
        if (cantidad <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La cantidad debe ser mayor a 0.");
        }

        Optional<Producto> productoOpt = productoService.obtenerProductoPorId(productoId);
        
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            
            // Verificar que haya suficiente stock antes de restar
            if (producto.getCantidad() >= cantidad) {
                producto.setCantidad(producto.getCantidad() - cantidad); // Restar cantidad comprada
                productoService.guardarProducto(producto);
                return ResponseEntity.ok("Stock actualizado: quedan " + producto.getCantidad() + " unidades.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stock insuficiente. Solo quedan " + producto.getCantidad() + " unidades.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = productoService.obtenerTodos();
        if (productos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(productos);
    }

    @PostMapping
    public ResponseEntity<Producto> agregarProducto(@RequestBody Producto producto) {
        if (producto.getNombre() == null || producto.getCantidad() == null || producto.getPrecio() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.guardarProducto(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        if (producto.getNombre() == null || producto.getCantidad() == null || producto.getPrecio() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(productoService.actualizarProducto(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
