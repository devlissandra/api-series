package br.com.senac.apiseries.assembler;

import br.com.senac.apiseries.controller.ItemListaController;
import br.com.senac.apiseries.controller.ListaController;
import br.com.senac.apiseries.controller.SerieController;
import br.com.senac.apiseries.entity.ItemLista;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ItemListaModelAssembler
        implements RepresentationModelAssembler<ItemLista, EntityModel<ItemLista>> {

    @Override
    public EntityModel<ItemLista> toModel(ItemLista itemLista) {

        EntityModel<ItemLista> model = EntityModel.of(
                itemLista,

                linkTo(methodOn(ItemListaController.class)
                        .buscarPorId(itemLista.getId()))
                        .withSelfRel(),

                linkTo(methodOn(ItemListaController.class)
                        .listar(null, null))
                        .withRel("itens-lista")
        );

        if (itemLista.getLista() != null) {
            model.add(
                    linkTo(methodOn(ListaController.class)
                            .buscarPorId(itemLista.getLista().getId()))
                            .withRel("lista")
            );
        }

        if (itemLista.getSerie() != null) {
            model.add(
                    linkTo(methodOn(SerieController.class)
                            .buscarPorId(itemLista.getSerie().getId()))
                            .withRel("serie")
            );
        }

        return model;
    }
}