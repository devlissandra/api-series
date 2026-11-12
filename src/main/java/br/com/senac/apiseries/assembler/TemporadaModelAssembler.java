package br.com.senac.apiseries.assembler;

import br.com.senac.apiseries.controller.SerieController;
import br.com.senac.apiseries.controller.TemporadaController;
import br.com.senac.apiseries.entity.Temporada;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TemporadaModelAssembler
        implements RepresentationModelAssembler<Temporada, EntityModel<Temporada>> {

    @Override
    public EntityModel<Temporada> toModel(Temporada temporada) {

        EntityModel<Temporada> model = EntityModel.of(
                temporada,

                linkTo(methodOn(TemporadaController.class)
                        .buscarPorId(temporada.getId()))
                        .withSelfRel(),

                linkTo(methodOn(TemporadaController.class)
                        .listar(null, null))
                        .withRel("temporadas")
        );

        if (temporada.getSerie() != null) {
            model.add(
                    linkTo(methodOn(SerieController.class)
                            .buscarPorId(temporada.getSerie().getId()))
                            .withRel("serie")
            );
        }

        return model;
    }
}