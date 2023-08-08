package com.vodafone.hackathon.reflection;

import com.vodafone.hackathon.datalake.DataLake;
import com.vodafone.hackathon.utils.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class JavaMethodUtils {

    public static void methodInvoker(String expression) throws Exception {

        List<String> vars = StringUtils.getVarsFromMethod(expression);
        List<Object> actualVars = new ArrayList<>();
        String regexNumber = "[0-9]+";
        String regexstring = "\".*\""; //starts with double qoutes end with

        for (String s : vars) {
            if (s.matches(regexNumber)) {
                actualVars.add(Integer.parseInt(s));
            } else if (s.matches(regexstring)) {
               String s1 =  s.substring(s.indexOf("\"") + 1);
                actualVars.add(s1.substring(0,s1.indexOf("\"")));
            } else {
                actualVars.add(DataLake.dataLake.get(StringUtils.getStringBetween(s)));
            }
        }

        String className = StringUtils.getClassName(expression);

        //load the class
        Class<?> classs = Class.forName(className);

        //get the method name
        String methodName = StringUtils.getMethodName(expression);

        //methodParams

        Method[] methods = classs.getDeclaredMethods();
        Method finalMethod = null;

        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                //check all parameters

                if (m.getParameterCount() == actualVars.size()) {
                    boolean tempFlag = true;
                    Parameter[] params = m.getParameters();

                    for (int i = 0; i < params.length; i++) {

                        if (!params[i].getType().getName().equals(actualVars.get(i).getClass().getName())) {
                            tempFlag = false;
                            break;
                        }
                    }
                    if (tempFlag) {
                        finalMethod = m;
                        break;
                    }
                }


            }
        }
        if (finalMethod == null) {
            throw new NoSuchMethodException("method is not present");
        } else {
            finalMethod.invoke(null, actualVars.toArray());
        }


    }

    public static void main(String[] args) {

    }
}