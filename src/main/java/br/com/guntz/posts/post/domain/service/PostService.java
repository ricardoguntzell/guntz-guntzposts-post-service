package br.com.guntz.posts.post.domain.service;

import br.com.guntz.posts.post.api.config.exception.PostNotFoundException;
import br.com.guntz.posts.post.domain.model.Post;
import br.com.guntz.posts.post.domain.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostMessagingService postMessagingService;

    @Transactional
    public Post save(Post post) {
        post.convertBodyToSummary();

        postRepository.saveAndFlush(post);
        log.info("Post Registred: {}", post.getId());

        postMessagingService.sendPostToTextProcessorService(post);

        return post;
    }

    public Post getPostById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

}
