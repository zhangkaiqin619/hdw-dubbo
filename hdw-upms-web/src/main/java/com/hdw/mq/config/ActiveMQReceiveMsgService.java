package com.hdw.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.advisory.AdvisorySupport;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ProducerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.stereotype.Component;

import javax.jms.*;

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
    //@JmsListener(destination = "test", containerFactory = "queueJmsListenerContainerFactory", concurrency = "5-10")
    public void receiveMsg(String text) {
        System.out.println("==== 生产者/消费者模式 收到的消息：" + text + " ====");
    }


    /**
     * 发布/订阅模式  订阅者模式
     *
     * @param text
     */
    //@JmsListener(destination = "ActiveMQ.Advisory.Connection", containerFactory = "topicJmsListenerContainerFactory", concurrency = "5-10")
    public void receiveTopicMsg(String text) {
        System.out.println("==== 发布/订阅模式 收到的消息：" + text + " ====");
    }

    public void testAdvisory4() throws Exception {

        final Session session = cachingConnectionFactory.createConnection()
                .createSession(false/*支持事务*/, Session.AUTO_ACKNOWLEDGE);

        Destination advisoryDestination = AdvisorySupport.getProducerAdvisoryTopic("ActiveMQ.Advisory.Connection");
        MessageConsumer consumer = session.createConsumer(advisoryDestination);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message msg) {

                if (msg instanceof ActiveMQMessage) {
                    try {
                        ActiveMQMessage aMsg = (ActiveMQMessage) msg;
                        ProducerInfo prod = (ProducerInfo) aMsg.getDataStructure();
                    } catch (JMSException e) {
                        log.error("Failed to process message: " + msg);
                    }
                }

            }

        });

    }

}
