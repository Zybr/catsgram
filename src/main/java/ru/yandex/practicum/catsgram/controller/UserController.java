package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private Long lastId = 0L;

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        if (this.getUserEmails().contains(user.getEmail())) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        user.setRegistrationDate(Instant.now());

        user.setId(++lastId);
        users.put(user.getId(), user);

        return user;
    }

    @PutMapping
    public User update(@RequestBody User update) {
        if (update.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (!this.users.containsKey(update.getId())) {
            throw new NotFoundException("User not found");
        }

        User user = users.get(update.getId());

        if (update.getUsername() != null) {
            user.setUsername(update.getUsername());
        }

        if (update.getPassword() != null) {
            user.setPassword(update.getPassword());
        }

        users.put(update.getId(), user);

        return update;
    }

    private Set<String> getUserEmails() {
        return this.users.values()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());
    }
}
