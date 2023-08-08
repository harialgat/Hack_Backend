package com.vodafone.hackathon.reflection;

import com.vodafone.hackathon.utils.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

public class JavaMethodUtils {

    public static void methodInvoker(String expression) throws Exception {
        String className = StringUtils.getClassName(expression);

        //load the class
        Class<?> classs = Class.forName(className);

        //get the method name
        String methodName = StringUtils.getMethodName(expression);

        //methodParams

        List<Object> methodParams = new ArrayList<>();
        Method[] methods = classs.getDeclaredMethods();
        for(Method m :  methods){
             if(m.getName().equals(methodName)){
                 //check all parameters

             }
        }


    }


}
