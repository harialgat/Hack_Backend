package com.vodafone.hackathon.responseutils;

import com.vodafone.hackathon.datalake.DataLake;
import com.vodafone.hackathon.jsonops.JsonUtils;
import com.vodafone.hackathon.reflection.JavaMethodUtils;

public class SaveUtils {
    public static Object saveVars(String response, String key, String expression) throws Exception {
        //either json path
        //or method
        Object saved = null;
        if (expression.contains("$") && (expression.indexOf("$") == 0)) {
            Object res = JsonUtils.getJsonPathVal(response, expression);
            saved =  res;
            DataLake.dataLake.put(key, res);
        } else {
            //update the method parameters if it contains $ as method parameter
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
            Object out = JavaMethodUtils.methodInvoker(expression);
            saved = out;
            DataLake.dataLake.put(key, out);
        }
        return saved;
    }
}
