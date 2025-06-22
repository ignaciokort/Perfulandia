package com.gestion_pago.cl.gestion_pago.Assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.gestion_pago.cl.gestion_pago.controller.UsuarioController;
import com.gestion_pago.cl.gestion_pago.model.Usuario;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
            linkTo(methodOn(UsuarioController.class).guardar(null)).withRel("crear"),
            linkTo(methodOn(UsuarioController.class).actualizar(usuario.getId(), usuario)).withRel("actualizar"),
            linkTo(methodOn(UsuarioController.class).eliminar(usuario.getId().longValue())).withRel("eliminar"),
            linkTo(methodOn(UsuarioController.class).buscarYRestar(usuario.getId(), usuario.getProductoId())).withRel("comprar-producto"),
            linkTo(methodOn(UsuarioController.class).listar()).withRel("todos-los-usuarios"),
            linkTo(methodOn(UsuarioController.class).procesarPago(usuario, null)).withRel("procesar-pago")
        );
    }
}
