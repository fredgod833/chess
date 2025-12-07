package fr.fgodard.passivation;

public interface Serialiser<O> {
    String serialize(O value);
}
