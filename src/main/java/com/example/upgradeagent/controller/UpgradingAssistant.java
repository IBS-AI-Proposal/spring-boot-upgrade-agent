package com.example.upgradeagent.controller;

import com.example.upgradeagent.common.PomModifier;
import com.example.upgradeagent.common.RewriteRunner;
import com.example.upgradeagent.common.version.SpringBootVersionFetcher;
import com.example.upgradeagent.service.GPTService;
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

    @Autowired
    private SpringBootVersionFetcher springBootVersionFetcher;


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
        String latestSpringBootVersion = springBootVersionFetcher.getLatestSpringBootVersion();
        System.out.println("Latest Spring Boot version: " + latestSpringBootVersion);

        // STEP 2 : Fetch the open rewrite recipe according to the springboot version
        //String latestOpenReWriteRecipe = "org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_0";
        String latestOpenReWriteRecipe = gptService.getLatestOpenRewriteRecipe(latestSpringBootVersion);
        System.out.println("Latest Open ReWrite Recipe: " + latestOpenReWriteRecipe);


        // STEP 3 : Update the pom with Open rewrite recipe
        PomModifier.addRewritePlugin(projectPath, latestOpenReWriteRecipe);
        try {
            // STEP 4 : Run the app with open rewrite command
            RewriteRunner.runRewrite(projectPath, latestOpenReWriteRecipe);
        } catch (Exception e) {
            // Upgrade failed, let's send the stack trace to LLM and fix it
        }

        // STEP 4 (Optional) : Git commit
        // GitCommitHelper.commitChanges(projectPath, "Upgrade Spring Boot via AI prompt chaining");

    }

}
