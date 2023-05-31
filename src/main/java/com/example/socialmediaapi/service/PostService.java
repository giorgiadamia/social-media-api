package com.example.socialmediaapi.service;

import com.example.socialmediaapi.domain.Post;
import com.example.socialmediaapi.repository.PostRepository;
import com.example.socialmediaapi.web.dto.post.PostDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Value("${spring.resources.static-locations}")
    private String uploadPath;

    @Transactional(readOnly = true)
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
    }

    @Transactional(readOnly = true)
    public List<Post> getAllPostsByUserId(Long id) {
        return postRepository.findAllByUserId(id);
    }

    @Transactional
    public void updatePost(Long userId, Long postId, PostDto postDto) {
        Post postFromMemory = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        if (!Objects.equals(userId, postFromMemory.getUser().getId())) {
            throw new IllegalCallerException("It is not user's post");
        }

        if (!postFromMemory.getText().equals(postDto.getText())) {
            postFromMemory.setText(postDto.getText());
        }

        if (!postFromMemory.getTitle().equals(postDto.getTitle())) {
            postFromMemory.setTitle(postDto.getTitle());
        }

        if (postDto.getImage() != null) {
            File file = new File(postDto.getImage());
            if (file.exists()) {
                postFromMemory.setImage(postDto.getImage());
            }
        }

        postRepository.save(postFromMemory);
    }

    @Transactional
    public void createPost(Post post, Long userId) {
        File file = new File(post.getImage());

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getName();

        File folder = new File(uploadPath);

        if (file.exists()) {
            if (!folder.exists()) {
                folder.mkdir();
            }

            Path targetPath = Path.of(uploadPath, resultFilename);

            try {
                Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                post.setImage(resultFilename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            post.setImage(null); // не существует файла по такому пути
        }

        post.setUser(userService.getUserById(userId));
        post.setTime(LocalDateTime.now());
        postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post postFromMemory = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        if (!Objects.equals(userId, postFromMemory.getUser().getId())) {
            throw new IllegalCallerException("It is not user's post");
        }

        postRepository.deleteById(postId);
    }
}
