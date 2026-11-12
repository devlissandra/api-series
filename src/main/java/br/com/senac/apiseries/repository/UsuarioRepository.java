package br.com.senac.apiseries.repository;

import br.com.senac.apiseries.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Page<Usuario> findByNomeContainingIgnoreCase(
            String nome,
            Pageable pageable
    );
}