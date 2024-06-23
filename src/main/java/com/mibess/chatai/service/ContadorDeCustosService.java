package com.mibess.chatai.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.ModelType;

@Service
public class ContadorDeCustosService {

    public int qntTokens(String tokens) {
        var registry = Encodings.newDefaultEncodingRegistry();
        var enc = registry.getEncodingForModel(ModelType.GPT_4);
        return enc.countTokens(tokens);
    }

    public BigDecimal custos(String tokens, String valorPorToken) {
        return new BigDecimal(
                qntTokens(tokens))
                .divide(new BigDecimal("1000"))
                .multiply(new BigDecimal(valorPorToken));

    }
}
