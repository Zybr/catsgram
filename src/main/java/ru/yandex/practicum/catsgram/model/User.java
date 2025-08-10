package ru.yandex.practicum.catsgram.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@SuppressWarnings("all")
@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class User {
    @EqualsAndHashCode.Exclude
    private Long Id;
    @EqualsAndHashCode.Exclude
    private String username;
    private String email;
    @EqualsAndHashCode.Exclude
    private String password;
    @EqualsAndHashCode.Exclude
    private Instant registrationDate;
}
