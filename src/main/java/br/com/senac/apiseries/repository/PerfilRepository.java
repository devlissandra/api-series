package br.com.senac.apiseries.repository;

import br.com.senac.apiseries.entity.Perfil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Page<Perfil> findByNomeExibicaoContainingIgnoreCase(
            String nomeExibicao,
            Pageable pageable
    );
}