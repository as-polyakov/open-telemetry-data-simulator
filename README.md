# open-telemetry-data-simulator
This is a tool for generating OpenTelemetry data and emitting it to a local sidecar. Useful for testing OpenTelemetry components and doing stress tests.

Test data is define in JSON file which simulator replays.

There are 3 main elements in the test data scenario:
* Repeat Group - iterates its body specific number of times
* Span - emits a span, can nest other spans and metrics
* Metrics - emits metrics, can be nested within Span

Example:

```
{
    "@type": "repeatGroup",
    "elements": [
      {
        "@type": "span",
        "name": "randomSpan",
        "attributes": {
          "type": "http",
          "userId": "userA"
        },
        "body": {
          "@type": "repeatGroup",
          "elements": [
            {
              "@type": "metrics",
              "elements": [
                {
                  "name": "http.latency",
                  "value": "random",
                  "attrbutes": {
                    "k1": "v1"
                  }
                },
                {
                  "name": "http.latency",
                  "value": "random",
                  "attrbutes": {
                    "k1": "v1"
                  }
                }
              ]
            }
          ],
          "count": 10
        }
      },
      {
        "@type": "metrics",
        "elements": [
          {
            "name": "http1.latency",
            "value": "random",
            "attrbutes": {
              "k1": "v1"
            }
          },
          {
            "name": "http1.latency",
            "value": "random",
            "attrbutes": {
              "k1": "v1"
            }
          }
        ]
      }
    ],
    "count": 10
  }
```

Here we have a root level repeat group which starts a span with the name `randomSpan` and the body of another repeated group consisting of two metrics `http.latency`. Metrics are emitted 10 times per each iteration of `randomSpan`. Once `randomSpan` is closed two more metrics `http.latency1` are emitted, then the whole thing repeats.
Value for metrics can be either a number or "random" in which case random value is emitted.