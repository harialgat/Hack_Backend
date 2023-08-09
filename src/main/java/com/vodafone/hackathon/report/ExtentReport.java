package com.vodafone.hackathon.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.util.ArrayList;

public class ExtentReport {
    public static ExtentReports extent;
    public static ExtentTest test;
    public static ExtentHtmlReporter htmlReporter;

    /**
     * Start Point for Extent Report
     */
    public static void initiateReport() {
        extent = new ExtentReports();
        htmlReporter = new ExtentHtmlReporter("src/main/resources/reports.html");
        extent.attachReporter(htmlReporter);
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setDocumentTitle("Extent Report");
        htmlReporter.config().setReportName("Test Report");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.STANDARD);

    }

    /**
     * complete the report once the all testcase execution is over
     */
    public static void completeTheReport() {
        extent.flush();
    }


}
