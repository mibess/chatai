package com.mibess.chatai.exceptions;

public class ChatHistoricoException extends RuntimeException {

    public ChatHistoricoException(String message) {
        super(message);
    }

    public ChatHistoricoException(String message, Throwable cause) {
        super(message, cause);
    }

}
