package com.autenticacion.perfulandia.Model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table(name = "usuario")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor

public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column(unique = true, nullable = false)
    private String correoUsuario;

    @Column(length = 10, nullable = false)
    private String contrasenaUsuario;

}
