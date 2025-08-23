package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.model.Image;
import ru.yandex.practicum.catsgram.model.ImageData;
import ru.yandex.practicum.catsgram.service.ImageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/posts/{postId}/images")
    public List<Image> getPostImages(
            @PathVariable("postId") long postId
    ) {
        return imageService.getPostImages(postId);
    }

    @PostMapping("/posts/{postId}/images")
    public List<Image> createPostImages(
            @PathVariable("postId") long postId,
            @RequestParam("images") List<MultipartFile> files
    ) {
        return imageService.saveImages(postId, files).stream().toList();
    }

    @GetMapping(value = "/images/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadImage(
            @PathVariable long id
    ) {
        ImageData data = imageService.getImageData(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition
                        .attachment()
                        .filename(data.getName())
                        .build()
        );

        return new ResponseEntity<>(
                data.getData(),
                headers,
                HttpStatus.OK
        );
    }
}