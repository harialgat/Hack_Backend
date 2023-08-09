package com.vodafone.hackathon.reflection;

import com.vodafone.hackathon.datalake.DataLake;
import com.vodafone.hackathon.utils.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class JavaMethodUtils {

    public static Object methodInvoker(String expression) throws Exception {

        List<String> vars = StringUtils.getVarsFromMethod(expression);
        List<Object> actualVars = new ArrayList<>();
        String regexNumberInteger = "[0-9]+";
        String regexFloat = "[0-9\\.]+";

        String regexstring = "\".*\""; //starts with double qoutes end with
        String regexInputResponse = "[{]{1}[a-zA-Z0-9\\\\s\\p{P}\\p{S}]*[}]{1}";
        for (String s : vars) {
            if (s.matches(regexNumberInteger)) {
                actualVars.add(Integer.parseInt(s));
            } else if (s.matches(regexFloat)) {
                actualVars.add(Float.parseFloat(s));
            } else if (s.matches(regexInputResponse)) {
                actualVars.add(s);
            } else if (s.matches(regexstring)) {
                String s1 = s.substring(s.indexOf("\"") + 1);
                actualVars.add(s1.substring(0, s1.indexOf("\"")));
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
        Object result = null;
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
            result = finalMethod.invoke(null, actualVars.toArray());
        }

        return result;
    }

    public static void main(String[] args) {

    }
}