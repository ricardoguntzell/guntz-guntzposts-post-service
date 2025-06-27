package br.com.guntz.posts.post.infrastructure.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    private static final String PROCESS_POST_PROCESSING_RESULT = "post-service.post-processing-result.v1";

    public static final String QUEUE_POST_PROCESSING_RESULT = PROCESS_POST_PROCESSING_RESULT + ".q";
    public static final String DEAD_LETTER_QUEUE_POST_PROCESSING_RESULT = PROCESS_POST_PROCESSING_RESULT + ".dlq";
    public static final String FONOUT_EXCHANGE_POST_RECEIVED = PROCESS_POST_PROCESSING_RESULT + ".e";

    //text-processor-service
    public static final String FONOUT_EXCHANGE_TEXT_PROCESSOR_RECEIVED = "text-processor-service.post-processing.v1.e";

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public Queue queuePostProcessingResult() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "");
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_POST_PROCESSING_RESULT);

        return QueueBuilder
                .durable(QUEUE_POST_PROCESSING_RESULT)
                .withArguments(args)
                .build();
    }

    @Bean
    public Queue deadLetterQueuePostProcessingResult() {
        return QueueBuilder
                .durable(DEAD_LETTER_QUEUE_POST_PROCESSING_RESULT)
                .build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queuePostProcessingResult()).to(exchangePostRecived());
    }

    @Bean
    public FanoutExchange exchangePostRecived() {
        return ExchangeBuilder
                .fanoutExchange(FONOUT_EXCHANGE_POST_RECEIVED)
                .build();
    }

}