package com.vodafone.hackathon.pojos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Testcase {
private String testCaseName;
private String testCaseDescription;
private List<TestStep> steps;
}
