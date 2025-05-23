package com.gestion_pago.cl.gestion_pago.service;


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