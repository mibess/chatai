package com.mibess.chatai.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import com.mibess.chatai.converter.RespostaChatConverter;
import com.mibess.chatai.domain.TipoMensagem;
import com.mibess.chatai.dto.RespostaChatDTO;
import com.mibess.chatai.infra.OpenAiService;
import com.mibess.chatai.input.MensagemInput;

import reactor.core.publisher.Flux;

@Service
public class ChatService {

	private OpenAiService openAiService;
	private RespostaChatConverter respostaChatConverter;
	private ChatHistoricoService chatHistoricoService;

	public ChatService(
			OpenAiService openAiService,
			RespostaChatConverter respostaChatConverter,
			ChatHistoricoService chatHistoricoService) {
		this.openAiService = openAiService;
		this.respostaChatConverter = respostaChatConverter;
		this.chatHistoricoService = chatHistoricoService;
	}

	public RespostaChatDTO chat(MensagemInput mensagemInput, boolean recordarMensagens) {

		String resposta = openAiService.mensagem(mensagemInput, recordarMensagens);

		if (recordarMensagens) {
			chatHistoricoService.inserirChat(mensagemInput.chatId(), mensagemInput.mensagem(),
					TipoMensagem.USUARIO.toString());
			chatHistoricoService.inserirChat(mensagemInput.chatId(), resposta, TipoMensagem.ROBO.toString());
		}

		return respostaChatConverter.respostaChatDTO(resposta);
	}

	public Flux<String> chatStream(MensagemInput mensagemInput, boolean recordarMensagens) {

		Flux<String> fluxResposta = openAiService.mensagemStream(mensagemInput, recordarMensagens);
		String respostaSimples = fluxResposta.collectList()
				.map(list -> String.join("", list))
				.block();

		if (recordarMensagens) {
			chatHistoricoService.inserirChat(mensagemInput.chatId(), mensagemInput.mensagem(),
					TipoMensagem.USUARIO.toString());
			chatHistoricoService.inserirChat(mensagemInput.chatId(), respostaSimples, TipoMensagem.ROBO.toString());
		}

		return fluxResposta;
	}

	public ResponseBodyEmitter emitter(Flux<String> fluxResposta) {
		ResponseBodyEmitter emitter = new ResponseBodyEmitter();
		List<RespostaChatDTO> listaRespostas = new ArrayList<>();

		fluxResposta.subscribe(
				flux -> listaRespostas.add(new RespostaChatDTO(flux)),
				emitter::completeWithError,
				() -> {
					try {
						emitter.send(listaRespostas);
					} catch (Exception e) {
						emitter.completeWithError(e);
					} finally {
						emitter.complete();
					}
				});

		return emitter;
	}

	/* Exemplo de outras configurações */
	/*
	 * public Map<String, String> chatVariasRespostas(String mensagem) {
	 * Map<String, String> response = new HashMap<>();
	 * 
	 * Prompt prompt = new Prompt(
	 * Arrays.asList(
	 * new UserMessage(mensagem),
	 * new SystemMessage(
	 * "Você é um categorizador de produtos, retorne apenas as categorias, as respostas devem ser em formato de lista"
	 * )),
	 * OpenAiChatOptions.builder()
	 * .withN(3)
	 * .build());
	 * 
	 * List<String> mensagens = chatClient.prompt(prompt)
	 * .call()
	 * .contents()
	 * .stream().collect(Collectors.toList());
	 * 
	 * String resposta = mensagens.stream()
	 * .collect(Collectors.joining("_____"));
	 * 
	 * mensagens.stream().forEach((m) -> {
	 * System.out.println(m + "\n \n");
	 * });
	 * 
	 * response.put(
	 * "resposta",
	 * resposta);
	 * 
	 * return response;
	 * }
	 */
}