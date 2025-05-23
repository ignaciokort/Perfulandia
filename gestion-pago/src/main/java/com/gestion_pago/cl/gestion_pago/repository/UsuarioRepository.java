package com.gestion_pago.cl.gestion_pago.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gestion_pago.cl.gestionpago.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}