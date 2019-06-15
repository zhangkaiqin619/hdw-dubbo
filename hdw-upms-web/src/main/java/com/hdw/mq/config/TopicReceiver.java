package com.hdw.mq.config;

import com.hdw.common.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ConsumerInfo;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * @Description com.hdw.mq.config
 * @Authour TuMinglong
 * @Date 2019/6/15 23:38
 */
@Component
@Slf4j
public class TopicReceiver extends MessageListenerAdapter {

    @Override
    public void setDefaultResponseTopicName(String destinationName) {
        super.setDefaultResponseTopicName("ActiveMQ.Advisory.Consumer.Queue.test");
    }

    @JmsListener(destination = "ActiveMQ.Advisory.Consumer.Queue.test", containerFactory = "topicJmsListenerContainerFactory")
    public void onMessage(Message msg) {
        if (msg instanceof ActiveMQMessage) {
            try {
                ActiveMQMessage aMsg = (ActiveMQMessage) msg;
                ConsumerInfo consumerInfo = (ConsumerInfo) aMsg.getDataStructure();
                System.out.println("消费者信息："+JacksonUtils.toJson(consumerInfo));
                // 告知activemq应用已收到消息
                msg.acknowledge();
            } catch (JMSException e) {
                log.error("Failed to process message: " + msg);
            }
        }
    }
}