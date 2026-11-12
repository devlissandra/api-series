package br.com.senac.apiseries.controller;

import br.com.senac.apiseries.entity.StatusAcompanhamento;
import jakarta.validation.constraints.NotNull;

public class ItemListaRequest {

    @NotNull(message = "O listaId é obrigatório")
    private Long listaId;

    @NotNull(message = "O serieId é obrigatório")
    private Long serieId;

    @NotNull(message = "O status é obrigatório")
    private StatusAcompanhamento status;

    public Long getListaId() {
        return listaId;
    }

    public void setListaId(Long listaId) {
        this.listaId = listaId;
    }

    public Long getSerieId() {
        return serieId;
    }

    public void setSerieId(Long serieId) {
        this.serieId = serieId;
    }

    public StatusAcompanhamento getStatus() {
        return status;
    }

    public void setStatus(StatusAcompanhamento status) {
        this.status = status;
    }
}