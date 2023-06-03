package com.example.socialmediaapi.service;

import com.example.socialmediaapi.domain.Post;
import com.example.socialmediaapi.domain.User;
import com.example.socialmediaapi.repository.PostRepository;
import com.example.socialmediaapi.web.dto.PostDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostService postService;

    private final static String filePath = "src" + File.separator + "test" + File.separator +
            "resources" + File.separator + "testImages";

    @Value("${spring.resources.static-locations}")
    private String uploadPath;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPostById() {
        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        Post result = postService.getPostById(postId);

        assertNotNull(result);
        assertEquals(postId, result.getId());

        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    public void testGetPostByIdNonExistingPost() {
        Long postId = 1L;

        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> postService.getPostById(postId));

        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    public void testGetAllPostsByUserId() {
        Long userId = 1L;
        List<Post> posts = Arrays.asList(new Post(), new Post());

        when(postRepository.findAllByUserId(userId))
                .thenReturn(posts);

        List<Post> result = postService.getAllPostsByUserId(userId);

        assertNotNull(result);
        assertEquals(posts.size(), result.size());

        verify(postRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    public void testGetAllPostsByUserIdNoPosts() {
        Long userId = 1L;

        when(postRepository.findAllByUserId(userId))
                .thenReturn(Collections.emptyList());

        List<Post> result = postService.getAllPostsByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(postRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    public void testUpdatePost() {
        Long userId = 1L;
        Long postId = 1L;
        PostDto postDto = new PostDto();
        postDto.setTitle("Updated Title");
        postDto.setText("Updated Text");

        Post existingPost = new Post();
        existingPost.setId(postId);
        existingPost.setTitle("Old Title");
        existingPost.setText("Old Text");
        existingPost.setUser(new User());
        existingPost.getUser().setId(userId);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(existingPost));
        when(userService.getUserById(userId))
                .thenReturn(existingPost.getUser());
        when(postRepository.save(existingPost))
                .thenReturn(existingPost);

        assertDoesNotThrow(() -> postService.updatePost(userId, postId, postDto));

        assertEquals(postDto.getTitle(), existingPost.getTitle());
        assertEquals(postDto.getText(), existingPost.getText());

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(existingPost);
    }

    @Test
    public void testUpdatePostInvalidUser() {
        Long userId = 1L;
        Long postId = 1L;
        PostDto postDto = new PostDto();

        Post existingPost = new Post();
        existingPost.setUser(new User());
        existingPost.getUser().setId(2L);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(existingPost));

        assertThrows(IllegalCallerException.class, () -> postService.updatePost(userId, postId, postDto));

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).save(existingPost);
    }

    @Test
    public void testUpdatePostNonExistingPost() {
        Long userId = 1L;
        Long postId = 1L;
        PostDto postDto = new PostDto();

        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> postService.updatePost(userId, postId, postDto));

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    public void testCreatePost() {
        Long userId = 1L;
        String imageFileName = filePath + File.separator + "image.jpeg";
        String title = "postTitle";
        String text = "postText";

        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        post.setImage(imageFileName);

        when(postRepository.save(post)).thenReturn(post);

        postService.createPost(post, userId);

        File uploadDirectory = new File(uploadPath);

        assertTrue(uploadDirectory.exists());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    public void testCreatePostInvalidData() {
        Long userId = 1L;

        Post post = new Post();
        post.setImage("non-existing-image.jpg");

        File file = mock(File.class);
        File folder = mock(File.class);

        when(file.exists()).thenReturn(false);

        assertThrows(RuntimeException.class, () -> postService.createPost(post, userId));

        verifyNoInteractions(folder);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    public void testDeletePost() {
        Long userId = 1L;
        Long postId = 1L;

        Post existingPost = new Post();
        existingPost.setId(postId);
        existingPost.setUser(new User());
        existingPost.getUser().setId(userId);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(existingPost));

        assertDoesNotThrow(() -> postService.deletePost(userId, postId));

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    public void testDeletePostInvalidUser() {
        Long userId = 1L;
        Long postId = 1L;

        Post existingPost = new Post();
        existingPost.setUser(new User());
        existingPost.getUser().setId(2L);

        when(postRepository.findById(postId))
                .thenReturn(Optional.of(existingPost));

        assertThrows(IllegalCallerException.class, () -> postService.deletePost(userId, postId));

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testDeletePostNonExistingPost() {
        Long userId = 1L;
        Long postId = 1L;

        when(postRepository.findById(postId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> postService.deletePost(userId, postId));

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).deleteById(anyLong());
    }
}