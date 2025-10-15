package com.example.upgradeagent.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ProjectScanner {

    public static final Predicate<Path> PATH_PREDICATE = p -> p.toString().endsWith(".java")
            || p.toString().endsWith(".xml")
            || p.toString().endsWith(".yml")
            || p.toString().endsWith(".properties");

    public static String loadSpringFiles(final String rootPath) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (Stream<Path> stream = Files.walk(Paths.get(rootPath))) {
            stream.filter(Files::isRegularFile)
                    .filter(PATH_PREDICATE)
                    .forEach(p -> builder.append(formatFileContent(p)));
        }
        return builder.toString();
    }

    private static String formatFileContent(Path p) {
        try {
            return "\n--- " + p + " ---\n" + Files.readString(p) + "\n";
        } catch (IOException ignored) {
            return "";
        }
    }
}
