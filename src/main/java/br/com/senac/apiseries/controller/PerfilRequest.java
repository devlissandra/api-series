package br.com.senac.apiseries.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PerfilRequest {

    @NotBlank(message = "O nome de exibição é obrigatório")
    private String nomeExibicao;

    @NotNull(message = "O usuarioId é obrigatório")
    private Long usuarioId;

    public String getNomeExibicao() {
        return nomeExibicao;
    }

    public void setNomeExibicao(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}