package com.vodafone.hackathon.runner;

import com.vodafone.hackathon.pojos.Testcase;
import com.vodafone.hackathon.report.ExtentReport;
import com.vodafone.hackathon.utils.FileUtils;
import com.vodafone.hackathon.utils.YamlUtils;

import java.util.List;

public class CoreRunner {
    public static void main(String[] args) {
        //collect the java files


        //collect test files
        FileUtils.collectTestFiles(Configuration.testFilePath);
        //parse test cases
        List<Testcase> tests = YamlUtils.parseTestCases(FileUtils.testFiles);

        //start the extent report
        ExtentReport.initiateReport();
        for(Testcase testcase : tests){
            try {
                TestRunner.runTest(testcase);
            } catch (Exception e) {

            }
        }
        //flush the report

        ExtentReport.completeTheReport();

    }
}
