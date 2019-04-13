package com.qf.shop_service_goods;


import org.springframework.amqp.core.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ConfigurationPriovide {
    //声明一个交换机的名
    public static final String FANTOUT_NAME="goods_exchange";
    /**
     * 初始化两个消息队列
     * @return
     */
        @Bean
        public Queue getQueue1(){
            return new Queue("good_queue1");
        }
    @Bean
    public Queue getQueue2(){
        return new Queue("good_queue2");
    }

    /**
     * 声明交换机
     * @return
     */
    @Bean
    public FanoutExchange getExchange() {
        return new FanoutExchange(FANTOUT_NAME);
    }

    /**
     * 绑定交换机和队列
     * @param getQueue1
     * @param getExchange
     * @return
     */
    @Bean
    public Binding getBind1(Queue getQueue1,FanoutExchange getExchange){
      return  BindingBuilder.bind(getQueue1).to(getExchange);
    }
    @Bean
    public Binding getBind2(Queue getQueue2,FanoutExchange getExchange){
        return  BindingBuilder.bind(getQueue2).to(getExchange);
    }
}
