package br.com.senac.apiseries.controller;

import br.com.senac.apiseries.assembler.ListaModelAssembler;
import br.com.senac.apiseries.entity.Lista;
import br.com.senac.apiseries.repository.ListaRepository;
import br.com.senac.apiseries.repository.UsuarioRepository;
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
@RequestMapping("/listas")
@Tag(
        name = "Listas",
        description = "Operações para criação e gerenciamento das listas de acompanhamento dos usuários."
)
public class ListaController {

    private final ListaRepository listaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ListaModelAssembler assembler;

    public ListaController(
            ListaRepository listaRepository,
            UsuarioRepository usuarioRepository,
            ListaModelAssembler assembler) {

        this.listaRepository = listaRepository;
        this.usuarioRepository = usuarioRepository;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(
            summary = "Listar listas",
            description = "Retorna todas as listas cadastradas utilizando paginação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listas listadas com sucesso")
    })
    public PagedModel<EntityModel<Lista>> listar(
            Pageable pageable,
            PagedResourcesAssembler<Lista> pagedResourcesAssembler) {

        Page<Lista> listas =
                listaRepository.findAll(pageable);

        return pagedResourcesAssembler.toModel(
                listas,
                assembler
        );
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar listas por usuário",
            description = "Retorna as listas pertencentes a um usuário específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    public PagedModel<EntityModel<Lista>> buscarPorUsuario(
            @RequestParam Long usuarioId,
            Pageable pageable,
            PagedResourcesAssembler<Lista> pagedResourcesAssembler) {

        Page<Lista> listas =
                listaRepository.findByUsuarioId(
                        usuarioId,
                        pageable
                );

        return pagedResourcesAssembler.toModel(
                listas,
                assembler
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar lista por ID",
            description = "Retorna uma lista específica pelo seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista encontrada"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada")
    })
    public ResponseEntity<EntityModel<Lista>> buscarPorId(
            @PathVariable Long id) {

        return listaRepository.findById(id)
                .map(lista ->
                        ResponseEntity.ok(
                                assembler.toModel(lista)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar lista",
            description = "Cria uma nova lista vinculada a um usuário."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lista criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<EntityModel<Lista>> cadastrar(
            @Valid @RequestBody ListaRequest request) {

        return usuarioRepository
                .findById(request.getUsuarioId())
                .map(usuario -> {

                    Lista lista = new Lista();

                    lista.setNome(request.getNome());
                    lista.setUsuario(usuario);

                    Lista salva =
                            listaRepository.save(lista);

                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(assembler.toModel(salva));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar lista",
            description = "Atualiza o nome ou o usuário responsável por uma lista."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Lista ou usuário não encontrado")
    })
    public ResponseEntity<EntityModel<Lista>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ListaRequest request) {

        return listaRepository.findById(id)
                .map(listaExistente -> {

                    listaExistente.setNome(
                            request.getNome()
                    );

                    if (!listaExistente.getUsuario().getId()
                            .equals(request.getUsuarioId())) {

                        return usuarioRepository
                                .findById(request.getUsuarioId())
                                .map(usuario -> {

                                    listaExistente.setUsuario(
                                            usuario
                                    );

                                    Lista atualizada =
                                            listaRepository.save(
                                                    listaExistente
                                            );

                                    return ResponseEntity.ok(
                                            assembler.toModel(
                                                    atualizada
                                            )
                                    );
                                })
                                .orElse(
                                        ResponseEntity
                                                .notFound()
                                                .build()
                                );
                    }

                    Lista atualizada =
                            listaRepository.save(
                                    listaExistente
                            );

                    return ResponseEntity.ok(
                            assembler.toModel(atualizada)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir lista",
            description = "Exclui uma lista pelo seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Lista excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada")
    })
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        if (!listaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        listaRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}