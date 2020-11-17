package com.parallelstream.otsimulator.bindings;

import com.google.common.base.Objects;
import com.parallelstream.otsimulator.ValueGenerator;

public class StaticValueGenerator implements ValueGenerator {
    private final int value;

    public StaticValueGenerator(int value) {
        this.value = value;
    }

    @Override
    public int generate() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaticValueGenerator that = (StaticValueGenerator) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
