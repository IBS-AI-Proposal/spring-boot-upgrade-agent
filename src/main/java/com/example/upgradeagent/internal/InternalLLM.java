package com.example.upgradeagent.internal;


import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Repository;

@Repository
public class InternalLLM {

    private final OllamaChatModel chatModel;

    public InternalLLM(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String askGpt(String prompt) {
        return this.chatModel.call(prompt);
    }
}

