package com.autenticacion.cl.autenticacion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.autenticacion.cl.autenticacion.model.Cliente;
import com.autenticacion.cl.autenticacion.repository.ClienteRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

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

    public boolean clienteExiste(String correo) {
        return clienteRepository.existsByCorreoCliente(correo);
    }
}