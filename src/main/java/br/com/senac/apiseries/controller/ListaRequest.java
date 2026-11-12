package br.com.senac.apiseries.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ListaRequest {

    @NotBlank(message = "O nome da lista é obrigatório")
    private String nome;

    @NotNull(message = "O usuarioId é obrigatório")
    private Long usuarioId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}