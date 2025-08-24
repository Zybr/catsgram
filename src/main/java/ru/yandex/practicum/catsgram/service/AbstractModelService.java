package ru.yandex.practicum.catsgram.service;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractModelService<Model> {
    protected final Map<Long, Model> models = new HashMap<>();

    protected long getNextId() {
        return 1 + models.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
    }
}
