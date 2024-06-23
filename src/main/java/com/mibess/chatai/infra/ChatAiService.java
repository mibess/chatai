package com.mibess.chatai.infra;

import com.mibess.chatai.input.MensagemInput;

import reactor.core.publisher.Flux;

public interface ChatAiService {

    String mensagem(MensagemInput mensagemInput, boolean recordarMensagens);

    Flux<String> mensagemStream(MensagemInput mensagemInput, boolean recordarMensagens);

}
