package com.parallelstream.otsimulator;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.parallelstream.otsimulator.bindings.RandomValueGenerator;
import com.parallelstream.otsimulator.bindings.StaticValueGenerator;

import java.io.IOException;

public class ValueGeneratorSerializer extends JsonSerializer<ValueGenerator> {

    @Override
    public void serialize(ValueGenerator valueGenerator, com.fasterxml.jackson.core.JsonGenerator jsonGenerator, com.fasterxml.jackson.databind.SerializerProvider serializerProvider) throws IOException {
        if (valueGenerator instanceof RandomValueGenerator) {
            serialize((RandomValueGenerator) valueGenerator, jsonGenerator, serializerProvider);
        } else if (valueGenerator instanceof StaticValueGenerator) {
            serialize((StaticValueGenerator) valueGenerator, jsonGenerator, serializerProvider);
        }
    }

    private static void serialize(RandomValueGenerator rvg, com.fasterxml.jackson.core.JsonGenerator jsonGenerator, com.fasterxml.jackson.databind.SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString("random");
    }

    private static void serialize(StaticValueGenerator svg, com.fasterxml.jackson.core.JsonGenerator jsonGenerator, com.fasterxml.jackson.databind.SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(svg.generate());
    }
}
