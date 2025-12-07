package com.fgodard.math.stats;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LinearRegContainer<T> {

    private record ObjectContainer<T>(T object, long quantity) {
    }

    private final ArrayList<ObjectContainer<T>> objectsDatas = new ArrayList<>();
    private final ArrayList<Long> reg = new ArrayList<>();
    private int breakIdx = 0;
    boolean dirty = true;

    public void clear() {
        this.objectsDatas.clear();
        dirty = true;
    }

    public void addObject(T object, long objectsNo) {
        this.objectsDatas.add(new ObjectContainer<>(object, objectsNo));
        dirty = true;
    }

    private void computeRegression(int desiredSize) {
        reg.clear();
        objectsDatas.sort(Comparator.comparingLong(o -> o.quantity));
        for (int i = 0; i < objectsDatas.size() - 1; i++) {
            reg.add(objectsDatas.get(i + 1).quantity - objectsDatas.get(i).quantity);
        }
        int count = reg.size();
        for (int i = 0; i < count - 1; i++) {
            if (reg.get(i + 1) < reg.get(i)) {
                breakIdx = i + 1;
                if ((count - i - 1) <= desiredSize) {
                    break;
                }
            }
        }
        dirty = false;
    }

    public List<T> getGreatests(int desiredSize) {
        List<T> result = new ArrayList<T>();
        if (objectsDatas.size() < 3) {
            for (ObjectContainer<T> oc : objectsDatas) {
                result.add(oc.object);
            }
            return result;
        }
        if (dirty) computeRegression(desiredSize);
        for (ObjectContainer<T> oc : objectsDatas.subList(breakIdx, objectsDatas.size())) {
            result.add(oc.object);
        }
        return result;
    }

    public List<T> getLowest() {
        List<T> result = new ArrayList<T>();
        if (dirty) {
            return result;
        }
        for (ObjectContainer<T> oc : objectsDatas.subList(0, breakIdx)) {
            result.add(oc.object);
        }
        return result;
    }

}
