package com.tienhuynh.auth_service.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Queue Names
    public static final String USER_SAVE_QUEUE = "user.save.request.queue";
    public static final String USER_GET_QUEUE = "user.get.request.queue";

    // Exchange Names
    public static final String USER_EXCHANGE = "user.exchange";

    // Routing Keys
    public static final String USER_GET_ROUTING_KEY = "user.get.request";
    public static final String USER_SAVE_ROUTING_KEY = "user.save.request";

    // Define Queues
    @Bean
    public Queue userSaveQueue() {
        return new Queue(USER_SAVE_QUEUE, true);
    }

    @Bean
    public Queue userGetQueue() {
        return new Queue(USER_GET_QUEUE, false);
    }

    // Define Exchanges
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    // Bindings
    @Bean
    public Binding getUserBinding() {
        return BindingBuilder.bind(userGetQueue()).to(userExchange()).with(USER_GET_ROUTING_KEY);
    }

    @Bean
    public Binding saveUserBinding() {
        return BindingBuilder.bind(userSaveQueue()).to(userExchange()).with(USER_SAVE_ROUTING_KEY);
    }
}
