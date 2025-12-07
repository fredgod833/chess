package com.fgodard.math.stats;

class ObjectContainer<T> {

    private T object;
    private long quantity;

    ObjectContainer(T object, long quantity) {
        this.object = object;
        this.quantity = quantity;
    }

    long getQuantity() {
        return quantity;
    }

    T getObject() {
        return object;
    }
}