package br.com.senac.apiseries.controller;

import br.com.senac.apiseries.assembler.ItemListaModelAssembler;
import br.com.senac.apiseries.entity.ItemLista;
import br.com.senac.apiseries.entity.StatusAcompanhamento;
import br.com.senac.apiseries.repository.ItemListaRepository;
import br.com.senac.apiseries.repository.ListaRepository;
import br.com.senac.apiseries.repository.SerieRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/itens-lista")
@Tag(
        name = "Itens das Listas",
        description = "Operações para adicionar e gerenciar séries dentro das listas de acompanhamento."
)
public class ItemListaController {

    private final ItemListaRepository itemListaRepository;
    private final ListaRepository listaRepository;
    private final SerieRepository serieRepository;
    private final ItemListaModelAssembler assembler;

    public ItemListaController(
            ItemListaRepository itemListaRepository,
            ListaRepository listaRepository,
            SerieRepository serieRepository,
            ItemListaModelAssembler assembler) {

        this.itemListaRepository = itemListaRepository;
        this.listaRepository = listaRepository;
        this.serieRepository = serieRepository;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(
            summary = "Listar itens das listas",
            description = "Retorna todos os itens cadastrados nas listas utilizando paginação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Itens listados com sucesso")
    })
    public PagedModel<EntityModel<ItemLista>> listar(
            Pageable pageable,
            PagedResourcesAssembler<ItemLista> pagedResourcesAssembler) {

        Page<ItemLista> itens =
                itemListaRepository.findAll(pageable);

        return pagedResourcesAssembler.toModel(
                itens,
                assembler
        );
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar itens por status",
            description = "Pesquisa itens das listas pelo status de acompanhamento."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    public PagedModel<EntityModel<ItemLista>> buscarPorStatus(
            @RequestParam String status,
            Pageable pageable,
            PagedResourcesAssembler<ItemLista> pagedResourcesAssembler) {

        StatusAcompanhamento statusConvertido =
                StatusAcompanhamento.fromString(status);

        Page<ItemLista> itens =
                itemListaRepository.findByStatus(
                        statusConvertido,
                        pageable
                );

        return pagedResourcesAssembler.toModel(
                itens,
                assembler
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar item por ID",
            description = "Retorna um item específico de uma lista."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item encontrado"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    public ResponseEntity<EntityModel<ItemLista>> buscarPorId(
            @PathVariable Long id) {

        return itemListaRepository.findById(id)
                .map(item ->
                        ResponseEntity.ok(
                                assembler.toModel(item)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Adicionar série a uma lista",
            description = "Cria um item de lista relacionando uma lista a uma série e definindo seu status de acompanhamento."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Lista ou série não encontrada")
    })
    public ResponseEntity<?> cadastrar(
            @Valid @RequestBody ItemListaRequest request) {

        var lista =
                listaRepository.findById(request.getListaId());

        if (lista.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var serie =
                serieRepository.findById(request.getSerieId());

        if (serie.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ItemLista item = new ItemLista();

        item.setLista(lista.get());
        item.setSerie(serie.get());
        item.setStatus(request.getStatus());

        ItemLista novoItem =
                itemListaRepository.save(item);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(novoItem));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar item da lista",
            description = "Atualiza a lista, a série ou o status de acompanhamento de um item."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Item, lista ou série não encontrada")
    })
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ItemListaRequest request) {

        return itemListaRepository.findById(id)
                .map(itemExistente -> {

                    var lista =
                            listaRepository.findById(
                                    request.getListaId()
                            );

                    if (lista.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    var serie =
                            serieRepository.findById(
                                    request.getSerieId()
                            );

                    if (serie.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    itemExistente.setLista(lista.get());
                    itemExistente.setSerie(serie.get());
                    itemExistente.setStatus(request.getStatus());

                    ItemLista atualizado =
                            itemListaRepository.save(
                                    itemExistente
                            );

                    return ResponseEntity.ok(
                            assembler.toModel(atualizado)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir item da lista",
            description = "Remove um item de uma lista."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        if (!itemListaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        itemListaRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}