package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.ToString;

@SuppressWarnings("all")
@Data
public class Image {
    private Long Id;
    @ToString.Exclude
    private long postId;
    @ToString.Exclude
    private String originalFileName;
    @ToString.Exclude
    private String filePath;
}
