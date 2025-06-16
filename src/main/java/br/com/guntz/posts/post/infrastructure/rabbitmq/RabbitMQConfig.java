package br.com.guntz.posts.post.infrastructure.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private static final String PROCESS_POST = "post-service.post-received.v1";
    public static final String FONOUT_EXCHANGE_POST_RECEIVED = PROCESS_POST + ".e";

    //text-processor-service
    private static final String QUEUE_TEXT_PROCESSOR_POST = "text-processor-service.post-processing.v1.q";

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    public Queue queueTextProcessorPost() {
        return QueueBuilder
                .durable(QUEUE_TEXT_PROCESSOR_POST)
                .build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queueTextProcessorPost()).to(exchange());
    }

    @Bean
    public FanoutExchange exchange() {
        return ExchangeBuilder
                .fanoutExchange(FONOUT_EXCHANGE_POST_RECEIVED)
                .build();
    }

}
