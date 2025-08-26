package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;
import ru.yandex.practicum.catsgram.service.SortOrder;

import java.util.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/{id}")
    public Post findOne(@PathVariable Long id) {
        Optional<Post> post = postService.findOne(id);
        if (post.isEmpty()) {
            throw new NotFoundException(
                    String.format(
                            "There is no Post with %s ID.",
                            id
                    )
            );
        }
        return post.get();
    }

    @GetMapping
    public Collection<Post> findAll(
            @RequestParam(required = false, defaultValue = "10") Long size,
            @RequestParam(required = false, defaultValue = "0") Long from,
            @RequestParam(required = false, defaultValue = "desc") String sort
    ) {
        List<String> sortValues = Arrays.stream(SortOrder.values())
                .map(SortOrder::toString)
                .toList();

        boolean isSortValid = sortValues
                .stream()
                .anyMatch((sortOrder) -> sortOrder.equals(sort));

        if (!isSortValid) {
            throw new ParameterNotValidException(
                    "sort",
                    String.format(
                            "Allowed values are: %s.",
                            String.join(",", sortValues)
                    )
            );
        }

        if (size <= 0) {
            throw new ParameterNotValidException(
                    "size",
                    "The value must be more than 0."
            );
        }

        if (from < 0) {
            throw new ParameterNotValidException(
                    "from",
                    "The value must be more or equal 0."
            );
        }

        return postService.findAll(
                size,
                from,
                SortOrder.from(sort)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post creation) {
        return postService.create(creation);
    }

    @PutMapping
    public Post update(@RequestBody Post updating) {
        return postService.update(updating);
    }
}