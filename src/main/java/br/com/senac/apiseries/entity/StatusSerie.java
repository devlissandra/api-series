package br.com.senac.apiseries.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusSerie {

    EM_ANDAMENTO("Em Andamento"),
    FINALIZADA("Finalizada"),
    DROPPADA("Dropada");

    private final String descricao;

    StatusSerie(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static StatusSerie fromString(String valor) {

        if (valor == null) {
            return null;
        }

        String normalizado = valor.trim().toLowerCase();

        if (normalizado.equals("em andamento")) {
            return EM_ANDAMENTO;
        }

        if (normalizado.equals("finalizada")) {
            return FINALIZADA;
        }

        if (normalizado.equals("dropada") ||
                normalizado.equals("cancelada")) {
            return DROPPADA;
        }

        throw new IllegalArgumentException("Status inválido: " + valor);
    }
}