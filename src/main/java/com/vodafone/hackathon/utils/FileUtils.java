package com.vodafone.hackathon.utils;

import java.io.File;
import java.util.*;

public class FileUtils {
    public static List<File> testFiles =  new ArrayList<>();
    public static List<File> javaFiles =  new ArrayList<>();

    public static void collectJavaFiles(String path) {
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) return;

        for (File f : list) {
            if (f.isDirectory()) {
                collectJavaFiles(f.getAbsolutePath());
            } else {
                if(f.getName().contains(".java"))
                    javaFiles.add(f.getAbsoluteFile());
            }
        }
    }
    public static void collectTestFiles(String path){
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) return;

        for (File f : list) {
            if (f.isDirectory()) {
                collectTestFiles(f.getAbsolutePath());
            } else {
                if(f.getName().contains("test_"))
                testFiles.add(f.getAbsoluteFile());
            }
        }
    }

    public static void main(String[] args) {
        List<String> a =  new ArrayList<>();
        a.add("harish");
        List b =a;
        b.remove("harish");
        System.out.println(a.size());
    }


}
