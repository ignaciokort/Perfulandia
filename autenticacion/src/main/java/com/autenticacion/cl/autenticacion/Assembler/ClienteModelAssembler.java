package com.autenticacion.cl.autenticacion.Assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.autenticacion.cl.autenticacion.controller.ClienteController;
import com.autenticacion.cl.autenticacion.model.Cliente;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<Cliente, EntityModel<Cliente>> {

    @Override
    public EntityModel<Cliente> toModel(Cliente cliente) {
        return EntityModel.of(cliente,
            linkTo(methodOn(ClienteController.class).obtenerClientePorId(cliente.getIdCliente())).withSelfRel(),
            linkTo(methodOn(ClienteController.class).listarClientes()).withRel("clientes"),
            linkTo(methodOn(ClienteController.class).actualizarCliente(cliente.getIdCliente(), cliente)).withRel("actualizar"),
            linkTo(methodOn(ClienteController.class).eliminarCliente(cliente.getIdCliente())).withRel("eliminar"),
            linkTo(methodOn(ClienteController.class).crearCliente(null)).withRel("crear")
        );
    }
}
