package com.gestion_pago.cl.gestion_pago.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.service;

import com.gestion_pago.cl.gestion_pago.model.Usuario;
import com.gestion_pago.cl.gestion_pago.repository.UsuarioRepository;
import jakarta.transactional.Transactional;

@service
@Transactional

public class UsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }
    public Usuario findById(Long id){
        return usuarioRepository.findById(id).get()
    }
    public Usuario save(Usuario usuario){
        return usuarioRepository.save(usuario);

    }
    public void delete(Long id){
        usuarioRepository.deleteById(id);
    }


}