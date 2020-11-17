package com.parallelstream.otsimulator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.parallelstream.otsimulator.bindings.*;
import io.opentelemetry.common.Labels;
import io.opentelemetry.exporters.logging.LoggingMetricExporter;
import io.opentelemetry.exporters.logging.LoggingSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.data.MetricData.LongPoint;
import io.opentelemetry.sdk.metrics.data.MetricData.Point;
import io.opentelemetry.sdk.metrics.export.IntervalMetricReader;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Simulator {


    private static IntervalMetricReader intervalMetricReader1;

    public static void main(String[] args) throws IOException {

        setupSdk();
        ScenarioVisitor visitor = new ScenarioVisitor();
        //root.accept(visitor);
        OpenTelemetrySdk.getTracerManagement().shutdown();
        intervalMetricReader1.shutdown();

    }

    private static void setupSdk() {
        OpenTelemetrySdk.getTracerManagement().addSpanProcessor(BatchSpanProcessor
                .newBuilder(new LoggingSpanExporter()).build());
        intervalMetricReader1 = IntervalMetricReader.builder()
                .setMetricProducers(Collections.singletonList(OpenTelemetrySdk.getMeterProvider().getMetricProducer()))
                .setMetricExporter(new LoggingMetricExporter()).build();
    }

    public ScenarioElement read() throws IOException {
        this.getClass().getResource("test-case.json");
        return null;
    }

    private static Class<? extends ScenarioElement> getType(String fieldName) {
        Objects.requireNonNull(fieldName);
        switch (fieldName) {
            case "metrics":
                return MetricsElement.class;
            case "span":
                return SpansElement.class;
            case "repeat":
                return RepeatGroupElement.class;
            default:
                throw new RuntimeException("unknown type " + fieldName);
        }
    }


    /*
        @Container
        public static GenericContainer<?> collectorContainer =
            new GenericContainer<>(DockerImageName.parse("otel/opentelemetry-collector-dev:latest"))
                .withNetwork(network)
                .withNetworkAliases("otel-collector")
                .withExposedPorts(OTLP_RECEIVER_PORT)
                .withCommand("--config=/etc/otel-collector-config-perf.yaml")
                .withCopyFileToContainer(
                    MountableFile.forClasspathResource("otel-collector-config-perf.yaml"),
                    "/etc/otel-collector-config-perf.yaml")
                .withLogConsumer(outputFrame -> System.out.print(outputFrame.getUtf8String()))
                .withLogConsumer(
                    outputFrame -> {
                      String logline = outputFrame.getUtf8String();
                      String spanExportPrefix = "TraceExporter\t{\"#spans\": ";
                      int start = logline.indexOf(spanExportPrefix);
                      int end = logline.indexOf("}");
                      if (start > 0) {
                        String substring = logline.substring(start + spanExportPrefix.length(), end);
                        totalSpansReceivedByCollector.addAndGet(Long.parseLong(substring));
                      }
                    })
                .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Everything is ready.*"));

        @Container
        public static GenericContainer<?> toxiproxyContainer =
            new GenericContainer<>(DockerImageName.parse("shopify/toxiproxy:latest"))
                .withNetwork(network)
                .withNetworkAliases("toxiproxy")
                .withExposedPorts(TOXIPROXY_CONTROL_PORT, COLLECTOR_PROXY_PORT)
                .dependsOn(collectorContainer)
                //          .withLogConsumer(outputFrame -> System.out.print(outputFrame.getUtf8String()))
                .waitingFor(new LogMessageWaitStrategy().withRegEx(".*API HTTP server starting.*"));
    */

    private IntervalMetricReader intervalMetricReader;


    private static void reportMetrics(List<MetricData> finishedMetricItems) {
        Map<String, List<MetricData>> metricsByName =
                finishedMetricItems.stream().collect(Collectors.groupingBy(MetricData::getName));
        metricsByName.forEach(
                (name, metricData) -> {
                    Stream<LongPoint> longPointStream =
                            metricData.stream().flatMap(md -> md.getPoints().stream()).map(p -> (LongPoint) p);
                    Map<Labels, List<LongPoint>> pointsByLabelset =
                            longPointStream.collect(Collectors.groupingBy(Point::getLabels));
                    pointsByLabelset.forEach(
                            (labels, longPoints) -> {
                                long total = longPoints.get(longPoints.size() - 1).getValue();
                                System.out.println(name + " : " + labels + " : " + total);
                            });
                });
    }
}

