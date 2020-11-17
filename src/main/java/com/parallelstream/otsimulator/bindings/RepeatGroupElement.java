package com.parallelstream.otsimulator.bindings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.parallelstream.otsimulator.ScenarioVisitor;

import java.util.List;

public class RepeatGroupElement extends ScenarioElement {
    @JsonProperty
    private final List<? extends ScenarioElement> elements;
    @JsonProperty
    private final int count;

    @JsonCreator
    public RepeatGroupElement(
            @JsonProperty("elements") List<ScenarioElement> elements, @JsonProperty("count") int count) {
        this.elements = elements;
        this.count = count;
    }

    public List<? extends ScenarioElement> getElements() {
        return elements;
    }

    public int getCount() {
        return count;
    }

    @Override
    public void accept(ScenarioVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepeatGroupElement that = (RepeatGroupElement) o;
        return count == that.count &&
                Objects.equal(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(elements, count);
    }
}
