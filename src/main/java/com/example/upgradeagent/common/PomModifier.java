package com.example.upgradeagent.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PomModifier {
    public static void updateSpringBootVersion(final String projectPath, final String newVersion) throws IOException {
        Path pom = Paths.get(projectPath, "pom.xml");
        String xml = Files.readString(pom);
        String updated = xml.replaceAll(
                "(<parent>\\s*<groupId>org.springframework.boot</groupId>\\s*<artifactId>spring-boot-starter-parent</artifactId>\\s*<version>)(.*?)(</version>)",
                "$1" + newVersion + "$3"
        );
        Files.writeString(pom, updated);
    }

    public static void addRewritePlugin(final String projectPath, final String recipe) throws IOException {
        Path pom = Paths.get(projectPath, "pom.xml");
        String xml = Files.readString(pom);

        // Check if the plugin is already present
        if (!xml.contains("<artifactId>rewrite-maven-plugin</artifactId>")) {
            // Plugin configuration to add
            String pluginConfig =
                    "    <plugin>\n" +
                            "   <groupId>org.openrewrite.maven</groupId>\n" +
                            "      <artifactId>rewrite-maven-plugin</artifactId>\n" +
                            "      <version>6.12.0</version>\n" +
                            "         <configuration>\n" +
                            "           <activeRecipes>\n" +
                            "             <recipe>" + recipe + "</recipe>\n" +
                            "           </activeRecipes>\n" +
                            "        </configuration>\n" +
                            "      <dependencies>\n" +
                            "         <dependency>\n" +
                            "           <groupId>org.openrewrite.recipe</groupId>\n" +
                            "           <artifactId>rewrite-spring</artifactId>\n" +
                            "           <version>6.9.0</version>\n" +
                            "       </dependency>\n" +
                            "      </dependencies>\n" +
                            "</plugin>\n";

            // Locate the <plugins> section and insert the plugin
            xml = xml.replaceFirst(
                    "(<plugins>\\s*)",
                    "$1" + pluginConfig
            );

            // Write the updated content back to the pom.xml
            Files.writeString(pom, xml);
        }
    }

    public static void addDependencyRewrite(final String projectPath) throws IOException {
        Path pom = Paths.get(projectPath, "pom.xml");
        String xml = Files.readString(pom);

        // Check if the dependency is already present
        if (!xml.contains("<artifactId>org.openrewrite.recipe</artifactId>")) {
            // Dependency configuration to add
            String dependencyConfig =
                    "    <dependency>\n" +
                            "      <groupId>org.openrewrite.recipe</groupId>\n" +
                            "      <artifactId>rewrite-spring</artifactId>\n" +
                            "      <version>5.13.2</version>\n" +
                            "    </dependency>\n";

            // Locate the <dependencies> section and insert the dependency
            xml = xml.replaceFirst(
                    "(<dependencies>\\s*)",
                    "$1" + dependencyConfig
            );

            // Write the updated content back to the pom.xml
            Files.writeString(pom, xml);
        }
    }
}



