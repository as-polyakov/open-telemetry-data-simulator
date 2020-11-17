package com.parallelstream.otsimulator.bindings;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.parallelstream.otsimulator.ScenarioVisitor;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({@JsonSubTypes.Type(value = RepeatGroupElement.class, name = "repeatGroup"), @JsonSubTypes.Type(value = MetricsElement.class, name = "metrics"),
        @JsonSubTypes.Type(value = SpansElement.class, name = "span")})
public abstract class ScenarioElement {
    public abstract void accept(ScenarioVisitor visitor);
}
