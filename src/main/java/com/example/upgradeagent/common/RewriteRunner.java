package com.example.upgradeagent.common;

import java.io.File;
import java.io.IOException;

public class RewriteRunner {
    public static void runRewrite(String projectPath, String recipe) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("mvn", "rewrite:run",
                "-Drewrite.activeRecipes=" + recipe);
        pb.directory(new File(projectPath));
        pb.inheritIO();
        Process process = pb.start();
        process.waitFor();
    }
}

