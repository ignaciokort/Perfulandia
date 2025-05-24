package com.autenticacion.cl.autenticacion.service;

import com.autenticacion.cl.autenticacion.model.Cliente;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Cliente crearCliente(Cliente cliente) {
        entityManager.persist(cliente);
        return cliente;
    }

    public Optional<Cliente> obtenerClientePorId(Long id) {
        return Optional.ofNullable(entityManager.find(Cliente.class, id));
    }

    @Transactional
    public Cliente actualizarCliente(Long id, Cliente cliente) {
        Cliente clienteExistente = entityManager.find(Cliente.class, id);
        if (clienteExistente != null) {
            clienteExistente.setCorreoCliente(cliente.getCorreoCliente());
            clienteExistente.setContrasenaCliente(cliente.getContrasenaCliente());
            return entityManager.merge(clienteExistente);
        }
        return null;
    }

    @Transactional
    public void eliminarCliente(Long id) {
        Cliente cliente = entityManager.find(Cliente.class, id);
        if (cliente != null) {
            entityManager.remove(cliente);
        }
    }

    public List<Cliente> listarClientes() {
        return entityManager.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
    }
}