package br.com.senac.apiseries.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class ItemLista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lista_id", nullable = false)
    @JsonIgnore
    @NotNull(message = "A lista é obrigatória")
    private Lista lista;

    @ManyToOne
    @JoinColumn(name = "serie_id", nullable = false)
    @NotNull(message = "A série é obrigatória")
    private Serie serie;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O status é obrigatório")
    private StatusAcompanhamento status;

    public ItemLista() {
    }

    public Long getId() {
        return id;
    }

    public Lista getLista() {
        return lista;
    }

    public void setLista(Lista lista) {
        this.lista = lista;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public StatusAcompanhamento getStatus() {
        return status;
    }

    public void setStatus(StatusAcompanhamento status) {
        this.status = status;
    }
}