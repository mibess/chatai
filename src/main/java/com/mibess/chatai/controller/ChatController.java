package com.mibess.chatai.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import com.mibess.chatai.converter.MensagensConverter;
import com.mibess.chatai.domain.Chat;
import com.mibess.chatai.dto.MensagensDTO;
import com.mibess.chatai.dto.RespostaChatDTO;
import com.mibess.chatai.input.MensagemInput;
import com.mibess.chatai.service.ChatHistoricoService;
import com.mibess.chatai.service.ChatService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class ChatController {

    private ChatService chatService;
    private ChatHistoricoService chatHistoricoService;
    private MensagensConverter mensagensConverter;

    public ChatController(ChatService chatService, ChatHistoricoService chatHistoricoService,
            MensagensConverter mensagensConverter) {
        this.chatService = chatService;
        this.chatHistoricoService = chatHistoricoService;
        this.mensagensConverter = mensagensConverter;
    }

    @PostMapping("chat")
    @ResponseStatus(HttpStatus.OK)
    public RespostaChatDTO chat(@RequestBody MensagemInput mensagemInput) {

        return chatService.chat(mensagemInput, false);

    }

    @PostMapping("chat-stream")
    @ResponseStatus(HttpStatus.OK)
    public ResponseBodyEmitter chatStream(@RequestBody MensagemInput mensagemInput) {

        Flux<String> fluxResposta = chatService.chatStream(mensagemInput, true);

        return chatService.emitter(fluxResposta);

    }

    @GetMapping("chat/u/{chatId}")
    @ResponseStatus(HttpStatus.OK)
    public List<MensagensDTO> buscarMensagens(@PathVariable String chatId) {
        List<Chat> mensagens = chatHistoricoService.listarChats(chatId);

        return mensagens.stream().map(mensagensConverter::mensagensDTO).collect(Collectors.toList());
    }

    @DeleteMapping("chat/u/{chatId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarChat(@PathVariable String chatId) {

        chatHistoricoService.deletarChat(chatId);

    }
}
