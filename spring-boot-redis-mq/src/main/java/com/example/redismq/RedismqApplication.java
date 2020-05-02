package com.example.redismq;

import com.example.message.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class RedismqApplication {


    private static final Logger LOGGER = LoggerFactory.getLogger(RedismqApplication.class);

    
    /**
     * 配置消息监听容器，监听topic为message的消息
     **/
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("message"));

        return container;
    }

    /**
     * 通过消息适配器来将消息接收者和默认消息监听方法注册给容器
     **/
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    Receiver receiver(CountDownLatch latch) {
        return new Receiver(latch);
    }

    @Bean
    CountDownLatch latch() {
        return new CountDownLatch(1);
    }

    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    public static void main(String[] args) throws Exception{
        ApplicationContext ctx =  SpringApplication.run(RedismqApplication.class, args);

        StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
        CountDownLatch latch = ctx.getBean(CountDownLatch.class);

        LOGGER.info("Sending message...");
        LOGGER.info("Begin to receive message ...");
        template.convertAndSend("message", "Hello Lu Hao !");

        latch.await();
        LOGGER.info("Reciived completed successfully!");
        System.exit(0);
    }


    /**
     * 2020-05-02 21:07:46.018  INFO 34740 --- [           main] com.example.redismq.RedismqApplication   : Sending message...
     * 2020-05-02 21:07:46.018  INFO 34740 --- [           main] com.example.redismq.RedismqApplication   : Begin to receive message ...
     * 2020-05-02 21:07:46.040  INFO 34740 --- [    container-2] com.example.message.Receiver             : Received message is <Hello Lu Hao !>
     * 2020-05-02 21:07:46.040  INFO 34740 --- [           main] com.example.redismq.RedismqApplication   : Reciived completed successfully!
     **/

}
