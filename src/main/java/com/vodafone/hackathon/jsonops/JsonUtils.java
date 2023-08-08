package com.vodafone.hackathon.jsonops;

import com.jayway.jsonpath.JsonPath;

import java.util.LinkedHashMap;
import java.util.Set;

public class JsonUtils {

    public static String updateJsonString(String currentString, String jsonPath, String newValue) {
        return JsonPath.parse(currentString).set(jsonPath, newValue).jsonString();
    }

    public static String resolveBody(LinkedHashMap<String, Object> data) {
        Set<String> keySet = data.keySet();
        return null;
    }
}
