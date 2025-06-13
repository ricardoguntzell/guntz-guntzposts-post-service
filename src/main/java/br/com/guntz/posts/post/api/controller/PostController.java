package br.com.guntz.posts.post.api.controller;

import br.com.guntz.posts.post.api.config.exception.PostNotFoundException;
import br.com.guntz.posts.post.api.model.PostOutput;
import br.com.guntz.posts.post.api.model.PostSummaryOutput;
import br.com.guntz.posts.post.common.IdGenerator;
import br.com.guntz.posts.post.domain.model.Post;
import br.com.guntz.posts.post.domain.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;

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

        Post post = getPostById(postId);

        return ResponseEntity.ok(convertPostToPostOutput(post));
    }



    private Post getPostById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
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
