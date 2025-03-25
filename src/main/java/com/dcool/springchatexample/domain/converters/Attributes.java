package com.dcool.springchatexample.domain.converters;

import java.util.HashMap;
import java.util.Map;


public class Attributes extends HashMap<String, Object> {

    private String text;

    public Attributes() {
        super();
    }

    public Attributes(String text) {
        super();
        this.text = text;
    }
    public Attributes(Map<? extends String, ? extends Object> map) {
        super(map);
    }

    public boolean hasText() {
        return text != null;
    }

    public String getText() {
        return text;
    }

}