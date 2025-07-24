package com.example.upgradeagent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ProjectScanner {
    public static String loadSpringFiles(String rootPath) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (Stream<Path> stream = Files.walk(Paths.get(rootPath))) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java") || p.toString().endsWith(".xml")
                            || p.toString().endsWith(".yml") || p.toString().endsWith(".properties"))
                    .forEach(p -> {
                        try {
                            builder.append("\n--- ").append(p).append(" ---\n")
                                    .append(Files.readString(p)).append("\n");
                        } catch (IOException ignored) {
                        }
                    });
        }
        return builder.toString();
    }
}
