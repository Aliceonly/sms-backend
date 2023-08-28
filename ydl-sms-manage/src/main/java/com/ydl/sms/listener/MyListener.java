package com.ydl.sms.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

//普通类--》reids监听类
@Slf4j
@Component
public class MyListener implements MessageListener{
    @Override
    public void onMessage(Message message, byte[] bytes) {
        log.info("接收到了消息:{}"+message);
    }
}