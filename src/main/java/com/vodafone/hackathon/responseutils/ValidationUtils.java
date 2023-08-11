package com.vodafone.hackathon.responseutils;


import com.vodafone.hackathon.datalake.DataLake;
import com.vodafone.hackathon.jsonops.JsonUtils;
import com.vodafone.hackathon.reflection.JavaMethodUtils;
import com.vodafone.hackathon.utils.StringUtils;
import org.testng.Assert;

public class ValidationUtils {
    public static void validateByField(String response, String key, Object expVal) throws  Exception{
        //check if the field has any dependent value from the datalake
        if (expVal.getClass().getName().equals("java.lang.String")) {
            String cString = String.valueOf(expVal);
            if ((cString.contains("{") || cString.contains("'{")) && ((cString.indexOf("{") == 0) || (cString.indexOf("'{") == 0))) {
                String var = StringUtils.getStringBetween(cString);
                expVal = DataLake.dataLake.get(var);
            }
        }
        Object actVal = JsonUtils.getJsonPathVal(response, key);
        if (!actVal.equals(expVal)) {
            try {
                Assert.fail("Values did not match " + "expected: " + expVal + " actual: " + actVal);
            } catch (AssertionError e){
                throw new RuntimeException(e.getMessage());
            }


        }
    }

    public static void validateByCode(int actCode, int expCode) throws Exception {

        if (actCode != expCode) {
            try {
                Assert.fail("Response Code Does Not Match");
            }catch (AssertionError e){
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    public static void validateByMethod(String expression, String response) throws AssertionError, RuntimeException {
        String s = expression.substring(expression.indexOf("(") + 1, expression.indexOf(")"));
        String[] arr = s.split(",");
        boolean responseFound = false;
        for (int i = 0; i < arr.length; i++) {

            if (arr[i].equals( "$")) {
                responseFound =  true;
                arr[i] = "\""+response+"\"";
            }
        }
        String toBeReplaced = "";
        for (int i = 0; i < arr.length; i++) {
            if (i != arr.length - 1) {
                if (responseFound)
                    toBeReplaced = toBeReplaced + arr[i] + "response,";
                else
                    toBeReplaced = toBeReplaced + arr[i] + ",";
            } else {
                toBeReplaced = toBeReplaced + arr[i] + ")";
            }
        }

        int replaceIndex = expression.indexOf("(");
        expression = expression.substring(0, replaceIndex + 1) + toBeReplaced;

        System.out.println("final expression " + expression);
        try {
            JavaMethodUtils.methodInvoker(expression);
        }catch (AssertionError e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
