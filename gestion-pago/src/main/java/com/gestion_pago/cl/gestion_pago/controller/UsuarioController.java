package com.gestion_pago.cl.gestion_pago.controller;
import com.gestion_pago.cl.gestion_pago.model.Producto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import com.gestion_pago.cl.gestion_pago.model.Usuario;
import com.gestion_pago.cl.gestion_pago.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    // Procesar pago y actualizar inventario
    @PostMapping("/procesar")
    public ResponseEntity<String> procesarPago(@RequestBody Usuario pago, HttpSession session) {
        // Validar autenticación con WebClient
        Boolean isAuth = webClientBuilder.build()
            .get()
            .uri("http://localhost:8081/auth/validate")
            .retrieve()
            .bodyToMono(Boolean.class)
            .block();

        if (!Boolean.TRUE.equals(isAuth)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado.");
        }

        // Obtener información del producto
        Producto producto = webClientBuilder.build()
            .get()
            .uri("http://localhost:8082/productos/" + pago.getProductoId())
            .retrieve()
            .bodyToMono(Producto.class)
            .block();

        if (producto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado.");
        }

        // Validar saldo del usuario
        Usuario usuario = usuarioService.findById(pago.getId());
        if (usuario.getMonto() < producto.getPrecio()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Saldo insuficiente. Usuario tiene: " + usuario.getMonto() + ", se necesita: " + producto.getPrecio());
        }

        // Actualizar monto del usuario
        usuario.setMonto(usuario.getMonto() - producto.getPrecio());
        usuarioService.save(usuario);

        // Actualizar inventario
        String inventarioResponse = webClientBuilder.build()
            .post()
            .uri("http://localhost:8082/inventario/actualizar/" + producto.getId() + "?cantidad=-1")
            .retrieve()
            .bodyToMono(String.class)
            .block();

        return ResponseEntity.ok("Pago procesado. Nuevo saldo: " + usuario.getMonto() + ". " + inventarioResponse);
    }

    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> usuarios = usuarioService.findAll();
        return usuarios.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(usuarios);
    }

    // Guardar usuario
    @PostMapping
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario) {
        if (usuario.getMonto() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(usuario));
    }

    // Buscar usuario y restar precio del producto
    @GetMapping("/{id}/producto/{productoId}")
    public ResponseEntity<?> buscarYRestar(@PathVariable Integer id, @PathVariable Long productoId) {
        try {
            Usuario usuario = usuarioService.findById(id);
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
            }

            Producto producto = webClientBuilder.build()
                .get()
                .uri("http://localhost:8082/productos/" + productoId)
                .retrieve()
                .bodyToMono(Producto.class)
                .block();

            if (producto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado.");
            }

            if (usuario.getMonto() >= producto.getPrecio()) {
                usuario.setMonto(usuario.getMonto() - producto.getPrecio());
                usuarioService.save(usuario);
                return ResponseEntity.ok("Compra realizada. Nuevo saldo: " + usuario.getMonto());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo insuficiente.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la compra.");
        }
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        try {
            Usuario usu = usuarioService.findById(id);
            if (usu == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            usu.setId(id);
            usu.setNro_cuenta(usuario.getNro_cuenta());
            usu.setNombre(usuario.getNombre());
            usu.setApellido(usuario.getApellido());
            usu.setCorreo(usuario.getCorreo());
            usu.setFechaNacimiento(usuario.getFechaNacimiento());
            usu.setMonto(usuario.getMonto());

            return ResponseEntity.ok(usuarioService.save(usu));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }
    }
}
