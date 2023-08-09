package com.vodafone.hackathon.responseutils;

import com.vodafone.hackathon.datalake.DataLake;
import com.vodafone.hackathon.jsonops.JsonUtils;
import com.vodafone.hackathon.reflection.JavaMethodUtils;

public class SaveUtils {
    public static void saveVars(String response, String key, String expression) throws Exception {
        //either json path
        //or method
        if (expression.contains("$") && (expression.indexOf("$") == 0)) {
            Object res = JsonUtils.getJsonPathVal(response, expression);
            DataLake.dataLake.put(key, res);
        } else {
            //update the method parameters if it contains $ as method parameter
            String s = expression.substring(expression.indexOf("(") + 1, expression.indexOf(")"));
            String[] arr = s.split(",");
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == "$") {
                    arr[i] = response;
                }
            }
            String toBeReplaced = "";
            for (int i = 0; i < arr.length; i++) {
                if (i != arr.length - 1) {
                    toBeReplaced = toBeReplaced + arr[i] + ",";
                } else {
                    toBeReplaced = toBeReplaced + arr[i] + ")";
                }
            }

            int replaceIndex = expression.indexOf("(");
            expression = expression.substring(0, replaceIndex + 1) + toBeReplaced;

            Object out = JavaMethodUtils.methodInvoker(expression);
            DataLake.dataLake.put(key, out);
        }
    }
}
