package br.com.guntz.posts.post.domain.service;

import br.com.guntz.posts.post.api.config.exception.PostNotFoundException;
import br.com.guntz.posts.post.api.model.PostProducedData;
import br.com.guntz.posts.post.api.model.PostReceivedData;
import br.com.guntz.posts.post.domain.model.Post;
import br.com.guntz.posts.post.domain.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static br.com.guntz.posts.post.infrastructure.rabbitmq.RabbitMQConfig.FONOUT_EXCHANGE_POST_RECEIVED;
import static br.com.guntz.posts.post.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_POST_PROCESSING_RESULT;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostMessagingService {

    private final RabbitTemplate rabbitTemplate;
    private final PostRepository postRepository;

    public void sendPostToTextProcessorService(Post post) {
        var postData = convertPostToPostData(post);
        var routingKey = "";

        rabbitTemplate.convertAndSend(FONOUT_EXCHANGE_POST_RECEIVED, routingKey, postData);
        log.info("Post {} inserted in exchange: {}", post.getId(), FONOUT_EXCHANGE_POST_RECEIVED);
    }

    @Transactional
    public void processPostReading(PostReceivedData postReceivedData) {
        log.info("Post Processed from queue {} and updated: {}", QUEUE_POST_PROCESSING_RESULT, postReceivedData.getPostId());
        var post = postRepository.findById(postReceivedData.getPostId())
                .orElseThrow(PostNotFoundException::new);

        post.update(postReceivedData);
    }

    private PostProducedData convertPostToPostData(Post post) {
        return PostProducedData.builder()
                .postId(post.getId())
                .postBody(post.getBody())
                .build();
    }
}
