package com.vodafone.hackathon.runner;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.vodafone.hackathon.jsonops.JsonUtils;
import com.vodafone.hackathon.pojos.*;
import com.vodafone.hackathon.report.ExtentReport;
import com.vodafone.hackathon.request.HttpOperation;
import com.vodafone.hackathon.responseutils.SaveUtils;
import com.vodafone.hackathon.responseutils.ValidationUtils;

import java.util.LinkedHashMap;
import java.util.List;

public class TestRunner {

    public static void runTest(Testcase testcase) throws Exception {
        System.out.println("running test:" + testcase.getTestCaseName());
        System.out.println("test Description:" + testcase.getTestCaseDescription());
        ExtentTest extentTest = ExtentReport.extent.createTest(testcase.getTestCaseName(), testcase.getTestCaseDescription());


        List<TestStep> steps = testcase.getSteps();
        for (int i = 0; i < steps.size(); i++) {

            TestStep currentStep = steps.get(i);
            ExtentTest extentStep =  extentTest.createNode(currentStep.getDescription());
            Request request = currentStep.getRequest();
            LinkedHashMap<String, String> headers = request.getHeaders();
            String body = null;
            if (request.getData() != null) {
                body = JsonUtils.resolveBody(request.getData());
            }
            if (headers == null) {
                headers = new LinkedHashMap<>();
            }
            String url = request.getUrl();
            extentStep.createNode("URL").log(Status.INFO, url);
            String method = request.getMethod();

            TestObject testObject = new TestObject();
            testObject.setHeaders(headers);
            extentStep.createNode("payload").log(Status.INFO, MarkupHelper.createCodeBlock(body));
            testObject.setBody(body);
            testObject.setUrl(url);
            testObject.setMethod(method);
            ResponseEntity actResponse = null;
            try {
                actResponse = HttpOperation.restCall(testObject);
            } catch (Exception e) {
                extentStep.createNode("Test Failed Cause ").log(Status.ERROR, e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            System.out.println("response code" + actResponse.getCode());
            System.out.println("response " + actResponse.getResponse());
            extentStep.createNode("Response").log(Status.INFO, MarkupHelper.createCodeBlock(actResponse.getResponse()));
            //validations
            Response expectedResponse = currentStep.getResponse();
            Validate validate = expectedResponse.getValidate();
            int expectedCode = validate.getCode();
            ExtentTest validationsExtent =  extentStep.createNode("Validations");

            try {
                //put first validation on responsse code
                ValidationUtils.validateByCode(actResponse.getCode(), expectedCode);
                validationsExtent.log(Status.PASS, "Response codes matched");
            } catch (Exception e) {
                validationsExtent.log(Status.FAIL, e.getMessage());
                throw new Exception(e.getMessage());
            }



            // check if validate by fields is there or not
            LinkedHashMap<String, ?> fields = validate.getFields();
            if (fields != null) {
                for (String key : fields.keySet()) {
                    try {
                        ValidationUtils.validateByField(actResponse.getResponse(), key, fields.get(key));
                        validationsExtent.log(Status.PASS, key);
                    } catch (Exception e) {

                        validationsExtent.log(Status.FAIL, "value to the expected field did not match" + key);
                        throw new Exception(e.getMessage());
                    }
                }
            }
            List<String> methodsValidation = validate.getMethods();
            if (methodsValidation != null && methodsValidation.size() > 0) {
                for (String methods : methodsValidation) {
                    try {
                        ValidationUtils.validateByMethod(methods);
                        validationsExtent.log(Status.PASS, methods);
                    } catch (Exception e) {
                        validationsExtent.log(Status.FAIL, "assertion error" + methods);
                        throw new Exception(e.getMessage());
                    }
                }
            }

            //save vars if any
            LinkedHashMap<String, String> valuesToBeSaved = expectedResponse.getSave();

            if (valuesToBeSaved != null) {
                ExtentTest saveVars =  extentStep.createNode("Save Variables");
                for (String key : valuesToBeSaved.keySet()) {
                    try {
                        Object out = SaveUtils.saveVars(actResponse.getResponse(), key, valuesToBeSaved.get(key));
                        saveVars.log(Status.INFO, key + ": " + out);
                    }catch (Exception e) {
                       saveVars.log(Status.FAIL, valuesToBeSaved.get(key)+ " "  +e.getMessage() );
                       throw new Exception(e.getMessage());
                    }
                }
            }


        }

    }
}
