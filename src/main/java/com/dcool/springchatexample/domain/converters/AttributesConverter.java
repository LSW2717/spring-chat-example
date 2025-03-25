package com.dcool.springchatexample.domain.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@Converter(autoApply = true)
public class AttributesConverter implements AttributeConverter<Attributes, String> {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public String convertToDatabaseColumn(Attributes attribute) {
        try {
            if (attribute == null) return null;

            if (attribute.hasText()) {
                return attribute.getText();
            } else {
                return mapper.writeValueAsString(attribute);
            }

        } catch(Exception e) {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Attributes convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) return null;
            return new Attributes(mapper.readValue(dbData, Map.class));
        } catch(Exception e) {
            return null;
        }
    }
}