package com.hospital_vm.cl.hospital_vm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hospital_vm.cl.hospital_vm.model.Paciente;
import com.hospital_vm.cl.hospital_vm.service.PacienteService;

@RestController
@RequestMapping("/api/v1/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<Paciente>> listar(){
        List<Paciente> pacientes = pacienteService.findAll();
        if (pacientes.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.ok(pacientes);
    }

    @PostMapping
    public ResponseEntity<Paciente> guardar(@RequestBody Paciente paciente){
        Paciente productoNuevo = pacienteService.save(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoNuevo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscar(@PathVariable Integer id){
        try {
            Paciente paciente = pacienteService.findById(id);
            return ResponseEntity.ok(paciente);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizar(@PathVariable Integer id, @RequestBody Paciente paciente){
        try {
            Paciente pac =  pacienteService.findById(id);
            pac.setId(id);
            pac.setRun(paciente.getRun());
            pac.setNombre(paciente.getNombre());
            pac.setApellido(paciente.getApellido());
            pac.setFechaNacimiento(paciente.getFechaNacimiento());
            pac.setCorreo(paciente.getCorreo());

            pacienteService.save(pac);
            return ResponseEntity.ok(paciente);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        try {
            pacienteService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }




    
}
