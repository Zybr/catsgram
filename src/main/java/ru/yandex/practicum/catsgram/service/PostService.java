package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    private final UserService userService;

    public Collection<Post> findAll() {
        return posts.values();
    }

    public Post create(Post creation) {
        if (creation.getDescription() == null || creation.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        if (userService.findOne(creation.getAuthorId()).isEmpty()) {
            throw new ConditionsNotMetException(
                    String.format(
                            "There is not user with %s ID",
                            creation.getAuthorId()
                    )
            );
        }

        creation.setId(getNextId());
        creation.setPostDate(Instant.now());
        posts.put(creation.getId(), creation);
        return creation;
    }

    public Post update(Post updating) {
        if (updating.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (posts.containsKey(updating.getId())) {
            Post oldPost = posts.get(updating.getId());

            if (updating.getDescription() == null || updating.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }

            oldPost.setDescription(updating.getDescription());

            return oldPost;
        }

        throw new NotFoundException("Пост с id = " + updating.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);

        return ++currentMaxId;
    }
}
