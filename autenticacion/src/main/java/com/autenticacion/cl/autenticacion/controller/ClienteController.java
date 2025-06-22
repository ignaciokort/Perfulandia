package com.autenticacion.cl.autenticacion.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.autenticacion.cl.autenticacion.Assembler.ClienteModelAssembler;
import com.autenticacion.cl.autenticacion.model.Cliente;
import com.autenticacion.cl.autenticacion.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Cliente", description="Operaciones relacionadas con clientes")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteModelAssembler assembler;

    @GetMapping("/validar/{correo}")
    @Operation(summary = "Validar Clientes por correo", description = "Valida si un cliente existe por correo")
    public ResponseEntity<Boolean> validarCliente(@PathVariable String correo) {
        boolean esValido = clienteService.clienteExiste(correo);
        return ResponseEntity.ok(esValido);
    }

    @PostMapping
    @Operation(summary = "Crear Cliente", description = "Crea un nuevo cliente")
    public ResponseEntity<EntityModel<Cliente>> crearCliente(@RequestBody Cliente cliente) {
        Cliente creado = clienteService.crearCliente(cliente);
        return ResponseEntity
            .created(linkTo(methodOn(ClienteController.class).obtenerClientePorId(creado.getIdCliente())).toUri())
            .body(assembler.toModel(creado));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener Cliente", description = "Obtiene un cliente por ID")
    public ResponseEntity<EntityModel<Cliente>> obtenerClientePorId(@PathVariable Long id) {
        return clienteService.obtenerClientePorId(id)
            .map(cliente -> ResponseEntity.ok(assembler.toModel(cliente)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar Cliente", description = "Actualiza un cliente por ID")
    public ResponseEntity<EntityModel<Cliente>> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente actualizado = clienteService.actualizarCliente(id, cliente);
        return actualizado != null ?
            ResponseEntity.ok(assembler.toModel(actualizado)) :
            ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Cliente", description = "Elimina un cliente por ID")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.ok("Cliente eliminado correctamente.");
    }

    @GetMapping
    @Operation(summary = "Listar Clientes", description = "Obtiene una lista de todos los clientes")
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> listarClientes() {
        List<EntityModel<Cliente>> clientes = clienteService.listarClientes()
            .stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return ResponseEntity.ok(
            CollectionModel.of(clientes, linkTo(methodOn(ClienteController.class).listarClientes()).withSelfRel())
        );
    }
}
