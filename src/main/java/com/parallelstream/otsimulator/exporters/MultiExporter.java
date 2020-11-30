package com.parallelstream.otsimulator.exporters;

import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.export.MetricExporter;

import java.util.ArrayList;
import java.util.Collection;

public class MultiExporter implements MetricExporter {

    private final Collection<MetricExporter> exporters;

    public MultiExporter(Collection<MetricExporter> exporters) {
        this.exporters = exporters;
    }

    @Override
    public CompletableResultCode export(Collection<MetricData> collection) {
        Collection<CompletableResultCode> res = new ArrayList<>(exporters.size());
        for (MetricExporter e : exporters) {
            res.add(e.export(collection));
        }
        return CompletableResultCode.ofAll(res);
    }


    @Override
    public CompletableResultCode flush() {
        Collection<CompletableResultCode> res = new ArrayList<>(exporters.size());
        for (MetricExporter e : exporters) {
            res.add(e.flush());
        }
        return CompletableResultCode.ofAll(res);
    }

    @Override
    public void shutdown() {
        for (MetricExporter e : exporters) {
            e.shutdown();
        }
    }
}
