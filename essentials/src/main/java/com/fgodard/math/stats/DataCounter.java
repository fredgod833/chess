package com.fgodard.math.stats;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class DataCounter<T> {

    private final HashMap<T, BigDecimal> dataSet = new HashMap<>();

    private BigDecimal sum = BigDecimal.ZERO;

    public void add(T data) {
        dataSet.merge(data, BigDecimal.ONE, BigDecimal::add);
        sum = sum.add(BigDecimal.valueOf(1));
    }

    public <U extends Number & Comparable<U>> void add(T data, U count) {
        BigDecimal value = dataSet.get(data);
        BigDecimal bCount = BigDecimal.valueOf(count.doubleValue());
        if (value == null) {
            value = bCount;
        } else {
            value = value.add(bCount);
        }
        dataSet.put(data, value);
        sum = sum.add(bCount);
    }

    public Collection<T> getValues() {
        ArrayList<T> result = new ArrayList<>(dataSet.keySet());
        result.sort((o1, o2) -> dataSet.get(o2).subtract(dataSet.get(o1)).intValue());
        return result;
    }

    public Long getRate(T value, Long factor) {
        BigDecimal occurs = dataSet.get(value);
        if (occurs == null) {
            return null;
        } else if (sum.equals(BigDecimal.ZERO)) {
            return factor;
        } else {
            return occurs
                    .multiply(BigDecimal.valueOf(factor))
                    .divide(sum, MathContext.DECIMAL32).longValue();
        }
    }

}
