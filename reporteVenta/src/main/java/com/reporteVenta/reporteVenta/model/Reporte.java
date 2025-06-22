package com.reporteVenta.reporteVenta.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reporte") 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReporte;

    @Column(nullable = false)
    private String producto;

    @Column(nullable = false)
    private int cantidadVendida;

    @Column(nullable = false)
    private double totalVentas;

    @Column(nullable = false)
    private String fechaVenta;
}
