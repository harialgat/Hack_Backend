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
public class Response {
    private Validate validate;
    private LinkedHashMap<String, String> save;
}
