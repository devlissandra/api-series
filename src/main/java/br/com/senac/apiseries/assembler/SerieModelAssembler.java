package br.com.senac.apiseries.assembler;

import br.com.senac.apiseries.controller.CategoriaController;
import br.com.senac.apiseries.controller.SerieController;
import br.com.senac.apiseries.controller.TemporadaController;
import br.com.senac.apiseries.entity.Serie;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class SerieModelAssembler
        implements RepresentationModelAssembler<Serie, EntityModel<Serie>> {

    @Override
    public EntityModel<Serie> toModel(Serie serie) {

        EntityModel<Serie> model = EntityModel.of(
                serie,

                linkTo(methodOn(SerieController.class)
                        .buscarPorId(serie.getId()))
                        .withSelfRel(),

                linkTo(methodOn(SerieController.class)
                        .listar(null, null))
                        .withRel("series")
        );

        if (serie.getCategorias() != null) {
            serie.getCategorias().forEach(categoria ->
                    model.add(
                            linkTo(methodOn(CategoriaController.class)
                                    .buscarPorId(categoria.getId()))
                                    .withRel("categoria")
                    )
            );
        }

        return model;
    }
}