package br.com.senac.apiseries.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusAcompanhamento {

    AGUARDANDO("Aguardando"),
    ASSISTINDO("Assistindo"),
    CONCLUIDA("Concluída"),
    DROPPADA("Dropada");

    private final String descricao;

    StatusAcompanhamento(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static StatusAcompanhamento fromString(String valor) {

        if (valor == null) {
            return null;
        }

        String normalizado = valor.trim().toLowerCase();

        if (normalizado.equals("aguardando")) {
            return AGUARDANDO;
        }

        if (normalizado.equals("assistindo")) {
            return ASSISTINDO;
        }

        if (normalizado.equals("concluida") ||
                normalizado.equals("concluída")) {
            return CONCLUIDA;
        }

        if (normalizado.equals("dropada")) {
            return DROPPADA;
        }

        throw new IllegalArgumentException(
                "Status de acompanhamento inválido: " + valor
        );
    }
}