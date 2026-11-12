package br.com.senac.apiseries.controller;

import br.com.senac.apiseries.assembler.SerieModelAssembler;
import br.com.senac.apiseries.entity.Categoria;
import br.com.senac.apiseries.entity.Serie;
import br.com.senac.apiseries.repository.CategoriaRepository;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/series")
@Tag(
        name = "Séries",
        description = "Operações de cadastro, consulta, atualização e exclusão de séries asiáticas."
)
public class SerieController {

    private final SerieRepository serieRepository;
    private final CategoriaRepository categoriaRepository;
    private final SerieModelAssembler assembler;

    public SerieController(
            SerieRepository serieRepository,
            CategoriaRepository categoriaRepository,
            SerieModelAssembler assembler) {

        this.serieRepository = serieRepository;
        this.categoriaRepository = categoriaRepository;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(
            summary = "Listar séries",
            description = "Retorna todas as séries cadastradas utilizando paginação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Séries listadas com sucesso")
    })
    public PagedModel<EntityModel<Serie>> listar(
            Pageable pageable,
            PagedResourcesAssembler<Serie> pagedResourcesAssembler) {

        Page<Serie> series =
                serieRepository.findAll(pageable);

        return pagedResourcesAssembler.toModel(
                series,
                assembler
        );
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar séries por título",
            description = "Pesquisa séries pelo título, ignorando diferenças entre letras maiúsculas e minúsculas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    public PagedModel<EntityModel<Serie>> buscarPorTitulo(
            @RequestParam String titulo,
            Pageable pageable,
            PagedResourcesAssembler<Serie> pagedResourcesAssembler) {

        Page<Serie> series =
                serieRepository.findByTituloContainingIgnoreCase(
                        titulo,
                        pageable
                );

        return pagedResourcesAssembler.toModel(
                series,
                assembler
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar série por ID",
            description = "Retorna uma série específica pelo seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Série encontrada"),
            @ApiResponse(responseCode = "404", description = "Série não encontrada")
    })
    public ResponseEntity<EntityModel<Serie>> buscarPorId(
            @PathVariable Long id) {

        return serieRepository.findById(id)
                .map(serie ->
                        ResponseEntity.ok(
                                assembler.toModel(serie)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar série",
            description = "Cria uma nova série e permite associá-la a uma ou mais categorias."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Série criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<EntityModel<Serie>> cadastrar(
            @Valid @RequestBody SerieRequest request) {

        Serie serie = new Serie();

        serie.setTitulo(request.getTitulo());
        serie.setPaisOrigem(request.getPaisOrigem());
        serie.setAnoLancamento(request.getAnoLancamento());
        serie.setStatus(request.getStatus());

        List<Categoria> categorias = new ArrayList<>();

        if (request.getCategoriaIds() != null) {

            for (Long categoriaId : request.getCategoriaIds()) {

                var categoria =
                        categoriaRepository.findById(categoriaId);

                if (categoria.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }

                categorias.add(categoria.get());
            }
        }

        serie.setCategorias(categorias);

        Serie novaSerie =
                serieRepository.save(serie);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(novaSerie));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar série",
            description = "Atualiza os dados de uma série e suas categorias."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Série atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Série ou categoria não encontrada")
    })
    public ResponseEntity<EntityModel<Serie>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody SerieRequest request) {

        var serieOptional =
                serieRepository.findById(id);

        if (serieOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Serie serieExistente =
                serieOptional.get();

        serieExistente.setTitulo(request.getTitulo());
        serieExistente.setPaisOrigem(request.getPaisOrigem());
        serieExistente.setAnoLancamento(
                request.getAnoLancamento()
        );
        serieExistente.setStatus(request.getStatus());

        List<Categoria> categorias =
                new ArrayList<>();

        if (request.getCategoriaIds() != null) {

            for (Long categoriaId :
                    request.getCategoriaIds()) {

                var categoria =
                        categoriaRepository.findById(categoriaId);

                if (categoria.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }

                categorias.add(categoria.get());
            }
        }

        serieExistente.setCategorias(categorias);

        Serie atualizada =
                serieRepository.save(serieExistente);

        return ResponseEntity.ok(
                assembler.toModel(atualizada)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir série",
            description = "Exclui uma série pelo seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Série excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Série não encontrada")
    })
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        if (!serieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        serieRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}