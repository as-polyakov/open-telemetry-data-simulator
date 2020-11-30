package com.parallelstream.otsimulator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ImmutableList;
import com.parallelstream.otsimulator.bindings.ScenarioElement;
import com.parallelstream.otsimulator.exporters.MultiExporter;
import io.opentelemetry.exporters.logging.LoggingMetricExporter;
import io.opentelemetry.exporters.logging.LoggingSpanExporter;
import io.opentelemetry.exporters.otlp.OtlpGrpcMetricExporter;
import io.opentelemetry.exporters.otlp.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.export.IntervalMetricReader;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.trace.MultiSpanProcessor;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class Simulator {


    private static IntervalMetricReader metricReader;

    public static void main(String[] args) throws IOException, InterruptedException {
        setupSdk();
        if(args.length < 1) {
            System.out.println("Please specify scenario file name to execute");
            return;
        }

        execute(read(new File(args[0])));
        Thread.sleep(1000);
        metricReader.shutdown();
        OpenTelemetrySdk.getTracerManagement().shutdown();

    }

    private static void execute(ScenarioElement root) {
        root.accept(new ScenarioVisitor());
    }

    private static void setupSdk() {
        OpenTelemetrySdk.getTracerManagement().addSpanProcessor(
                MultiSpanProcessor.create(ImmutableList.of(BatchSpanProcessor
                                .newBuilder(new LoggingSpanExporter()).build(),
                        BatchSpanProcessor.newBuilder(OtlpGrpcSpanExporter.getDefault()).build())));

        MetricExporter multiExporter = new MultiExporter(ImmutableList.of(OtlpGrpcMetricExporter.getDefault(), new LoggingMetricExporter()));
        metricReader = IntervalMetricReader.builder()
                .setExportIntervalMillis(600)
                .setMetricProducers(Collections.singletonList(OpenTelemetrySdk.getMeterProvider().getMetricProducer()))
                .setMetricExporter(multiExporter).build();

    }

    public static ScenarioElement read(File f) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new JsonFactory()).registerModule(new GuavaModule());
        return mapper.readValue(f, ScenarioElement.class);
    }
}

