package com.example.upgradeagent.external.service;

import com.example.upgradeagent.common.PromptBuilder;
import com.example.upgradeagent.external.ExternalLLM;
import com.example.upgradeagent.internal.InternalLLM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class GPTService {

    @Autowired
    private Environment env;

    @Autowired
    private ExternalLLM externalLLM;

    @Autowired
    private InternalLLM internalLLM;

    public String getLatestOpenRewriteRecipe(final String version) {
        String prompt = String.format(PromptBuilder.prompt, version);
        String llmMode = env.getProperty("llm.mode", "external");
        if ("internal".equalsIgnoreCase(llmMode)) {
            return internalLLM.askGpt(prompt).trim();
        } else {
            return externalLLM.askGpt(prompt).trim();
        }
    }
}

