package com.vodafone.hackathon.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TestStep {
    private Request request;
    private Response response;
    private String description;
}
