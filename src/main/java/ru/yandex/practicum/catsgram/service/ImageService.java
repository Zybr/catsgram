package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.model.Image;
import ru.yandex.practicum.catsgram.model.Post;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService extends AbstractModelService<Image> {
    private final PostService postService;
    private String rootDir = System.getProperty("user.dir");
    @Value("${resources.images-dir}")
    private String imageDir;

    public List<Image> getPostImages(
            Long postId
    ) {
        return models.values()
                .stream()
                .filter(image -> image.getPostId() == postId)
                .collect(Collectors.toList());
    }

    public Collection<Image> saveImages(
            Long postId,
            List<MultipartFile> files
    ) {
        return files
                .stream()
                .map(
                        file -> saveImage(
                                getPost(postId),
                                file
                        )
                )
                .toList();
    }

    private Post getPost(
            Long id
    ) {
        Optional<Post> post = postService.findOne(id);

        if (post.isEmpty()) {
            throw new ConditionsNotMetException(
                    String.format(
                            "There is not Post with %s ID",
                            id
                    )
            );
        }

        return post.get();
    }

    private Image saveImage(
            Post post,
            MultipartFile file
    ) {
        Image image = new Image();

        image.setId(getNextId());

        try {
            image.setFilePath(saveFile(post, file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        image.setOriginalFileName(file.getOriginalFilename());
        image.setPostId(post.getId());

        return image;
    }

    private String saveFile(
            Post post,
            MultipartFile file
    ) throws IOException {
        Path dirPath = getImageDir(post);
        Path filePath = Paths.get(
                String.format(
                        "%s/%s.%s",
                        dirPath,
                        Instant.now().toEpochMilli(),
                        StringUtils.getFilenameExtension(file.getOriginalFilename())
                )
        );

        if (Files.notExists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        file.transferTo(filePath);

        return filePath.toString();
    }

    private Path getImageDir(Post post) {
        String path = String.format(
                "%s%s%s%s%s",
                this.rootDir,
                "/",
                this.imageDir,
                "/",
                post.getId()
        ).replace("\"", "");

        return Paths.get(path);
    }
}