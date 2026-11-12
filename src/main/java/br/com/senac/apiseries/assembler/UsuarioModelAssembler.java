package br.com.senac.apiseries.assembler;

import br.com.senac.apiseries.controller.UsuarioController;
import br.com.senac.apiseries.entity.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioModelAssembler
        implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {

        return EntityModel.of(
                usuario,

                linkTo(methodOn(UsuarioController.class)
                        .buscarPorId(usuario.getId()))
                        .withSelfRel(),

                linkTo(methodOn(UsuarioController.class)
                        .listar(null, null))
                        .withRel("usuarios")
        );
    }
}