package br.com.senac.apiseries.controller;

import br.com.senac.apiseries.assembler.UsuarioModelAssembler;
import br.com.senac.apiseries.entity.Usuario;
import br.com.senac.apiseries.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usuarios")
@Tag(
        name = "Usuários",
        description = "Operações de cadastro, consulta, atualização e exclusão de usuários."
)
public class UsuarioController {

    private final UsuarioRepository repository;
    private final UsuarioModelAssembler assembler;

    public UsuarioController(
            UsuarioRepository repository,
            UsuarioModelAssembler assembler) {

        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(
            summary = "Listar usuários",
            description = "Retorna todos os usuários cadastrados utilizando paginação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso")
    })
    public PagedModel<EntityModel<Usuario>> listar(
            Pageable pageable,
            PagedResourcesAssembler<Usuario> pagedResourcesAssembler) {

        Page<Usuario> usuarios =
                repository.findAll(pageable);

        return pagedResourcesAssembler.toModel(
                usuarios,
                assembler
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna um usuário específico pelo seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<EntityModel<Usuario>> buscarPorId(
            @PathVariable Long id) {

        return repository.findById(id)
                .map(usuario ->
                        ResponseEntity.ok(
                                assembler.toModel(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar usuários por nome",
            description = "Pesquisa usuários pelo nome utilizando uma consulta personalizada e paginação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    public PagedModel<EntityModel<Usuario>> buscarPorNome(
            @RequestParam String nome,
            Pageable pageable,
            PagedResourcesAssembler<Usuario> pagedResourcesAssembler) {

        Page<Usuario> usuarios =
                repository.findByNomeContainingIgnoreCase(
                        nome,
                        pageable
                );

        return pagedResourcesAssembler.toModel(
                usuarios,
                assembler
        );
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar usuário",
            description = "Cria um novo usuário informando nome e e-mail."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<EntityModel<Usuario>> cadastrar(
            @Valid @RequestBody Usuario usuario) {

        Usuario novoUsuario =
                repository.save(usuario);

        return ResponseEntity
                .status(201)
                .body(assembler.toModel(novoUsuario));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza o nome e o e-mail de um usuário existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<EntityModel<Usuario>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuario) {

        return repository.findById(id)
                .map(usuarioExistente -> {

                    usuarioExistente.setNome(
                            usuario.getNome()
                    );

                    usuarioExistente.setEmail(
                            usuario.getEmail()
                    );

                    Usuario atualizado =
                            repository.save(usuarioExistente);

                    return ResponseEntity.ok(
                            assembler.toModel(atualizado)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir usuário",
            description = "Exclui um usuário pelo seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
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