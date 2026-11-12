package br.com.senac.apiseries.repository;

import br.com.senac.apiseries.entity.Lista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListaRepository extends JpaRepository<Lista, Long> {

    Page<Lista> findByUsuarioId(
            Long usuarioId,
            Pageable pageable
    );
}