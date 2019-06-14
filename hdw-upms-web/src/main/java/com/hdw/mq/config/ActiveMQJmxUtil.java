package com.hdw.mq.config;

import com.hdw.mq.entity.MqModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.Connection;
import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.ConnectionViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.broker.region.policy.ConstantPendingMessageLimitStrategy;
import org.apache.activemq.broker.region.policy.PolicyEntry;
import org.apache.activemq.broker.region.policy.PolicyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Descripton com.hdw.mq.config
 * @Author TuMinglong
 * @Date 2019/6/14 11:58
 */
@Component
@Slf4j
public class ActiveMQJmxUtil {

    protected String bindAddress = "tcp://localhost:61619";

    @Autowired
    private ActiveMQJmxConfig activeMQRemoteConfig;

    public List<MqModel> getQueueList() {
        List<MqModel> list = new ArrayList<>();
        BrokerViewMBean mBean = null;
        MBeanServerConnection mbsc = null;
        JMXConnector connector = null;
        try {
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + activeMQRemoteConfig.getConnectorIp() + ":" + activeMQRemoteConfig.getConnectorPort() + activeMQRemoteConfig.getConnectorPath());
            connector = JMXConnectorFactory.connect(url);
            connector.connect();
            mbsc = connector.getMBeanServerConnection();
            ObjectName name = new ObjectName(activeMQRemoteConfig.getJmxDomain() + ":brokerName=" + activeMQRemoteConfig.getBrokerName() + ",type=Broker");
            mBean = MBeanServerInvocationHandler.newProxyInstance(mbsc, name, BrokerViewMBean.class, true);

            if (mBean != null) {
                for (ObjectName queueName : mBean.getQueues()) {
                    QueueViewMBean queueMBean = MBeanServerInvocationHandler.newProxyInstance(mbsc, queueName, QueueViewMBean.class, true);

                    MqModel mq = new MqModel();
                    mq.setName(queueMBean.getName());
                    mq.setQueueSize(queueMBean.getQueueSize());
                    mq.setConsumerCount(queueMBean.getConsumerCount());
                    mq.setDequeueCount(queueMBean.getDequeueCount());
                    mq.setEnqueueCount(queueMBean.getEnqueueCount());
                    System.out.println("Queue Name （消息队列名称）： " + queueMBean.getName());// 消息队列名称
                    System.out.println("Queue Size （队列中剩余的消息数）： " + queueMBean.getQueueSize());// 队列中剩余的消息数
                    System.out.println("Number of Consumers （消费者数）： " + queueMBean.getConsumerCount());// 消费者数
                    System.out.println("Number of Dequeue（出队总数）：" + queueMBean.getDequeueCount());// 出队总数
                    System.out.println("Number of Enqueue（入队总数）：" + queueMBean.getEnqueueCount());// 入队总数
                    list.add(mq);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connector != null) {
                    connector.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
