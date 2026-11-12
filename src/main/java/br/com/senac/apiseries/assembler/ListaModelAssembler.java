package br.com.senac.apiseries.assembler;

import br.com.senac.apiseries.controller.ListaController;
import br.com.senac.apiseries.controller.UsuarioController;
import br.com.senac.apiseries.entity.Lista;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ListaModelAssembler
        implements RepresentationModelAssembler<Lista, EntityModel<Lista>> {

    @Override
    public EntityModel<Lista> toModel(Lista lista) {

        EntityModel<Lista> model = EntityModel.of(
                lista,

                linkTo(methodOn(ListaController.class)
                        .buscarPorId(lista.getId()))
                        .withSelfRel(),

                linkTo(methodOn(ListaController.class)
                        .listar(null, null))
                        .withRel("listas")
        );

        if (lista.getUsuario() != null) {
            model.add(
                    linkTo(methodOn(UsuarioController.class)
                            .buscarPorId(lista.getUsuario().getId()))
                            .withRel("usuario")
            );
        }

        return model;
    }
}