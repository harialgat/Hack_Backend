package com.vodafone.hackathon.requestutils;

import com.vodafone.hackathon.datalake.DataLake;
import com.vodafone.hackathon.utils.StringUtils;

import java.util.LinkedHashMap;

public class ResolveRequest {

    public static LinkedHashMap<String, String> resolveHeaders(LinkedHashMap<String, String> headers) {
        if (headers == null) {
            return null;
        }
        LinkedHashMap<String, String> out = new LinkedHashMap<>();

        for (String key : headers.keySet()) {
            if (headers.get(key).getClass().getName().contains("String")) {
                String cString = String.valueOf(headers.get(key));
                if ((cString.contains("{") || cString.contains("'{")) && ((cString.indexOf("{") == 0) || (cString.indexOf("'{") == 0))) {
                    String var = StringUtils.getStringBetween(cString);
                    String cVal = String.valueOf(DataLake.dataLake.get(var));
                    out.put(key, cVal);

                } else {
                    out.put(key, headers.get(key));
                }
            } else {
                out.put(key, headers.get(key));
            }
        }
        return out;
    }

    public static String resolveUrl(String url) {
        return null;
    }
}
