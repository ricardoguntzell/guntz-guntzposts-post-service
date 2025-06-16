package br.com.guntz.posts.post.domain.service;

import br.com.guntz.posts.post.api.config.exception.PostNotFoundException;
import br.com.guntz.posts.post.api.model.PostData;
import br.com.guntz.posts.post.domain.model.Post;
import br.com.guntz.posts.post.domain.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static br.com.guntz.posts.post.infrastructure.rabbitmq.RabbitMQConfig.FONOUT_EXCHANGE_POST_RECEIVED;

@Slf4j
@AllArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Post save(Post post) {
        post.convertBodyToSummary();

        postRepository.saveAndFlush(post);
        log.info("Post Registred: {}", post.getId());

        var postData = convertPostToPostData(post);
        var routingKey = "";

        rabbitTemplate.convertAndSend(FONOUT_EXCHANGE_POST_RECEIVED, routingKey, postData);
        log.info("Post {} inserted in exchange: {}", post.getId(), FONOUT_EXCHANGE_POST_RECEIVED);

        return post;
    }

    private PostData convertPostToPostData(Post post) {
        return PostData.builder()
                .postId(post.getId())
                .postBody(post.getBody())
                .build();
    }

    public Post getPostById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

}
