package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.UserService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User findOne(@PathVariable Long id) {
        Optional<User> user = userService.findOne(id);
        if (user.isEmpty()) {
            throw new NotFoundException(
                    String.format(
                            "There is no User with %s ID.",
                            id
                    )
            );
        }
        return user.get();
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User creation) {
        return userService.create(creation);
    }

    @PutMapping
    public User update(@RequestBody User updating) {
        return userService.update(updating);
    }
}
