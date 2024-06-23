package com.mibess.chatai.converter;

import org.springframework.stereotype.Component;

import com.mibess.chatai.domain.Chat;
import com.mibess.chatai.dto.MensagensDTO;

@Component
public class MensagensConverter {

    public MensagensDTO mensagensDTO(Chat chat) {
        return new MensagensDTO(
                chat.getMensagem(),
                chat.getTipoMensagem());

    }

}
