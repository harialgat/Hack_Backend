package com.vodafone.hackathon.utils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {


    public static String getStringBetween(String expression) {
        if (expression.contains("'{")) {
            return expression.substring(expression.indexOf("'{") + 2, expression.indexOf("'}")).replaceAll(" ", "");
        } else if (expression.contains("{")) {
            return expression.substring(expression.indexOf("{") + 1, expression.indexOf("}")).replaceAll(" ", "");
        } else {
            return null;
        }
    }
//    public static List<String> getMethodParams(String expression){
//
//    }

    public static String getClassName(String expression) {
        String className = "";
        String tillMethod = expression.substring(0, expression.indexOf("("));

        String[] nameArr = tillMethod.split("\\.");

        for (int i = 0; i < nameArr.length - 1; i++) {
            if (i == 0) {
                className = nameArr[0];
            } else {
                className = className + "." + nameArr[i];
            }
        }
        return className;
    }

    public static String getMethodName(String expression) {
        String temp = expression.substring(0, expression.indexOf("("));
        String[] arr = temp.split("\\.");
        return arr[arr.length - 1];
    }

    public static String getFullyQualifiedMethod(String expression) {
        return expression.substring(0, expression.indexOf("("));
    }

    public static void main(String[] args) {
      String str =
              "com.vofaone.me(1,{ data})";
       // System.out.println(getMethodName(str));
        System.out.println(getVarsFromMethod(str));
    }

    public static List<String> getVarsFromMethod(String expression){
        expression =  expression.substring(expression.indexOf("(")+1,expression.indexOf(")"));
        String[]arr;
        if(expression.contains("\"{")&&expression.contains("}\"") || expression.contains("\"[")&&expression.contains("]\"")){
            arr = expression.split("response,");
        }else{
            arr = expression.split(",");
        }


        return Arrays.stream(arr).map(m ->m.replaceAll(" ","")).collect(Collectors.toList());
    }

}
