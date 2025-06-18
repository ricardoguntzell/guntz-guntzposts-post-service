package br.com.guntz.posts.post.infrastructure.rabbitmq;

import br.com.guntz.posts.post.api.model.PostReceivedData;
import br.com.guntz.posts.post.domain.service.PostMessagingService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static br.com.guntz.posts.post.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_POST_PROCESSING_RESULT;


@AllArgsConstructor
@Component
public class RabbitMQListener {

    private final PostMessagingService postMessagingService;

    @SneakyThrows
    @RabbitListener(queues = QUEUE_POST_PROCESSING_RESULT, concurrency = "2-3")
    public void handleProcessPostFromTextProcessorService(@Payload PostReceivedData postReceivedData) {
        postMessagingService.processPostReading(postReceivedData);
    }

}
