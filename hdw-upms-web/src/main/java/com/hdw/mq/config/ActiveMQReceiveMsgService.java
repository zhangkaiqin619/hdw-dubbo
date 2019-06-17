package com.hdw.mq.config;

import com.hdw.common.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.command.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.stereotype.Component;

import javax.jms.*;
import javax.jms.Message;

/**
 * @Description ActiveMQ 生产者/消费者模式 消费者服务类 发布/订阅模式  订阅者模式
 * @Author TuMinglong
 * @Date 2018/5/23 16:05
 */
@Component
@Slf4j
public class ActiveMQReceiveMsgService {

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    /**
     * 生产者/消费者模式 消费者服务类
     *
     * @param text
     */
    @JmsListener(destination = "test", containerFactory = "queueJmsListenerContainerFactory", concurrency = "5-10")
    public void receiveMsg(String text) {
        log.info("==== 生产者/消费者模式 收到的消息：" + text + " ====");
    }


    /**
     * 发布/订阅模式  订阅者模式
     *
     * @param text
     */
    @JmsListener(destination = "testTopic", containerFactory = "topicJmsListenerContainerFactory", concurrency = "5-10")
    public void receiveTopicMsg(String text) {
        log.info("==== 发布/订阅模式 收到的消息：" + text + " ====");
    }


    @JmsListener(destination = "ActiveMQ.Advisory.Connection", containerFactory = "topicJmsListenerContainerFactory",concurrency = "5-10")
    public void advisoryConnection(Message msg) {
        if (msg instanceof ActiveMQMessage) {
            try {
                ActiveMQMessage aMsg = (ActiveMQMessage) msg;
                ConnectionInfo connectionInfo = (ConnectionInfo) aMsg.getDataStructure();
                log.info("连接信息：" + JacksonUtils.toJson(connectionInfo));
                // 告知activemq应用已收到消息
                msg.acknowledge();
            } catch (JMSException e) {
                log.error("Failed to process message: " + msg);
            }
        }
    }


    @JmsListener(destination = "ActiveMQ.Advisory.Queue", containerFactory = "topicJmsListenerContainerFactory", concurrency = "5-10")
    public void advisoryQueueMessage(Message msg) {
        if (msg instanceof ActiveMQMessage) {
            try {
                ActiveMQMessage aMsg = (ActiveMQMessage) msg;
                DestinationInfo destinationInfo = (DestinationInfo) aMsg.getDataStructure();
                log.info("队列信息：" + JacksonUtils.toJson(destinationInfo));
                // 告知activemq应用已收到消息
                msg.acknowledge();
            } catch (JMSException e) {
                log.error("Failed to process message: " + msg);
            }
        }
    }

    @JmsListener(destination = "ActiveMQ.Advisory.Consumer.Queue.test", containerFactory = "topicJmsListenerContainerFactory", concurrency = "5-10")
    public void advisoryConsumerQueueTest(Message msg) {
        if (msg instanceof ActiveMQMessage) {
            try {
                ActiveMQMessage aMsg = (ActiveMQMessage) msg;
                ConsumerInfo consumerInfo = (ConsumerInfo) aMsg.getDataStructure();
                log.info("消费者信息：" + JacksonUtils.toJson(consumerInfo));
                // 告知activemq应用已收到消息
                msg.acknowledge();
            } catch (JMSException e) {
                log.error("Failed to process message: " + msg);
            }
        }
    }

    @JmsListener(destination = "ActiveMQ.Advisory.Producer.Queue.test", containerFactory = "topicJmsListenerContainerFactory", concurrency = "5-10")
    public void advisoryProducerQueueTest(Message msg) {
        if (msg instanceof ActiveMQMessage) {
            try {
                ActiveMQMessage aMsg = (ActiveMQMessage) msg;
                ProducerInfo producerInfo = (ProducerInfo) aMsg.getDataStructure();
                log.info("生产者信息：" + JacksonUtils.toJson(producerInfo));
                // 告知activemq应用已收到消息
                msg.acknowledge();
            } catch (JMSException e) {
                log.error("Failed to process message: " + msg);
            }
        }
    }

}
