package com.GestionInventario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.GestionInventario.Model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
