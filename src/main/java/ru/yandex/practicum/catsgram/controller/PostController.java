package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.Optional;

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
    public Collection<Post> findAll() {
        return postService.findAll();
    }

    @PostMapping
    public Post create(@RequestBody Post creation) {
        return postService.create(creation);
    }

    @PutMapping
    public Post update(@RequestBody Post updating) {
        return postService.update(updating);
    }
}