package com.mibess.chatai.converter;

import org.springframework.stereotype.Component;

import com.mibess.chatai.dto.RespostaChatDTO;

@Component
public class RespostaChatConverter {

    public RespostaChatDTO respostaChatDTO(String mensagem) {
        return new RespostaChatDTO(mensagem);
    }

}
