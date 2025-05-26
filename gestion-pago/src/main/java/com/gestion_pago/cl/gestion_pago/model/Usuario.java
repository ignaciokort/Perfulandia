package com.gestion_pago.cl.gestion_pago.model;


import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor



public class Usuario{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 16, nullable = false)
    private String nro_cuenta;

    @Column(unique = true, length = 5, nullable = false)
    private Integer productoId;

    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false )
    private String correo;

    @Column (nullable = true)
    private Date fechaNacimiento;

    @Column(nullable = false)
    private Integer monto;

}