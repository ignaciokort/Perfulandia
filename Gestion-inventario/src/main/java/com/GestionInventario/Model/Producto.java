package com.GestionInventario.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "inventario")
@NoArgsConstructor
@AllArgsConstructor

public class Inventario {

     @Id
     @GeneratedValue(strategy=GenerationType.IDENTITY)
     private Integer idProducto;

     @Column(nullable=false)
     private String nombreProducto;

     @Column(nullable=false)
     private String descripcion;

     @Column(nullable=false)
     private Integer cantidad;

}
