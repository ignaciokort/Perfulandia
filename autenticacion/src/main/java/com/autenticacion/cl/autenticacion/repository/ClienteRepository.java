package com.autenticacion.cl.autenticacion.repository;

import com.autenticacion.cl.autenticacion.model.Cliente;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCorreoCliente(String correoCliente);
    boolean existsByCorreoCliente(String correoCliente);
}