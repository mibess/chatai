package com.mibess.chatai.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mibess.chatai.domain.Chat;
import com.mibess.chatai.exceptions.ChatHistoricoException;
import com.mibess.chatai.repository.ChatRepository;

@Service
public class ChatHistoricoService {

    private ChatRepository chatRepository;

    public ChatHistoricoService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Transactional
    public void inserirChat(String chatId, String mensagem, String tipo) {
        Chat chat = Chat.builder()
                .chatId(chatId)
                .mensagem(mensagem)
                .tipoMensagem(tipo)
                .build();

        chatRepository.save(chat);
    }

    @Transactional
    public void deletarChat(String chatId) {
        List<Chat> chats = listarChats(chatId);

        chatRepository.deleteAll(chats);
    }

    public List<Chat> listarChats(String chatId) {
        return chatRepository.findByChatId(chatId).orElseThrow(() -> {
            throw new ChatHistoricoException(String.format("Lista não encontrada para o chat: %s", chatId));
        });
    }

}
