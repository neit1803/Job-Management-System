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
    public static final String USER_CHECK_QUEUE = "user.check.request.queue";
    public static final String USER_CHECK_RESPONSE_QUEUE = "auth.check.response.queue";

    // Exchange Names
    public static final String USER_EXCHANGE = "user.exchange";
    public static final String AUTH_EXCHANGE = "auth.exchange";

    // Routing Keys
    public static final String USER_CHECK_ROUTING_KEY = "user.check.request";
    public static final String USER_CHECK_RESPONSE_ROUTING_KEY = "auth.check.response";
    public static final String USER_SAVE_ROUTING_KEY = "user.save.request";

    // Define Queues
    @Bean
    public Queue userCheckQueue() {
        return new Queue(USER_CHECK_QUEUE, false);
    }

    @Bean
    public Queue userSaveQueue() {
        return new Queue(USER_SAVE_QUEUE, false);
    }

    @Bean
    public Queue userCheckResponseQueue() {
        return new Queue(USER_CHECK_RESPONSE_QUEUE, false);
    }

    // Define Exchanges
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(USER_EXCHANGE);
    }

    @Bean
    public TopicExchange authExchange() {
        return new TopicExchange(AUTH_EXCHANGE);
    }

    // Bindings
    @Bean
    public Binding checkUserBinding() {
        return BindingBuilder.bind(userCheckQueue()).to(userExchange()).with(USER_CHECK_ROUTING_KEY);
    }

    @Bean
    public Binding saveUserBinding() {
        return BindingBuilder.bind(userSaveQueue()).to(userExchange()).with(USER_SAVE_ROUTING_KEY);
    }

    @Bean
    public Binding checkUserResponseBinding() {
        return BindingBuilder.bind(userCheckResponseQueue()).to(authExchange()).with(USER_CHECK_RESPONSE_ROUTING_KEY);
    }
}
