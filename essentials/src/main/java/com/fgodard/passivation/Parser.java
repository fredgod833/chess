package fr.fgodard.passivation;

public interface Parser<I> {
    I parse(final String value);
}
