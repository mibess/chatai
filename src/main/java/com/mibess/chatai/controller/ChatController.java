package com.mibess.chatai.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mibess.chatai.input.MensagemInput;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, String>> hostCheck(@RequestBody MensagemInput mensagemInput) {

        Map<String, String> response = new HashMap<>();

        response.put("hostcheck",
                "Ok v1 - Deployed at: " + new Date().toString() +
                        "Mensagem:" + mensagemInput.getMensagem());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("chat")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, String>> chat(@RequestBody MensagemInput mensagemInput) {

        Map<String, String> response = new HashMap<>();

        response.put(
                "resposta", chatClient.prompt()
                        .user(mensagemInput.getMensagem())
                        .call()
                        .content());

        return ResponseEntity.ok().body(response);
    }
}
