package com.fgodard.passivation;

public interface Parser<I> {
    I parse(final String value);
}
