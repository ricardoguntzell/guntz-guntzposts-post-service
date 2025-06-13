package br.com.guntz.posts.post.api.controller;

import br.com.guntz.posts.post.api.config.exception.PostNotFoundException;
import br.com.guntz.posts.post.api.model.PostInput;
import br.com.guntz.posts.post.api.model.PostOutput;
import br.com.guntz.posts.post.api.model.PostSummaryOutput;
import br.com.guntz.posts.post.common.IdGenerator;
import br.com.guntz.posts.post.domain.model.Post;
import br.com.guntz.posts.post.domain.repository.PostRepository;
import br.com.guntz.posts.post.domain.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostSummaryOutput>> listAll(@PageableDefault Pageable pageable) {
        log.info("Executing Listing the Comments");

        var posts = postRepository.findAll(pageable)
                .map(this::convertPostToPostSummaryOutput);

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostOutput> findCommentById(@PathVariable UUID postId) {
        log.info("Executing Search the Post: {}", postId);

        Post post = postService.getPostById(postId);

        return ResponseEntity.ok(convertPostToPostOutput(post));
    }

    @PostMapping
    public ResponseEntity<PostSummaryOutput> create(@Valid @RequestBody PostInput input, UriComponentsBuilder uriBuilder) {
        Post postSaved = postService.save(convertToNewPost(input));

        var uri = uriBuilder.path("/api/posts/{postId}").buildAndExpand(postSaved.getId()).toUri();

        log.info("Post Registred: {}", postSaved.getId());

        return ResponseEntity.created(uri).body(convertPostToPostSummaryOutput(postSaved));
    }

    private Post convertToNewPost(PostInput input) {
        return Post.builder()
                .id(IdGenerator.generateTimeBasedUUID())
                .title(input.getTitle())
                .body(input.getBody())
                .author(input.getAuthor())
                .build();
    }

    private PostOutput convertPostToPostOutput(Post post) {
        return PostOutput.builder()
                .id(IdGenerator.generateTimeBasedUUID())
                .title(post.getTitle())
                .body(post.getBody())
                .author(post.getAuthor())
                .wordCount(post.getWordCount())
                .calculatedValue(post.getCalculatedValue())
                .build();
    }

    private PostSummaryOutput convertPostToPostSummaryOutput(Post post) {
        return PostSummaryOutput.builder()
                .id(IdGenerator.generateTimeBasedUUID())
                .title(post.getTitle())
                .summary(post.getBody())
                .author(post.getAuthor())
                .build();
    }


}
