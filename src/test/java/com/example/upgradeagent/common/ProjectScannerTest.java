package com.example.upgradeagent.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectScannerTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Returns concatenated content of all matching files in the directory")
    @Test
    void returnsConcatenatedContentOfMatchingFiles() throws IOException {
        Path tempDir = Files.createTempDirectory("testDir");
        Path file1 = Files.createFile(tempDir.resolve("file1.java"));
        Path file2 = Files.createFile(tempDir.resolve("file2.xml"));
        Files.writeString(file1, "class Test {}");
        Files.writeString(file2, "<xml>Test</xml>");

        String result = ProjectScanner.loadSpringFiles(tempDir.toString());

        assertTrue(result.contains("--- " + file1 + " ---"));
        assertTrue(result.contains("class Test {}"));
        assertTrue(result.contains("--- " + file2 + " ---"));
        assertTrue(result.contains("<xml>Test</xml>"));
    }

    @DisplayName("Returns empty string when no matching files are found")
    @Test
    void returnsEmptyStringWhenNoMatchingFilesFound() throws IOException {
        Path tempDir = Files.createTempDirectory("testDir");
        Path file = Files.createFile(tempDir.resolve("file.txt"));
        Files.writeString(file, "This is a test file.");
        String result = ProjectScanner.loadSpringFiles(tempDir.toString());
        assertEquals("", result);
    }

    @DisplayName("Ignores files that cannot be read")
    @Test
    void ignoresFilesThatCannotBeRead() throws IOException {
        Path tempDir = Files.createTempDirectory("testDir");
        Path unreadableFile = Files.createFile(tempDir.resolve("unreadable.java"));
        Files.writeString(unreadableFile, "class Test {}");
        unreadableFile.toFile().setReadable(false);
        String result = ProjectScanner.loadSpringFiles(tempDir.toString());
        assertFalse(result.contains("class Test {}"));
    }

    @DisplayName("Throws exception when root path does not exist")
    @Test
    void throwsExceptionWhenRootPathDoesNotExist() {
        String invalidPath = "non/existent/path";
        assertThrows(IOException.class, () -> ProjectScanner.loadSpringFiles(invalidPath));
    }

}
