package ru.yandex.practicum.catsgram.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("all")
@ToString
@Getter
@Setter
class Cat {
    private String color;
    private int age;
}