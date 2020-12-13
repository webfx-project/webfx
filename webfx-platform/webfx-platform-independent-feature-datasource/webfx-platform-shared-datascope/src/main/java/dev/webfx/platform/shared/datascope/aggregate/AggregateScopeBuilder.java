package dev.webfx.platform.shared.datascope.aggregate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public final class AggregateScopeBuilder {

    private final Map<Object /* Aggregate type */, List<Object>> aggregates = new HashMap<>();

    public AggregateScopeBuilder addAggregate(Object type, Object key) {
        List<Object> aggregates = this.aggregates.computeIfAbsent(type, k -> new ArrayList<>());
        aggregates.add(key);
        return this;
    }

    public AggregateScope build() {
        return new AggregateScope(aggregates.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().toArray()
                )));
    }

}
