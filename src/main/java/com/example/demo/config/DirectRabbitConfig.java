package com.example.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 自动配置--》
 *  也可以直接进入消息管理中心配置
 *  by jsg
 */
@Configuration
public class DirectRabbitConfig {


    /**
     * 队列
     * @return
     */
    @Bean
    public Queue DirectQueue() {
        return new Queue(Constant.USER_MESSAGE, true);
    }

    @Bean
    public Queue DirectQueue_callback() {
        return new Queue(Constant.ORDER_CALLBACK_MESSAGE, true);
    }
    /**
     * 交换机
     * @return
     */
    @Bean
    DirectExchange DirectExchange() {
        return new DirectExchange(Constant.USER_DIRECT_EXCHANGE, true, false);
    }
    @Bean
    DirectExchange DirectExchange_callback() {
        return new DirectExchange(Constant.ORDER_CALLBACK_DIRECT_EXCHANGE, true, false);
    }

    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(DirectQueue_callback()).to(DirectExchange_callback()).with(Constant.DIRECT_ROUTING);
    }
    @Bean
    Binding bindingDirect_callback() {
        return BindingBuilder.bind(DirectQueue()).to(DirectExchange()).with(Constant.ORDER_CALLBACK_DIRECT_ROUTING);
    }

    @Bean
    DirectExchange lonelyDirectExchange() {
        return new DirectExchange("lonelyDirectExchange");
    }


}