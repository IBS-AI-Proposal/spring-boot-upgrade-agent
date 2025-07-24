package com.example.upgradeagent.internal;

public class InternalLLM {
    private static InternalLLM instance;

    private InternalLLM() {
        // private constructor
    }

    public static InternalLLM getInstance() {
        if (instance == null) {
            instance = new InternalLLM();
        }
        return instance;
    }

    public void triggerInternalLLMCall() {
        // TODO: Implement internal LLM logic here
        System.out.println("Internal LLM call triggered.");
    }
}

