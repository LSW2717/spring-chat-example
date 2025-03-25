package com.dcool.springchatexample.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.hateoas.Link;

@Converter(autoApply = true)
public class LinkSetConverter implements AttributeConverter<Set<Link>, Byte> {

    protected Log logger = LogFactory.getLog(getClass());

    @Override
    public Byte convertToDatabaseColumn(Set<Link> attribute) {
//        logger.info("convertToDatabaseColumn: "+attribute);
        return null;
    }

    @Override
    public Set<Link> convertToEntityAttribute(Byte dbData) {
//        logger.info("convertToEntityAttribute: "+dbData);
        return new HashSet<>();
    }
}
