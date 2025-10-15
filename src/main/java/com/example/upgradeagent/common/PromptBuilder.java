package com.example.upgradeagent.common;

public class PromptBuilder {
    public static String buildUpgradePrompt(final String filesContent) {
        return """
                I want to upgrade my Spring Boot project to the latest stable version (e.g., 3.2.x).
                Here are the project files:
                
                %s
                
                Please:
                1. Identify the current Spring Boot version from pom.xml.
                2. Suggest changes in dependencies, plugins, or APIs.
                3. Recommend replacements for deprecated items (e.g., WebSecurityConfigurerAdapter).
                4. Propose OpenRewrite recipes if applicable.
                5. Summarize upgrade steps clearly.
                """.formatted(filesContent);
    }

    public static String latestOpenRewritePrompt(final String version) {
        return """
                I want to upgrade my Spring Boot project to the latest stable version which is ,  %s
                
                Please:
                1. Give me the correct, existing OpenRewrite recipe ID for upgrading to this version.
                2. This version should exist in official rewrite-spring module.
                3. Please avoid backticks, and formatting
                """.formatted(version);
    }

    public static String prompt =
                """
                Give me the correct, existing OpenRewrite recipe ID for upgrading to Spring Boot %s — 
                only if it exists in the official rewrite-spring module, no backticks, no formatting.
                
                """;

}

