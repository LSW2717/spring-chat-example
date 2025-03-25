package com.dcool.springchatexample.domain.converters;

import java.util.ArrayList;
import java.util.Collection;

public class AttributesSet extends ArrayList<Object>{
  
    private String text;

    public AttributesSet() {
        super();
    }

    public AttributesSet(String text) {
        super();
        this.text = text;
    }
    public AttributesSet(Collection<? extends Object> collection) {
        super(collection);
    }

    public boolean hasText() {
        return text != null;
    }

    public String getText() {
        return text;
    }
}
