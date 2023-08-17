package com.vodafone.hackathon;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class Test {
    public static Integer getData(String response, Integer a) {
        return 1;
    }

    public static void bulkDelete(String response, int hhId) throws IOException {

        ArrayList<LinkedHashMap<String, Object>> singleEventData = new ArrayList<>();
        ArrayList<LinkedHashMap<String, Object>> seriesEventData = new ArrayList<>();
        JsonObject root = JsonParser.parseString(response).getAsJsonObject();
        JsonArray array = root.get("result").getAsJsonObject().get("objects").getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            JsonObject cObject = array.get(i).getAsJsonObject();
            if (cObject.get("status").getAsString().equals("SCHEDULED") || cObject.get("status").getAsString().equals("RECORDED") || cObject.get("status").getAsString().equals("RECORDING")) {
                JsonObject metas = cObject.get("metaData").getAsJsonObject();
                Set<String> keySet = metas.keySet();
                if (keySet.contains("objectRecording")) {
                    //add data in Series
                    LinkedHashMap<String, Object> data = new LinkedHashMap<>();
                    data.put("TS", getTimeStamp(0));

                    data.put("DID", metas.get("DeviceID").getAsJsonObject().get("value").getAsString());
                    data.put("assetId", metas.get("assetId").getAsJsonObject().get("value").getAsString());
                    data.put("channelId", metas.get("channelId").getAsJsonObject().get("value").getAsString());
                    data.put("programId", metas.get("programId").getAsJsonObject().get("value").getAsString());
                    data.put("status", "deleted");
                    data.put("source", metas.get("source").getAsJsonObject().get("value").getAsString());
                    data.put("alreadyWatched", metas.get("alreadyWatched").getAsJsonObject().get("value").getAsString());
                    data.put("storage", "local");
                    data.put("ETID", 30);
                    data.put("EGID", 60);
                    data.put("UID", metas.get("UserID").getAsJsonObject().get("value").getAsString());
                    data.put("HHID", hhId);
                    data.put("OID", 185);
                    data.put("objectRecording", "series");
                    data.put("seriesID", metas.get("seriesID").getAsJsonObject().get("value").getAsString());
                    data.put("seriesName", metas.get("seriesName").getAsJsonObject().get("value").getAsString());
                    try {
                        data.put("seasonNumber", metas.get("seasonNumber").getAsJsonObject().get("value").getAsString());
                    } catch (Exception e) {
                        data.put("seasonNumber",null);
                    }
                    data.put("latestOccur", metas.get("latestOccur").getAsJsonObject().get("value").getAsString());
                    seriesEventData.add(data);

                } else {
                    //add data in Single event
                    LinkedHashMap<String, Object> data = new LinkedHashMap<>();
                    data.put("TS", getTimeStamp(0));
                    data.put("DID", metas.get("DeviceID").getAsJsonObject().get("value").getAsString());
                    data.put("assetId", metas.get("assetId").getAsJsonObject().get("value").getAsString());
                    data.put("channelId", metas.get("channelId").getAsJsonObject().get("value").getAsString());
                    data.put("programId", metas.get("programId").getAsJsonObject().get("value").getAsString());
                    data.put("status", "deleted");
                    try {
                        data.put("source", metas.get("source").getAsJsonObject().get("value").getAsString());
                    } catch (Exception e) {
                        data.put("source", "user");
                    }
                    try {
                        data.put("alreadyWatched", metas.get("alreadyWatched").getAsJsonObject().get("value").getAsString());
                    } catch (Exception r) {
                        data.put("alreadyWatched", "0");
                    }
                    data.put("storage", "local");
                    data.put("ETID", 30);
                    data.put("EGID", 60);
                    data.put("UID", metas.get("UserID").getAsJsonObject().get("value").getAsInt());
                    data.put("HHID", hhId);
                    data.put("OID", 185);
                    singleEventData.add(data);
                }

            }
        }
        JsonObject mainObj = new JsonObject();
        JsonArray batchArray = new JsonArray();
        for (LinkedHashMap<String, Object> data : singleEventData) {
            JsonObject jsonObject = new JsonObject();
            for (String key : data.keySet()) {
                if (data.get(key).getClass().getName().contains("Integer")) {
                    jsonObject.addProperty(key, Integer.valueOf((int) data.get(key)));
                } else {
                    jsonObject.addProperty(key, String.valueOf(data.get(key)));
                }
            }
            batchArray.add(jsonObject);
        }
        for (LinkedHashMap<String, Object> data : seriesEventData) {
            JsonObject jsonObject = new JsonObject();
            for (String key : data.keySet()) {

                if (data.get(key)!=null && data.get(key).getClass().getName().contains("Integer")) {
                    jsonObject.addProperty(key, Integer.valueOf((int) data.get(key)));
                } else {
                    jsonObject.addProperty(key, String.valueOf(data.get(key)));
                }
            }
            batchArray.add(jsonObject);
        }

        mainObj.add("batch", batchArray);

        Gson gson = new Gson();
        System.out.println(gson.toJson(mainObj));
        FileWriter file = new FileWriter("src/main/resources/output.json");
        file.write(gson.toJson(mainObj));


    }

    public static String getTimeStamp(Integer j) {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getTimeStamp(0));

    }
}
