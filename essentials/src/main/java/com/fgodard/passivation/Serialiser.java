package com.fgodard.passivation;

public interface Serialiser<O> {
    String serialize(O value);
}
