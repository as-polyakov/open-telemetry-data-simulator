package com.parallelstream.otsimulator;

import com.parallelstream.otsimulator.bindings.MetricsElement;
import com.parallelstream.otsimulator.bindings.RepeatGroupElement;
import com.parallelstream.otsimulator.bindings.ScenarioElement;
import com.parallelstream.otsimulator.bindings.SpansElement;
import io.opentelemetry.OpenTelemetry;
import io.opentelemetry.context.Scope;
import io.opentelemetry.metrics.LongValueRecorder;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.TracingContextUtils;

public class ScenarioVisitor {

    public void visit(RepeatGroupElement rg) {
        for (int i = 0; i < rg.getCount(); i++) {
            for (ScenarioElement e : rg.getElements()) {
                e.accept(this);
            }
        }
    }


    public void visit(MetricsElement metricsElement) {
        metricsElement.getElements().forEach(m -> {
            LongValueRecorder recorder = OpenTelemetry.getMeter("test").longValueRecorderBuilder(m.getName()).build();
            recorder.record(m.getValue().generate());
        });
    }

    public void visit(SpansElement spansElement) {
        /*try (Scope ignore = EventManagerImpl.getInstance().startEvent(ObsvsEventInput.builder()
                .spanBuilder(OpenTelemetry.getTracer("test").spanBuilder("myEvent"))
                .eventCallerMetadata(ObsvsEventCallerMetadata.builder()
                        .eventName("myEvent")
                        .build())
                .build())) {
                */
        Span.Builder spanBuilder = OpenTelemetry.getTracerProvider().get("test").spanBuilder(spansElement.getName());
        spansElement.getAttrbutes().forEach(spanBuilder::setAttribute);
        Span s = spanBuilder.startSpan();
        try (Scope ignored = TracingContextUtils.currentContextWith(s)) {
            spansElement.getBody().accept(this);
            s.end();
        }
            /*
        }
        */
    }
}
