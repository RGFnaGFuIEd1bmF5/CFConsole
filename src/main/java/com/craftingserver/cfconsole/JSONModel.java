package com.craftingserver.cfconsole;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by buraktutanlar on 24/09/15.
 */
public class JSONModel {

    private static ObjectMapper objectMapper = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .setVisibility(Json);

    /**
     * @return JSON representation of object or null if JsonProcessingException caught.
     */
    public String toJSON() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            try {
                return objectMapper.writeValueAsString(e);
            } catch (JsonProcessingException ex) {
                return null;
            }
        }
    }

}