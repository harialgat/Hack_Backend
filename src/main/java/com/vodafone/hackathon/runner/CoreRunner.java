package com.vodafone.hackathon.runner;

import com.vodafone.hackathon.pojos.Testcase;
import com.vodafone.hackathon.report.ExtentReport;
import com.vodafone.hackathon.utils.FileUtils;
import com.vodafone.hackathon.utils.JavaUtil;
import com.vodafone.hackathon.utils.YamlUtils;

import java.io.File;
import java.util.List;

public class CoreRunner {
    public static void main() {

        //resolve external Dependencies
     //   JavaUtil.resolveExternalDependencies(Configuration.externalDependencyPath);

        //collect the java files
        FileUtils.collectJavaFiles(Configuration.externalCodePath);

        //compile external Java files



        //collect test files
        FileUtils.collectTestFiles(Configuration.testFilePath);
        //parse test cases
        List<Testcase> tests = YamlUtils.parseTestCases(FileUtils.testFiles);

        //start the extent report
        ExtentReport.initiateReport();
        for (Testcase testcase : tests) {
            try {
                TestRunner.runTest(testcase);
            } catch (Exception e) {

            }
        }
        //flush the report
        ExtentReport.completeTheReport();

        FileUtils.deleteAllClassFiles(Configuration.externalCodePath);

    }
}
