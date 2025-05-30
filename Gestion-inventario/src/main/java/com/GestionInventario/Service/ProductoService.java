package com.GestionInventario.Service;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GestionInventario.Model.Producto;
import com.GestionInventario.Repository.ProductoRepository;

@Service
public class ProductoService {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

     public Optional<Producto> obtenerProductoPorId(Long id) {
        return Optional.ofNullable(entityManager.find(Producto.class, id));
    }

    public Producto actualizarProducto(Long id, Producto productoActualizado) {
    Producto producto = productoRepository.findById(id).orElse(null);
    if (producto != null) {
        producto.setNombre(productoActualizado.getNombre());
        producto.setCantidad(productoActualizado.getCantidad());
        producto.setPrecio(productoActualizado.getPrecio());
        return productoRepository.save(producto);
    } else {
        throw new RuntimeException("Producto no encontrado");
    }
}
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}
