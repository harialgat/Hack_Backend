package com.vodafone.hackathon.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.vodafone.hackathon.pojos.Testcase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YamlUtils {
    public static List<Testcase> parseTestCases(List<File> testFiles) {
        List<Testcase> testcases = new ArrayList<>();
        for (File f : testFiles) {
            testcases.add(getTestCase(f));
        }
        return testcases;
    }

    public static Testcase getTestCase(File currentFile) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        Testcase testcase = null;
        try {
            testcase = mapper.readValue(currentFile, Testcase.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return testcase;

    }
}
