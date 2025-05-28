package com.gestion_pago.cl.gestion_pago.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 16, nullable = false)
    private String nro_cuenta;

    @Column(nullable = false)
    private Long productoId; // Cambiado a Long si coincide con la base de datos

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String correo;

    @Column(nullable = true)
    private LocalDate fechaNacimiento; // Mejor uso de fechas

    @Column(nullable = false)
    private Double monto; // Cambiado a Double para permitir decimales

    // Constructor vacío
    public Usuario() {}

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNro_cuenta() { return nro_cuenta; }
    public void setNro_cuenta(String nro_cuenta) { this.nro_cuenta = nro_cuenta; }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
}
