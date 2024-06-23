package com.mibess.chatai.exceptions;

public class PromptSystemException extends RuntimeException {

    public PromptSystemException(String message) {
        super(message);
    }

    public PromptSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
