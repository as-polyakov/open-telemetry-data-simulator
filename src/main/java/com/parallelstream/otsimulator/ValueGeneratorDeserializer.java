package com.parallelstream.otsimulator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.parallelstream.otsimulator.bindings.RandomValueGenerator;
import com.parallelstream.otsimulator.bindings.StaticValueGenerator;

import java.io.IOException;

public class ValueGeneratorDeserializer extends JsonDeserializer<ValueGenerator> {
    @Override
    public ValueGenerator deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String val = jsonParser.readValueAs(String.class);
        return "random".equalsIgnoreCase(val) ? new RandomValueGenerator() : new StaticValueGenerator(Integer.parseInt(val));
    }
}
