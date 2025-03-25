package com.dcool.springchatexample.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.hateoas.Link;

@Converter(autoApply = true)
public class LinkConverter implements AttributeConverter<Link, String> {

    protected Log logger = LogFactory.getLog(getClass());

    protected final Link convertToEntityAttribute = Link.of(".").withSelfRel();
    protected final String convertToDatabaseColumn = ".";//Link.of(".").withSelfRel();


    @Override
    public String convertToDatabaseColumn(Link attribute) {
//        logger.info("convertToDatabaseColumn: "+attribute);
        return attribute != null ? attribute.getHref() : null;
    }

    @Override
    public Link convertToEntityAttribute(String dbData) {
//        logger.info("convertToEntityAttribute: "+dbData);
        return dbData != null ? Link.of(dbData).withSelfRel() : null;// convertToEntityAttribute;//link;//Link.of(".").withSelfRel();
    }

//    public static <T> T convert(){
//        try {
//            String last = UriComponentsBuilder.fromUriString(uri).build().getPathSegments().stream().reduce((first, second) -> second).orElse(null);
//            this.seq = Long.parseLong(last);
//        }catch(Exception e) {
//        }
//
//    }
}
