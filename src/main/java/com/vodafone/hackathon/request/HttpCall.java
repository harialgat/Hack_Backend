package com.vodafone.hackathon.request;

import com.vodafone.hackathon.pojos.Request;
import com.vodafone.hackathon.pojos.ResponseEntity;
import com.vodafone.hackathon.pojos.TestObject;

public interface HttpCall {

    ResponseEntity doPost(TestObject request);

    ResponseEntity doGet(TestObject request);

    ResponseEntity doPut(TestObject request);

    ResponseEntity doPatch(TestObject request);

    ResponseEntity doDelete(TestObject request);

}
