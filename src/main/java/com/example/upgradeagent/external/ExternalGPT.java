package com.example.upgradeagent.external;

import com.example.upgradeagent.common.PomModifier;
import com.example.upgradeagent.common.ProjectScanner;
import com.example.upgradeagent.common.PromptBuilder;
import com.example.upgradeagent.common.RewriteRunner;
import com.example.upgradeagent.common.version.SpringBootVersionFetcher;
import com.example.upgradeagent.external.service.GPTService;

public class ExternalGPT {

    private static final String PROJECT_PATH = "/Users/Vishnu/REPOS/AIDEMO/ecommerce-auditlog";
    private static final String OPENAI_API_KEY = "your_github_PAT";
    private static ExternalGPT instance;

    private ExternalGPT() {
        // private constructor to prevent instantiation
    }

    public static ExternalGPT getInstance() {
        if (instance == null) {
            instance = new ExternalGPT();
        }
        return instance;
    }

    public void triggerExternalGPTCall() throws Exception {

        String files = ProjectScanner.loadSpringFiles(PROJECT_PATH);
        String prompt = PromptBuilder.buildUpgradePrompt(files);

        //String suggestion = GPTService.getSpringBootVersionUpgradeSuggestions(OPENAI_API_KEY);
        //System.out.println("🧠 GPT Suggestions:\n" + suggestion);

        String latestVersion = SpringBootVersionFetcher.getLatestSpringBootVersion();
        System.out.println("Latest Spring Boot version: " + latestVersion);

        // Optional: Auto-edit pom.xml to upgrade Spring Boot version
        PomModifier.updateSpringBootVersion(PROJECT_PATH, latestVersion);
        String latestOpenReWriteVersion = GPTService.getLatestOpenRewriteRecipe(OPENAI_API_KEY, latestVersion);

        // Run OpenRewrite recipes (via shell)
        PomModifier.addRewritePlugin(PROJECT_PATH, latestOpenReWriteVersion);
        //PomModifier.addDependencyRewrite(PROJECT_PATH);
        //String latestOpenReWriteVersion = "org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_0";
        RewriteRunner.runRewrite(PROJECT_PATH, latestOpenReWriteVersion);

        // Optional: Git commit
        //GitCommitHelper.commitChanges(PROJECT_PATH, "Upgrade Spring Boot via AI agent");
        // Optional: apply changes using PomUpdater here
    }

}
