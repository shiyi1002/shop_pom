package com.qf.shop_web_sso;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class RabbitConfig {

    /**
     * 声明一个队列
     * @return
     */
    @Bean
    public Queue getQueue() {
        return new Queue("email_queue");
    }

}
