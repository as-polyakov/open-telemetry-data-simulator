package com.parallelstream.otsimulator.bindings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.parallelstream.otsimulator.ValueGenerator;
import com.parallelstream.otsimulator.ValueGeneratorDeserializer;
import com.parallelstream.otsimulator.ValueGeneratorSerializer;

public class MetricElement {
    @JsonProperty
    private final String name;
    @JsonProperty
    private final ImmutableMap<String, String> attrbutes;

    @JsonProperty
    @JsonDeserialize(using = ValueGeneratorDeserializer.class)
    @JsonSerialize(using = ValueGeneratorSerializer.class)
    private final ValueGenerator value;

    @JsonCreator
    public MetricElement(@JsonProperty("name") String name, @JsonProperty("value") ValueGenerator value,
                         @JsonProperty("attributes") ImmutableMap<String, String> attributes) {
        this.name = name;
        this.value = value;
        this.attrbutes = attributes;
    }

    public String getName() {
        return name;
    }

    public ImmutableMap<String, String> getAttrbutes() {
        return attrbutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricElement that = (MetricElement) o;
        return Objects.equal(name, that.name) &&
                Objects.equal(attrbutes, that.attrbutes) &&
                Objects.equal(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, attrbutes, value);
    }

    public ValueGenerator getValue() {
        return value;
    }
}
