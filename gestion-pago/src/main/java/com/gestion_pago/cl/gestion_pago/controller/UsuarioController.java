

package com.gestion_pago.cl.gestion_pago.controller;


import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.gestion_pago.cl.gestion_pago.model.Usuario;
import com.gestion_pago.cl.gestion_pago.service.UsuarioService;

import jakarta.servlet.http.HttpSession;



@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioController{

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/procesar")
    public ResponseEntity<String> procesarPago(@RequestBody Usuario pago, HttpSession session) {
        // Validar que la sesión esté activa consultando el microservicio de autenticación
        Boolean isAuth = restTemplate.getForObject("http://localhost:8081/auth/validate", Boolean.class);
        if (!Boolean.TRUE.equals(isAuth)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado.");
        }
        
        // Llamar al microservicio de inventario para actualizar el stock del producto.
        // Por ejemplo, reducir la cantidad en 1 tras una compra:
        String inventarioResponse = restTemplate.postForObject(
            "http://localhost:8082/inventario/actualizar/" + pago.getProductoId() + "?cantidad=-1",
            null, 
            String.class
        );

        // Aquí se agregaría la lógica real para procesar el pago (transacción, validación, etc.).
        String respuesta = "Pago procesado para el cliente " + pago.getId() + ". " + inventarioResponse;
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listar(){
        List<Usuario> usuarios = usuarioService.findAll();
        if(usuarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);

    }
    //meotodode guardar
    @PostMapping
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario){
       Usuario productoNuevo = usuarioService.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoNuevo);
    }





    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable Integer id){
        try {
            Usuario usuario = usuarioService.findById(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Integer id,@RequestBody Usuario usuario){
        try{
            Usuario usu = usuarioService.findById(id);
            usu.setId(id);
            usu.setNro_cuenta(usuario.getNro_cuenta());
            usu.setNombre(usuario.getNombre());
            usu.setApellido(usuario.getApellido());
            usu.setCorreo(usuario.getCorreo());
            usu.setFechaNacimiento(usuario.getFechaNacimiento());
            usu.setMonto(usuario.getMonto());

            usuarioService.save(usu);
            return ResponseEntity.ok(usuario);
        } catch(Exception e){
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        try{
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }


    
}