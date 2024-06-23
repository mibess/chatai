package com.mibess.chatai.domain;

import lombok.Getter;

@Getter
public enum TipoMensagem {
    ROBO("Robô"),
    USUARIO("Usuário");

    private String tipo;

    TipoMensagem(String tipo) {
        this.tipo = tipo;
    }
}
