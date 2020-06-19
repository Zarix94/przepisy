package com.przepisy.przepisy.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class Result {
    private boolean result;
    private String errorsJson;
    private ArrayList<Error> errors;

    public Result(){
        result = false;
        errors = new ArrayList<>();
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setError(Error error){
        result = false;
        this.errors.add(error);
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }

        return jsonString;
    }
}
