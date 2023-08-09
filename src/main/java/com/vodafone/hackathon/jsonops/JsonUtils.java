package com.vodafone.hackathon.jsonops;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.vodafone.hackathon.datalake.DataLake;
import com.vodafone.hackathon.reflection.JavaMethodUtils;
import com.vodafone.hackathon.utils.StringUtils;

import net.minidev.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;

import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Set;

public class JsonUtils {

    public static String updateJsonString(String currentString, String jsonPath, Object newValue) {
        return JsonPath.parse(currentString).set(jsonPath, newValue).jsonString();
    }

    public static String resolveBody(LinkedHashMap<String, Object> data) throws Exception {
        Set<String> keySet = data.keySet();
        String path = String.valueOf(data.get("$"));
        path = path.substring(path.indexOf("payload:") + 9).replaceAll("\"", "");
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(path));
        String root = jsonObject.toJSONString();

        for (String key : keySet) {
            if (key == "$") {
                continue;
            } else {
                //resolve the variables
                Object currentVal = data.get(key);
                if (currentVal.getClass().getName().equals("java.lang.String")) {
                    String cString = String.valueOf(currentVal);
                    if (cString.contains("method:")) {
                        Object result = JavaMethodUtils.methodInvoker(cString.substring(cString.indexOf("method:") + 7));
                        root = updateJsonString(root, key, result);
                    } else if ((cString.contains("{") || cString.contains("'{")) && ((cString.indexOf("{") == 0) || (cString.indexOf("'{") == 0))) {
                        //resolve varibales
                        String var = StringUtils.getStringBetween(cString);
                        root = updateJsonString(root, key, DataLake.dataLake.get(var));
                    } else {
                        root = updateJsonString(root, key, cString);
                    }
                } else {
                    root = updateJsonString(root, key, currentVal);
                }
            }
        }

        return root;
    }

    public static void main(String[] args) {
        DataLake.dataLake.put("var", 123);
        LinkedHashMap<String, Object> keyVal = new LinkedHashMap<>();
        keyVal.put("$", "payload: src/main/resources/test.json");
        keyVal.put("$.user", "method:com.vodafone.hackathon.jsonops.JsonUtils.dum(1)");
        try {
            System.out.println(resolveBody(keyVal));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int dum(Integer a) {
        return a;
    }

    public static Object getJsonPathVal(String response, String jsonPath){

            Configuration config = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS).build();
            Object value = JsonPath.using(config).parse(response).read(jsonPath);
            if(value != null) {
                if(value.getClass().getTypeName().contains("JSONArray")) {
                    JSONArray jsonArray = (JSONArray) value;
                    if(jsonArray.size() == 1)
                        value = jsonArray.get(0);
                    else {
                        System.out.println(value);
                        value = null;
                        Assert.fail("Expected object should not be an array");
                    }
                }
            } else
                value = "null";
            return value;

    }
}
