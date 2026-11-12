package br.com.senac.apiseries.repository;

import br.com.senac.apiseries.entity.Serie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    Page<Serie> findByTituloContainingIgnoreCase(
            String titulo,
            Pageable pageable
    );
}