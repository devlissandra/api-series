package br.com.senac.apiseries.controller;

import br.com.senac.apiseries.entity.StatusSerie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class SerieRequest {

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "O país de origem é obrigatório")
    private String paisOrigem;

    @NotNull(message = "O ano de lançamento é obrigatório")
    private Integer anoLancamento;

    @NotNull(message = "O status é obrigatório")
    private StatusSerie status;

    private List<Long> categoriaIds;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPaisOrigem() {
        return paisOrigem;
    }

    public void setPaisOrigem(String paisOrigem) {
        this.paisOrigem = paisOrigem;
    }

    public Integer getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(Integer anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public StatusSerie getStatus() {
        return status;
    }

    public void setStatus(StatusSerie status) {
        this.status = status;
    }

    public List<Long> getCategoriaIds() {
        return categoriaIds;
    }

    public void setCategoriaIds(List<Long> categoriaIds) {
        this.categoriaIds = categoriaIds;
    }
}