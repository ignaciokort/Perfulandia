package com.gestion_pago.cl.gestion_pago.controller;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import com.gestion_pago.cl.gestion_pago.Assembler.*;
import com.gestion_pago.cl.gestion_pago.model.Producto;
import com.gestion_pago.cl.gestion_pago.model.Usuario;
import com.gestion_pago.cl.gestion_pago.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Pagos", description = "Operaciones relacionadas con los pagos de los usuarios")
@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioModelAssembler assembler;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @PostMapping("/procesar")
    @Operation(summary = "Procesar pagos", description = "Procesa el pago y actualiza inventario")
    public ResponseEntity<EntityModel<Usuario>> procesarPago(@RequestBody Usuario pago, HttpSession session) {
        Boolean isAuth = webClientBuilder.build()
            .get()
            .uri("http://localhost:8081/auth/validate")
            .retrieve()
            .bodyToMono(Boolean.class)
            .block();

        if (!Boolean.TRUE.equals(isAuth)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Producto producto = webClientBuilder.build()
            .get()
            .uri("http://localhost:8082/productos/" + pago.getProductoId())
            .retrieve()
            .bodyToMono(Producto.class)
            .block();

        if (producto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Usuario usuario = usuarioService.findById(pago.getId());
        if (usuario.getMonto() < producto.getPrecio()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        usuario.setMonto(usuario.getMonto() - producto.getPrecio());
        usuarioService.save(usuario);

        webClientBuilder.build()
            .post()
            .uri("http://localhost:8082/inventario/actualizar/" + producto.getId() + "?cantidad=-1")
            .retrieve()
            .bodyToMono(String.class)
            .block();

        return ResponseEntity.ok(assembler.toModel(usuario));
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Lista todos los usuarios registrados")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listar() {
        List<Usuario> usuarios = usuarioService.findAll();

        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<Usuario>> usuariosConLinks = usuarios.stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(usuariosConLinks, linkTo(methodOn(UsuarioController.class).listar()).withSelfRel())
        );
    }

    @PostMapping
    @Operation(summary = "Guardar usuario", description = "Guarda un nuevo usuario con monto")
    public ResponseEntity<EntityModel<Usuario>> guardar(@RequestBody Usuario usuario) {
        if (usuario.getMonto() < 0) {
            return ResponseEntity.badRequest().build();
        }

        Usuario guardado = usuarioService.save(usuario);
        return ResponseEntity
            .created(linkTo(methodOn(UsuarioController.class).guardar(null)).toUri())
            .body(assembler.toModel(guardado));
    }

    @GetMapping("/{id}/producto/{productoId}")
    @Operation(summary = "Buscar y restar", description = "Resta monto por compra de producto")
    public ResponseEntity<EntityModel<Usuario>> buscarYRestar(@PathVariable Integer id, @PathVariable Long productoId) {
        Usuario usuario = usuarioService.findById(id);
        if (usuario == null) return ResponseEntity.notFound().build();

        Producto producto = webClientBuilder.build()
            .get()
            .uri("http://localhost:8082/productos/" + productoId)
            .retrieve()
            .bodyToMono(Producto.class)
            .block();

        if (producto == null || usuario.getMonto() < producto.getPrecio()) {
            return ResponseEntity.badRequest().build();
        }

        usuario.setMonto(usuario.getMonto() - producto.getPrecio());
        Usuario actualizado = usuarioService.save(usuario);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza un usuario por ID")
    public ResponseEntity<EntityModel<Usuario>> actualizar(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Usuario existente = usuarioService.findById(id);
        if (existente == null) return ResponseEntity.notFound().build();

        existente.setNro_cuenta(usuario.getNro_cuenta());
        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setCorreo(usuario.getCorreo());
        existente.setFechaNacimiento(usuario.getFechaNacimiento());
        existente.setMonto(usuario.getMonto());

        return ResponseEntity.ok(assembler.toModel(usuarioService.save(existente)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario por ID")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }
    }
}
