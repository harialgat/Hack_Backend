package com.vodafone.hackathon.utils;

import com.vodafone.hackathon.runner.Configuration;
import dev.jeka.core.api.depmanagement.JkDependencySet;
import dev.jeka.core.api.depmanagement.JkRepo;
import dev.jeka.core.api.depmanagement.resolution.JkDependencyResolver;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.*;

public class JavaUtil {
    public static URLClassLoader classLoader;

    public static void resolveExternalDependencies(List<String> centralDependencies) {
        //I have to get these from dependency file;
        //  log.info("Resolve Dependencies function called ");
        List<Path> libraries = new ArrayList<>();

        JkDependencySet deps = JkDependencySet.of();
        Map<String, List<Path>> pathsMap = new HashMap<>();

        for (String artifact : centralDependencies) {
            deps = deps.and(artifact);
        }

        JkDependencyResolver resolver = JkDependencyResolver.of().addRepos(JkRepo.ofMavenCentral());

        for (String artifact : centralDependencies) {
            List<Path> libs = resolver.resolve(deps).getFiles().getEntries();
            libraries.addAll(libs);
            pathsMap.put(artifact, libs);
        }

        String cp = System.getProperty("java.class.path");

        for (String key : pathsMap.keySet()) {
            for (Path path : pathsMap.get(key)) {
                cp = cp + File.pathSeparator + path.toFile().getAbsolutePath();
            }
        }
        cp = cp + File.pathSeparator + Configuration.externalCodePath;
        System.setProperty("java.class.path", cp);
        try {
            compileJavaFiles(FileUtils.javaFiles);
        } catch (Exception e) {


        }
        try {
            urlClassLoader(libraries);
        } catch (Exception e) {

        }

    }

    public static void urlClassLoader(List<Path> libraries) throws MalformedURLException {

        List<URL> urls = new ArrayList<>();
        for (Path lib : libraries) {
            urls.add(lib.toUri().toURL());
        }
        urls.add(new File(Configuration.externalCodePath).toURI().toURL());
        classLoader = new URLClassLoader(urls.toArray(new URL[0]), ClassLoader.getSystemClassLoader());
    }

    public static void compileJavaFiles(List<File> javaFiles) {

        // Get the compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // Get the file system manager of the compiler
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        //fileManager.setLocation(StandardLocation.CLASS_PATH, Arrays.asList(files1));
        List<String> optionList = new ArrayList<String>();
        optionList.add("-classpath");
        optionList.add(System.getProperty("java.class.path"));

        // Create a compilation unit (files)
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(javaFiles);
        // A feedback object (diagnostic) to get errors
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        // Compilation unit can be created and called only once
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, optionList, null, compilationUnits);
        // The compile task is called
        task.call();
        int count = 0;

        // Printing of any compile problems
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            if (diagnostic.getLineNumber() < 0) {
                // log.warn("Compilation warning on line " + diagnostic.getLineNumber() + " in " + diagnostic.getMessage(Locale.ENGLISH));
            } else if (diagnostic.getMessage(Locale.ENGLISH).contains("lombok.javac.apt.LombokProcessor")) {
                // log.warn("Compilation warning on line " + diagnostic.getLineNumber() + " in lombok.javac.apt.LombokProcessor");
            } else {

                // log.error("Error on line " + diagnostic.getLineNumber() + " in " + diagnostic.getMessage(Locale.ENGLISH) + "--> File: " + diagnostic.getSource().toString());
            }
        }

        // Close the compile resources
        try {
            fileManager.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
