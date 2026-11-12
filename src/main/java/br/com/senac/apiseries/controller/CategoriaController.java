package br.com.senac.apiseries.controller;

import br.com.senac.apiseries.assembler.CategoriaModelAssembler;
import br.com.senac.apiseries.entity.Categoria;
import br.com.senac.apiseries.repository.CategoriaRepository;
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
@RequestMapping("/categorias")
@Tag(
        name = "Categorias",
        description = "Operações de cadastro, consulta, atualização e exclusão de categorias."
)
public class CategoriaController {

    private final CategoriaRepository repository;
    private final CategoriaModelAssembler assembler;

    public CategoriaController(
            CategoriaRepository repository,
            CategoriaModelAssembler assembler) {

        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(
            summary = "Listar categorias",
            description = "Retorna todas as categorias cadastradas utilizando paginação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categorias listadas com sucesso")
    })
    public PagedModel<EntityModel<Categoria>> listar(
            Pageable pageable,
            PagedResourcesAssembler<Categoria> pagedResourcesAssembler) {

        Page<Categoria> categorias =
                repository.findAll(pageable);

        return pagedResourcesAssembler.toModel(
                categorias,
                assembler
        );
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar categorias por nome",
            description = "Pesquisa categorias pelo nome, ignorando diferenças entre letras maiúsculas e minúsculas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    public PagedModel<EntityModel<Categoria>> buscarPorNome(
            @RequestParam String nome,
            Pageable pageable,
            PagedResourcesAssembler<Categoria> pagedResourcesAssembler) {

        Page<Categoria> categorias =
                repository.findByNomeContainingIgnoreCase(
                        nome,
                        pageable
                );

        return pagedResourcesAssembler.toModel(
                categorias,
                assembler
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar categoria por ID",
            description = "Retorna uma categoria específica a partir do seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<EntityModel<Categoria>> buscarPorId(
            @PathVariable Long id) {

        return repository.findById(id)
                .map(categoria ->
                        ResponseEntity.ok(
                                assembler.toModel(categoria)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar categoria",
            description = "Cria uma nova categoria."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<EntityModel<Categoria>> cadastrar(
            @Valid @RequestBody Categoria categoria) {

        Categoria novaCategoria = repository.save(categoria);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(novaCategoria));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar categoria",
            description = "Atualiza os dados de uma categoria existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<EntityModel<Categoria>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Categoria categoria) {

        return repository.findById(id)
                .map(categoriaExistente -> {

                    categoriaExistente.setNome(
                            categoria.getNome()
                    );

                    Categoria atualizada =
                            repository.save(categoriaExistente);

                    return ResponseEntity.ok(
                            assembler.toModel(atualizada)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir categoria",
            description = "Exclui uma categoria pelo seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}