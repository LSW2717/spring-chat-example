package com.dcool.springchatexample.domain.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Set;

@Converter(autoApply = true)
public class StringListConverter implements AttributeConverter<Set<String>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set<String> dataList) {
        try {
            return mapper.writeValueAsString(dataList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> convertToEntityAttribute(String data) {
        try {
            return mapper.readValue(data, Set.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}