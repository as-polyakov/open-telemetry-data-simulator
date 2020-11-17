package com.parallelstream.otsimulator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.parallelstream.otsimulator.bindings.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;


public class SimulatorTest {

    @Test
    public void testSerDe() throws JsonProcessingException {
        List<ScenarioElement> el = ImmutableList.of(
                new SpansElement("randomSpan", ImmutableMap.of(
                        "type", "http", "userId", "userA"
                ), new RepeatGroupElement(ImmutableList.of(
                        new MetricsElement(
                                ImmutableList.of(
                                        new MetricElement("http.latency", new RandomValueGenerator(), ImmutableMap.of("k1", "v1")),
                                        new MetricElement("http.latency", new RandomValueGenerator(), ImmutableMap.of("k1", "v1"))
                                )
                        )
                ), 10)),
                new MetricsElement(
                        ImmutableList.of(
                                new MetricElement("http1.latency", new RandomValueGenerator(), ImmutableMap.of("k1", "v1")),
                                new MetricElement("http1.latency", new RandomValueGenerator(), ImmutableMap.of("k1", "v1"))
                        )
                )
        );
        RepeatGroupElement root = new RepeatGroupElement(el, 10);
        ObjectMapper mapper = new ObjectMapper(new JsonFactory()).registerModule(new GuavaModule());
        String valueAsString = mapper.writeValueAsString(root);
        ScenarioElement e = mapper.readValue(valueAsString, RepeatGroupElement.class);
        Assert.assertEquals(root, e);
    }

}
