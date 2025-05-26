package com.autenticacion.cl.autenticacion.controller;

import com.autenticacion.cl.autenticacion.model.Cliente;
import com.autenticacion.cl.autenticacion.service.ClienteService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping("/login")
    public ResponseEntity<String> login(HttpSession session,
                                          @RequestParam String username,
                                          @RequestParam String password) {
        // Lógica de validación de credenciales (para demostración, se valida de forma simple)
        if ("usuario".equals(username) && "clave".equals(password)) {
            session.setAttribute("usuario", username);
            return ResponseEntity.ok("Sesión iniciada para " + username);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(HttpSession session) {
        return ResponseEntity.ok(session.getAttribute("usuario") != null);
    }

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.crearCliente(cliente));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerClientePorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.obtenerClientePorId(id);
        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente actualizado = clienteService.actualizarCliente(id, cliente);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.ok("Cliente eliminado correctamente.");
    }

    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }
}