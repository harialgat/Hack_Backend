package com.vodafone.hackathon.pojos;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Validate {
    private int code;
    private LinkedHashMap<String,?> fields;
    private List<String> methods;

}
