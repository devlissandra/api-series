package br.com.senac.apiseries.repository;

import br.com.senac.apiseries.entity.ItemLista;
import br.com.senac.apiseries.entity.StatusAcompanhamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemListaRepository
        extends JpaRepository<ItemLista, Long> {

    Page<ItemLista> findByStatus(
            StatusAcompanhamento status,
            Pageable pageable
    );
}