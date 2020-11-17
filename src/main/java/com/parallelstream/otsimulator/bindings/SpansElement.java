package com.parallelstream.otsimulator.bindings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.parallelstream.otsimulator.ScenarioVisitor;

public class SpansElement extends ScenarioElement {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("attributes")
    private final ImmutableMap<String, String> attrbutes;
    @JsonProperty("body")
    private final ScenarioElement body;

    @JsonCreator
    public SpansElement(@JsonProperty("name") String name, @JsonProperty("attributes") ImmutableMap<String, String> attributes, @JsonProperty("body") ScenarioElement body) {
        this.name = name;
        this.attrbutes = attributes;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public ImmutableMap<String, String> getAttrbutes() {
        return attrbutes;
    }

    public ScenarioElement getBody() {
        return body;
    }

    @Override
    public void accept(ScenarioVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpansElement that = (SpansElement) o;
        return Objects.equal(name, that.name) &&
                Objects.equal(attrbutes, that.attrbutes) &&
                Objects.equal(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, attrbutes, body);
    }
}
