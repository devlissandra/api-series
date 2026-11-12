package br.com.senac.apiseries.assembler;

import br.com.senac.apiseries.controller.CategoriaController;
import br.com.senac.apiseries.entity.Categoria;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CategoriaModelAssembler
        implements RepresentationModelAssembler<Categoria, EntityModel<Categoria>> {

    @Override
    public EntityModel<Categoria> toModel(Categoria categoria) {

        return EntityModel.of(
                categoria,

                linkTo(methodOn(CategoriaController.class)
                        .buscarPorId(categoria.getId()))
                        .withSelfRel(),

                linkTo(methodOn(CategoriaController.class)
                        .listar(null, null))
                        .withRel("categorias")
        );
    }
}