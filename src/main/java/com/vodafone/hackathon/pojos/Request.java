package com.vodafone.hackathon.pojos;

import java.util.LinkedHashMap;


public class Request {
    private String url;
    private String method;
    private LinkedHashMap<String,?> headers;
    private LinkedHashMap<String, Object> data;
}
