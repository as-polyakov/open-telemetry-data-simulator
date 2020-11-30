package com.parallelstream.otsimulator;

import com.parallelstream.otsimulator.bindings.MetricsElement;
import com.parallelstream.otsimulator.bindings.RepeatGroupElement;
import com.parallelstream.otsimulator.bindings.ScenarioElement;
import com.parallelstream.otsimulator.bindings.SpansElement;
import io.opentelemetry.OpenTelemetry;
import io.opentelemetry.common.Labels;
import io.opentelemetry.context.Scope;
import io.opentelemetry.metrics.LongCounter;
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
            LongCounter recorder = OpenTelemetry.getMeter("test").longCounterBuilder(m.getName()).build();
            Labels.Builder labelsBuilder = Labels.newBuilder();
            m.getAttrbutes().forEach(labelsBuilder::setLabel);
            recorder.add(m.getValue().generate(), labelsBuilder.build());
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
        spansElement.getAttributes().forEach(spanBuilder::setAttribute);
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
