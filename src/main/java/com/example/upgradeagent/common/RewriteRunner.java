package com.example.upgradeagent.common;

import com.example.upgradeagent.exception.UpgradeFailedException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class RewriteRunner {

    public static void runRewrite(final String projectPath, final String recipe)
            throws IOException, InterruptedException, UpgradeFailedException {

        ProcessBuilder pb = new ProcessBuilder("mvn", "rewrite:run",
                "-Drewrite.activeRecipes=" + recipe);
        pb.directory(new File(projectPath));
        pb.inheritIO();
        Process process = pb.start();

        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("Process completed successfully.");
        } else {
            System.out.println("Process failed with exit code: " + exitCode);
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            StringBuilder message = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                message.append(line);
            }
            errorReader.close();
            throw new UpgradeFailedException(message.toString());
        }
    }
}

