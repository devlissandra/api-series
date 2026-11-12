package br.com.senac.apiseries.controller;

import jakarta.validation.constraints.NotNull;

public class TemporadaRequest {

    @NotNull(message = "O número da temporada é obrigatório")
    private Integer numero;

    @NotNull(message = "A quantidade de episódios é obrigatória")
    private Integer quantidadeEpisodios;

    @NotNull(message = "O serieId é obrigatório")
    private Long serieId;

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getQuantidadeEpisodios() {
        return quantidadeEpisodios;
    }

    public void setQuantidadeEpisodios(Integer quantidadeEpisodios) {
        this.quantidadeEpisodios = quantidadeEpisodios;
    }

    public Long getSerieId() {
        return serieId;
    }

    public void setSerieId(Long serieId) {
        this.serieId = serieId;
    }
}