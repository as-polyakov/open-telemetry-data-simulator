package com.parallelstream.otsimulator.bindings;

import com.google.common.base.Objects;
import com.parallelstream.otsimulator.ValueGenerator;

public class RandomValueGenerator implements ValueGenerator {

    @Override
    public int generate() {
        return (int) (Math.random() * 100);
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
