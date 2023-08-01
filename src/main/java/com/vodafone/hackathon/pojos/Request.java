package com.vodafone.hackathon.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Request {
    private String url;
    private String method;
    private LinkedHashMap<String,?> headers;
    private LinkedHashMap<String, Object> data;
}
