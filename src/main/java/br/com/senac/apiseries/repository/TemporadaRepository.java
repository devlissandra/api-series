package br.com.senac.apiseries.repository;

import br.com.senac.apiseries.entity.Temporada;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporadaRepository
        extends JpaRepository<Temporada, Long> {

    Page<Temporada> findBySerieId(
            Long serieId,
            Pageable pageable
    );
}