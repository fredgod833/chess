package com.fgodard.math.stats;

import java.util.ArrayList;
import java.util.List;

public class NormalLawDataContainer<T> {

    private final ArrayList<Double> values = new ArrayList<>();
    private final ArrayList<T> objects = new ArrayList<>();
    private final ArrayList<Long> objectsNo = new ArrayList<>();
    private double sum = 0;
    private double mean = 0;
    private long quantity = 0;
    private double deviation;
    boolean dirty = true;

    public void addObject(T object, double value) {
        this.objects.add(object);
        this.objectsNo.add(1L);
        this.values.add(value);
        sum += value;
        quantity++;
        dirty = true;
    }

    public void addObject(T object, long objectsNo, double value) {
        this.objects.add(object);
        this.objectsNo.add(objectsNo);
        this.values.add(value);
        sum += value * objectsNo;
        quantity += objectsNo;
        dirty = true;
    }

    private void computeDeviation() {
        int n = values.size();
        if (n == 1) {
            mean = values.get(0);
            deviation = 0;
        } else {
            mean = sum / quantity;
            double sumSqrDist = 0;
            double d;
            for (int i = 0; i < n; i++) {
                d = values.get(i) - mean;
                sumSqrDist += Math.pow(d, 2) * objectsNo.get(i);
            }
            deviation = Math.sqrt(sumSqrDist / quantity);
        }
        dirty = false;
    }

    public double getMean() {
        if (dirty) computeDeviation();
        return mean;
    }

    public List<T> getGreaterThan(double k) {
        List<T> result = new ArrayList<>();
        if (values.isEmpty()) return result;
        if (dirty) computeDeviation();
        double limit = mean + k * deviation;
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) >= limit) {
                result.add(objects.get(i));
            }
        }
        return result;
    }

    public List<T> getLowerThan(double k) {
        List<T> result = new ArrayList<>();
        if (values.isEmpty()) return result;
        if (dirty) computeDeviation();
        double limit = mean + k * deviation;
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) <= limit) {
                result.add(objects.get(i));
            }
        }
        return result;
    }

    public List<T> getBetween(double k1, double k2) {
        List<T> result = new ArrayList<>();
        if (values.isEmpty()) return result;
        if (dirty) computeDeviation();
        double limit1 = mean + k1 * deviation;
        double limit2 = mean + k2 * deviation;
        double d;
        if (k2 < k1) {
            d = limit1;
            limit1 = limit2;
            limit2 = d;
        }
        for (int i = 0; i < values.size(); i++) {
            d = values.get(i);
            if (d > limit1 && d < limit2) {
                result.add(objects.get(i));
            }
        }
        return result;
    }

}
