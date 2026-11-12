package br.com.senac.apiseries.assembler;

import br.com.senac.apiseries.controller.PerfilController;
import br.com.senac.apiseries.controller.UsuarioController;
import br.com.senac.apiseries.entity.Perfil;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PerfilModelAssembler
        implements RepresentationModelAssembler<Perfil, EntityModel<Perfil>> {

    @Override
    public EntityModel<Perfil> toModel(Perfil perfil) {

        EntityModel<Perfil> model = EntityModel.of(
                perfil,

                linkTo(methodOn(PerfilController.class)
                        .buscarPorId(perfil.getId()))
                        .withSelfRel(),

                linkTo(methodOn(PerfilController.class)
                        .listar(null, null))
                        .withRel("perfis")
        );

        if (perfil.getUsuario() != null) {
            model.add(
                    linkTo(methodOn(UsuarioController.class)
                            .buscarPorId(perfil.getUsuario().getId()))
                            .withRel("usuario")
            );
        }

        return model;
    }
}