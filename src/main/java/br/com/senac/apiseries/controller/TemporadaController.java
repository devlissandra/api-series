package br.com.senac.apiseries.controller;

import br.com.senac.apiseries.assembler.TemporadaModelAssembler;
import br.com.senac.apiseries.entity.Temporada;
import br.com.senac.apiseries.repository.SerieRepository;
import br.com.senac.apiseries.repository.TemporadaRepository;
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
@RequestMapping("/temporadas")
@Tag(
        name = "Temporadas",
        description = "Operações de gerenciamento das temporadas das séries."
)
public class TemporadaController {

    private final TemporadaRepository temporadaRepository;
    private final SerieRepository serieRepository;
    private final TemporadaModelAssembler assembler;

    public TemporadaController(
            TemporadaRepository temporadaRepository,
            SerieRepository serieRepository,
            TemporadaModelAssembler assembler) {

        this.temporadaRepository = temporadaRepository;
        this.serieRepository = serieRepository;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(
            summary = "Listar temporadas",
            description = "Retorna todas as temporadas cadastradas utilizando paginação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Temporadas listadas com sucesso")
    })
    public PagedModel<EntityModel<Temporada>> listar(
            Pageable pageable,
            PagedResourcesAssembler<Temporada> pagedResourcesAssembler) {

        Page<Temporada> temporadas =
                temporadaRepository.findAll(pageable);

        return pagedResourcesAssembler.toModel(
                temporadas,
                assembler
        );
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar temporadas por série",
            description = "Retorna as temporadas pertencentes a uma série específica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    public PagedModel<EntityModel<Temporada>> buscarPorSerie(
            @RequestParam Long serieId,
            Pageable pageable,
            PagedResourcesAssembler<Temporada> pagedResourcesAssembler) {

        Page<Temporada> temporadas =
                temporadaRepository.findBySerieId(
                        serieId,
                        pageable
                );

        return pagedResourcesAssembler.toModel(
                temporadas,
                assembler
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar temporada por ID",
            description = "Retorna uma temporada específica pelo seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Temporada encontrada"),
            @ApiResponse(responseCode = "404", description = "Temporada não encontrada")
    })
    public ResponseEntity<EntityModel<Temporada>> buscarPorId(
            @PathVariable Long id) {

        return temporadaRepository.findById(id)
                .map(temporada ->
                        ResponseEntity.ok(
                                assembler.toModel(temporada)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar temporada",
            description = "Cria uma nova temporada vinculada a uma série."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Temporada criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Série não encontrada")
    })
    public ResponseEntity<EntityModel<Temporada>> cadastrar(
            @Valid @RequestBody TemporadaRequest request) {

        var serie =
                serieRepository.findById(request.getSerieId());

        if (serie.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Temporada temporada = new Temporada();

        temporada.setNumero(request.getNumero());
        temporada.setQuantidadeEpisodios(
                request.getQuantidadeEpisodios()
        );
        temporada.setSerie(serie.get());

        Temporada novaTemporada =
                temporadaRepository.save(temporada);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(novaTemporada));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar temporada",
            description = "Atualiza os dados e a série vinculada a uma temporada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Temporada atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Temporada ou série não encontrada")
    })
    public ResponseEntity<EntityModel<Temporada>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TemporadaRequest request) {

        var temporadaOptional =
                temporadaRepository.findById(id);

        if (temporadaOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Temporada temporadaExistente =
                temporadaOptional.get();

        var serie =
                serieRepository.findById(request.getSerieId());

        if (serie.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        temporadaExistente.setNumero(
                request.getNumero()
        );

        temporadaExistente.setQuantidadeEpisodios(
                request.getQuantidadeEpisodios()
        );

        temporadaExistente.setSerie(serie.get());

        Temporada atualizada =
                temporadaRepository.save(
                        temporadaExistente
                );

        return ResponseEntity.ok(
                assembler.toModel(atualizada)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir temporada",
            description = "Exclui uma temporada pelo seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Temporada excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Temporada não encontrada")
    })
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        if (!temporadaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        temporadaRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}