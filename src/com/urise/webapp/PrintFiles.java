package com.urise.webapp;

import java.io.File;

public class PrintFiles {
    public static void main(String[] args) {
        File file = new File("src/com/urise/webapp");
        for (File files : file.listFiles()) {
            if (files.isDirectory()) {
                for (String fileName : files.list()) {
                    System.out.println(fileName);
                }
            } else {
                System.out.println(files.getName());
            }
        }
    }
}
