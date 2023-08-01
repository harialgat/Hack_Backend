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
public class TestObject {
    private String method;
    private String body;
    private String url;
    private LinkedHashMap<String, Object> headers;
}
