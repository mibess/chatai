package com.mibess.chatai.infra;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequest;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import com.mibess.chatai.input.MensagemInput;
import com.mibess.chatai.service.PromptSystemService;

import reactor.core.publisher.Flux;

@Service
public class OpenAiService implements ChatAiService {
	private final ChatClient chatClient;
	private final ChatMemory chatMemory;

	private PromptSystemService promptSystemService;

	public OpenAiService(ChatClient chatClient, ChatMemory chatMemory, PromptSystemService promptSystemService) {
		this.chatClient = chatClient;
		this.chatMemory = chatMemory;
		this.promptSystemService = promptSystemService;
	}

	@Override
	public String mensagem(MensagemInput mensagemInput, boolean recordarMensagens) {
		ChatClientRequest chatRequest = criarChatClientRequest(mensagemInput);

		if (!recordarMensagens) {
			return chatRequest.call().content();
		} else {
			return chatRequest
					.advisors(new MessageChatMemoryAdvisor(chatMemory, mensagemInput.chatId(), 100))
					.call()
					.content();
		}
	}

	@Override
	public Flux<String> mensagemStream(MensagemInput mensagemInput, boolean recordarMensagens) {
		ChatClientRequest chatRequest = criarChatClientRequest(mensagemInput);
		if (!recordarMensagens) {
			return chatRequest.stream().content();
		} else {
			return chatRequest
					.advisors(new MessageChatMemoryAdvisor(chatMemory, mensagemInput.chatId(), 100))
					.stream()
					.content();
		}
	}

	public ChatClientRequest criarChatClientRequest(MensagemInput mensagemInput) {
		String prompt = promptSystemService.promptSystem(mensagemInput.usuario(), false);

		return chatClient.prompt().system(prompt).user(mensagemInput.mensagem());

	}
}
