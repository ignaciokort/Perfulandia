package com.GestionInventario.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GestionInventario.Model.Producto;
import com.GestionInventario.Repository.ProductoRepository;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
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
