package ru.yandex.practicum.catsgram.service;

public enum SortOrder {
    ASC,
    DESC;

    public static SortOrder from(String order) {
        if (order.equals("desc")) {
            return DESC;
        }

        return ASC;
    }
}
