package com.parallelstream.otsimulator.bindings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.parallelstream.otsimulator.ScenarioVisitor;

import java.util.List;

public class MetricsElement extends ScenarioElement {
    @JsonCreator
    public MetricsElement(@JsonProperty("elements") List<MetricElement> elements) {
        this.elements = elements;
    }

    @JsonProperty
    private final List<MetricElement> elements;

    public List<MetricElement> getElements() {
        return elements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricsElement that = (MetricsElement) o;
        return Objects.equal(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(elements);
    }

    @Override
    public void accept(ScenarioVisitor visitor) {
        visitor.visit(this);
    }
}
