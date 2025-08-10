package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.ToString;

import java.time.Instant;

@SuppressWarnings("all")
@Data
public class Post {
    private Long Id;
    @ToString.Exclude
    private Long authorId;
    @ToString.Exclude
    private String description;
    @ToString.Exclude
    private Instant postDate;
}
