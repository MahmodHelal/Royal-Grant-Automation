package core.context;

import java.util.HashMap;
import java.util.Map;

/**
 * ScenarioContext — simple key-value store for sharing data within a scenario.
 * مخزن بيانات بسيط لمشاركة البيانات داخل الـ scenario.
 */
public class ScenarioContext {

    private final Map<String, Object> context = new HashMap<>();

    public void set(String key, Object value) {
        context.put(key, value);
    }

    public Object get(String key) {
        return context.get(key);
    }

    public void clear() {
        context.clear();
    }
}
