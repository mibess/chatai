package com.mibess.chatai.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.mibess.chatai.exceptions.PromptSystemException;

@Service
public class PromptSystemService {

    @Value("classpath:/files/jogos.csv")
    private Resource jogosResource;

    @Value("classpath:/prompts/liga-pes.st") // referencia de estudos https://www.youtube.com/watch?v=ZoPVGrB8iHU
    private Resource promptTemplateResource;

    public String promptSystem(String usuario, boolean lerArquivoDeJogos) {
        PromptTemplate promptTemplate = new PromptTemplate(promptTemplateResource);

        String partidas = lerArquivoDeJogos ? lerArquivoDeJogos() : "";

        Prompt prompt = promptTemplate.create(Map.of(
                "usuario", usuario,
                "partidas", partidas));

        return prompt.getContents();
    }

    private String lerArquivoDeJogos() {
        // futuramente criar uma função para buscar os jogos
        try {
            // Path path = Paths.get(new ClassPathResource(arquivo).getURI());

            Path path = jogosResource.getFile().toPath();

            return Files.lines(path).collect(Collectors.joining("\n"));

        } catch (Exception e) {
            throw new PromptSystemException("Erro ao carregar o arquivo! " + e.getMessage());
        }
    }

    private void salvarRespostaEmArquivo(String resposta, boolean gravarResposta) {
        if (!gravarResposta) {
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
            String timestamp = LocalDateTime.now().format(formatter);
            String fileName = "resposta-chat-" + timestamp + ".txt";

            Path directoryPath = Paths.get(new ClassPathResource("files/respostas").getURI());

            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = directoryPath.resolve(fileName);
            Files.writeString(filePath, resposta, StandardOpenOption.CREATE_NEW);
        } catch (Exception e) {
            throw new PromptSystemException("Erro ao salvar o arquivo! " + e.getMessage());
        }
    }
}
