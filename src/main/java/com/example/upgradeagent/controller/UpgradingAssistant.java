package com.example.upgradeagent.controller;

import com.example.upgradeagent.common.PomModifier;
import com.example.upgradeagent.common.ProjectScanner;
import com.example.upgradeagent.common.PromptBuilder;
import com.example.upgradeagent.common.RewriteRunner;
import com.example.upgradeagent.common.version.SpringBootVersionFetcher;
import com.example.upgradeagent.external.ExternalLLM;
import com.example.upgradeagent.external.service.GPTService;
import com.example.upgradeagent.internal.InternalLLM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class UpgradingAssistant {
    private static UpgradingAssistant instance;

    @Autowired
    private Environment env;

    @Autowired
    private  GPTService gptService;


    private UpgradingAssistant() {
        // private constructor to prevent instantiation
    }

    public static UpgradingAssistant getInstance() {
        if (instance == null) {
            instance = new UpgradingAssistant();
        }
        return instance;
    }

    public void run() throws Exception {
        String projectPath = env.getProperty("project.path"); // Read from application.yml

        // STEP 1 : Fetch the latest spring boot version from maven central
        String latestSpringBootVersion = SpringBootVersionFetcher.getLatestSpringBootVersion();
        System.out.println("Latest Spring Boot version: " + latestSpringBootVersion);

        // STEP 2 : Fetch the open rewrite recipe according to the springboot version
        //String latestOpenReWriteVersion = "org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_0";
        String latestOpenReWriteVersion = gptService.getLatestOpenRewriteRecipe(latestSpringBootVersion);

        // STEP 3 : Run OpenRewrite recipes (via shell)
        PomModifier.addRewritePlugin(projectPath, latestOpenReWriteVersion);
        RewriteRunner.runRewrite(projectPath, latestOpenReWriteVersion);

        // STEP 4 (Optional) : Git commit
        // GitCommitHelper.commitChanges(projectPath, "Upgrade Spring Boot via AI agent");

    }

}
