package com.example.upgradeagent;

import com.example.upgradeagent.external.ExternalGPT;
import com.example.upgradeagent.internal.InternalLLM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class SpringBootUpgradeAgent implements ApplicationRunner {

    @Autowired
    private Environment env;

    public static void main(final String[] args) {
        SpringApplication app = new SpringApplication(SpringBootUpgradeAgent.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        String llmMode = env.getProperty("llm.mode", "external");
        if ("internal".equalsIgnoreCase(llmMode)) {
            InternalLLM internalLLM = InternalLLM.getInstance();
            internalLLM.triggerInternalLLMCall();
        } else {
            ExternalGPT externalGPT = ExternalGPT.getInstance();
            externalGPT.triggerExternalGPTCall();
        }
    }
}
