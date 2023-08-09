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

    public static void validateByMethod(String method) throws Exception {
        try {
            JavaMethodUtils.methodInvoker(method);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
