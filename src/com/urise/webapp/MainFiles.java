package com.urise.webapp;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class MainFiles {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get(".\\src\\com\\urise\\webapp");
        Files.walkFileTree(path, new Visit());
    }

    static class Visit implements FileVisitor<Path> {
        String str = "\t";
        int counter = 0;

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            System.out.println(str.repeat(counter) + dir.getFileName());
            counter++;
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            System.out.println("\t".repeat(counter) + file.getFileName());
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            counter--;
            return FileVisitResult.CONTINUE;
        }
    }
}

