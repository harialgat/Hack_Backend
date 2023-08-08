package com.vodafone.hackathon.request;

import com.vodafone.hackathon.pojos.ResponseEntity;
import com.vodafone.hackathon.pojos.TestObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class HttpOperation implements HttpCall {


    public ResponseEntity doPost(TestObject request) {
        StringBuffer jsonString = new StringBuffer();
        ResponseEntity responseEntity = new ResponseEntity();
        try {
            URL url = new URL(request.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(100000);
            Set<String> keys = request.getHeaders().keySet();

            for (String property : keys) {

                connection.setRequestProperty(property, request.getHeaders().get(property));
            }

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            writer.write(request.getBody());
            writer.close();
            int responseCode = connection.getResponseCode();
            responseEntity.setCode(responseCode);
            BufferedReader br = null;
            if (responseCode < 400) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
            responseEntity.setResponse(jsonString.toString());

            return responseEntity;
        } catch (Exception e) {
            throw new RuntimeException("post call failed");
        }


    }

    public ResponseEntity doGet(TestObject request) {
        StringBuffer jsonString = new StringBuffer();
        ResponseEntity responseEntity = new ResponseEntity();
        try {
            URL url = new URL(request.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(100000);
            Set<String> keys = request.getHeaders().keySet();

            for (String property : keys) {

                connection.setRequestProperty(property, request.getHeaders().get(property));
            }


            int responseCode = connection.getResponseCode();
            responseEntity.setCode(responseCode);
            BufferedReader br = null;
            if (responseCode < 400) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
            responseEntity.setResponse(jsonString.toString());

            return responseEntity;
        } catch (Exception e) {
            throw new RuntimeException("GET call failed");
        }
    }

    public ResponseEntity doPut(TestObject request) {
        StringBuffer jsonString = new StringBuffer();
        ResponseEntity responseEntity = new ResponseEntity();
        try {
            URL url = new URL(request.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setConnectTimeout(100000);
            Set<String> keys = request.getHeaders().keySet();

            for (String property : keys) {

                connection.setRequestProperty(property, request.getHeaders().get(property));
            }

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            writer.write(request.getBody());
            writer.close();
            int responseCode = connection.getResponseCode();
            responseEntity.setCode(responseCode);
            BufferedReader br = null;
            if (responseCode < 400) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
            responseEntity.setResponse(jsonString.toString());

            return responseEntity;
        } catch (Exception e) {
            throw new RuntimeException("put call failed");
        }
    }

    public ResponseEntity doPatch(TestObject request) {
        return null;
    }

    public ResponseEntity doDelete(TestObject request) {
        StringBuffer jsonString = new StringBuffer();
        ResponseEntity responseEntity = new ResponseEntity();
        try {
            URL url = new URL(request.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("DELETE");
            connection.setConnectTimeout(100000);
            Set<String> keys = request.getHeaders().keySet();

            for (String property : keys) {

                connection.setRequestProperty(property, request.getHeaders().get(property));
            }

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            writer.write(request.getBody());
            writer.close();
            int responseCode = connection.getResponseCode();
            responseEntity.setCode(responseCode);
            BufferedReader br = null;
            if (responseCode < 400) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
            responseEntity.setResponse(jsonString.toString());

            return responseEntity;
        } catch (Exception e) {
            throw new RuntimeException("delete call failed");
        }
    }
}
