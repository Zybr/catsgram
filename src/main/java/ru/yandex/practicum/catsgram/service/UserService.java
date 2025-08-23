package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final Map<Long, User> models = new HashMap<>();
    private Long lastId = 0L;

    public Optional<User> findOne(Long id) {
        return Optional.ofNullable(
                this.models.getOrDefault(id, null)
        );
    }

    public Collection<User> findAll() {
        return models.values();
    }

    public User create(User creation) {
        if (creation.getEmail() == null) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        if (this.getUserEmails().contains(creation.getEmail())) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        creation.setRegistrationDate(Instant.now());

        creation.setId(++lastId);
        models.put(creation.getId(), creation);

        return creation;
    }

    public User update(User updating) {
        if (updating.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (!this.models.containsKey(updating.getId())) {
            throw new NotFoundException("User not found");
        }

        User user = models.get(updating.getId());

        if (updating.getUsername() != null) {
            user.setUsername(updating.getUsername());
        }

        if (updating.getPassword() != null) {
            user.setPassword(updating.getPassword());
        }

        models.put(updating.getId(), user);

        return updating;
    }

    private Set<String> getUserEmails() {
        return this.models.values()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());
    }
}
