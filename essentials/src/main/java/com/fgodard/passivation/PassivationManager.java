package com.fgodard.passivation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by crios on 02/11/23.
 */
public class PassivationManager {

    private Map<String,Parser> parserMap = new HashMap();

    private Map<String,Serialiser> serialiserMap = new HashMap();

    public <O> Parser<O> getParser(final String name) {
        return parserMap.get(name);
    }

    public <I> Serialiser<I> getSerialiser(final String name) {
        return serialiserMap.get(name);
    }

}
