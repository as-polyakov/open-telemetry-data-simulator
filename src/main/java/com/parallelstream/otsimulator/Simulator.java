package com.parallelstream.otsimulator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ImmutableList;
import com.parallelstream.otsimulator.bindings.ScenarioElement;
import io.opentelemetry.exporters.logging.LoggingMetricExporter;
import io.opentelemetry.exporters.logging.LoggingSpanExporter;
import io.opentelemetry.exporters.otlp.OtlpGrpcMetricExporter;
import io.opentelemetry.exporters.otlp.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.export.IntervalMetricReader;
import io.opentelemetry.sdk.trace.MultiSpanProcessor;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class Simulator {


    private static IntervalMetricReader logginMetricReader;
    private static IntervalMetricReader otlpMetricReader;

    public static void main(String[] args) throws IOException {
        setupSdk();

        ScenarioVisitor visitor = new ScenarioVisitor();
        read(new File(args[0])).accept(visitor);
        OpenTelemetrySdk.getTracerManagement().shutdown();
        logginMetricReader.shutdown();
        otlpMetricReader.shutdown();

    }

    private static void setupSdk() {
        OpenTelemetrySdk.getTracerManagement().addSpanProcessor(
                MultiSpanProcessor.create(ImmutableList.of(BatchSpanProcessor
                                .newBuilder(new LoggingSpanExporter()).build(),
                        BatchSpanProcessor.newBuilder(OtlpGrpcSpanExporter.getDefault()).build())));
        logginMetricReader = IntervalMetricReader.builder()
                .setMetricProducers(Collections.singletonList(OpenTelemetrySdk.getMeterProvider().getMetricProducer()))
                .setMetricExporter(new LoggingMetricExporter()).build();
        otlpMetricReader = IntervalMetricReader.builder()
                .setMetricProducers(Collections.singletonList(OpenTelemetrySdk.getMeterProvider().getMetricProducer()))
                .setMetricExporter( OtlpGrpcMetricExporter.getDefault()).build();
    }

    public static ScenarioElement read(File f) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory()).registerModule(new GuavaModule());
        return mapper.readValue(f, ScenarioElement.class);
    }
}

