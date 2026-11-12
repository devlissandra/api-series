package br.com.senac.apiseries.controller;

import br.com.senac.apiseries.assembler.PerfilModelAssembler;
import br.com.senac.apiseries.entity.Perfil;
import br.com.senac.apiseries.repository.PerfilRepository;
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
@RequestMapping("/perfis")
@Tag(
        name = "Perfis",
        description = "Operações de cadastro e gerenciamento dos perfis dos usuários."
)
public class PerfilController {

    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilModelAssembler assembler;

    public PerfilController(
            PerfilRepository perfilRepository,
            UsuarioRepository usuarioRepository,
            PerfilModelAssembler assembler) {

        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(
            summary = "Listar perfis",
            description = "Retorna todos os perfis cadastrados utilizando paginação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfis listados com sucesso")
    })
    public PagedModel<EntityModel<Perfil>> listar(
            Pageable pageable,
            PagedResourcesAssembler<Perfil> pagedResourcesAssembler) {

        Page<Perfil> perfis =
                perfilRepository.findAll(pageable);

        return pagedResourcesAssembler.toModel(
                perfis,
                assembler
        );
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar perfis por nome",
            description = "Pesquisa perfis pelo nome de exibição."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    public PagedModel<EntityModel<Perfil>> buscarPorNome(
            @RequestParam String nome,
            Pageable pageable,
            PagedResourcesAssembler<Perfil> pagedResourcesAssembler) {

        Page<Perfil> perfis =
                perfilRepository.findByNomeExibicaoContainingIgnoreCase(
                        nome,
                        pageable
                );

        return pagedResourcesAssembler.toModel(
                perfis,
                assembler
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar perfil por ID",
            description = "Retorna um perfil específico pelo seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado")
    })
    public ResponseEntity<EntityModel<Perfil>> buscarPorId(
            @PathVariable Long id) {

        return perfilRepository.findById(id)
                .map(perfil ->
                        ResponseEntity.ok(
                                assembler.toModel(perfil)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar perfil",
            description = "Cria um novo perfil vinculado a um usuário."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<?> cadastrar(
            @Valid @RequestBody PerfilRequest request) {

        var usuario =
                usuarioRepository.findById(
                        request.getUsuarioId()
                );

        if (usuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Perfil perfil = new Perfil();

        perfil.setNomeExibicao(
                request.getNomeExibicao()
        );

        perfil.setUsuario(usuario.get());

        Perfil novoPerfil =
                perfilRepository.save(perfil);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(novoPerfil));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar perfil",
            description = "Atualiza os dados e o usuário vinculado a um perfil."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Perfil ou usuário não encontrado")
    })
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PerfilRequest request) {

        return perfilRepository.findById(id)
                .map(perfilExistente -> {

                    var usuario =
                            usuarioRepository.findById(
                                    request.getUsuarioId()
                            );

                    if (usuario.isEmpty()) {
                        return ResponseEntity.notFound().build();
                    }

                    perfilExistente.setNomeExibicao(
                            request.getNomeExibicao()
                    );

                    perfilExistente.setUsuario(
                            usuario.get()
                    );

                    Perfil atualizado =
                            perfilRepository.save(
                                    perfilExistente
                            );

                    return ResponseEntity.ok(
                            assembler.toModel(atualizado)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir perfil",
            description = "Exclui um perfil pelo seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Perfil excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado")
    })
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        if (!perfilRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        perfilRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}