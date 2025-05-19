package com.autenticacion.perfulandia.Service;

import com.autenticacion.perfulandia.Model.Usuario;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Usuario crearUsuario(Usuario usuario) {
        entityManager.persist(usuario);
        return usuario;
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return Optional.ofNullable(entityManager.find(Usuario.class, id));
    }

    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        Usuario usuarioExistente = entityManager.find(Usuario.class, id);
        if (usuarioExistente != null) {
            usuarioExistente.setCorreoUsuario(usuario.getCorreoUsuario());
            usuarioExistente.setContrasenaUsuario(usuario.getContrasenaUsuario());
            return entityManager.merge(usuarioExistente);
        }
        return null;
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = entityManager.find(Usuario.class, id);
        if (usuario != null) {
            entityManager.remove(usuario);
        }
    }

    public List<Usuario> listarUsuarios() {
        return entityManager.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }
}
    

