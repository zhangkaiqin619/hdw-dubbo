package com.hdw.mq.entity;

import lombok.Data;

/**
 * @Descripton 接收ActiveMQ队列的信息
 * @Author TuMinglong
 * @Date 2019/6/14 14:39
 */
@Data
public class MqModel {
    private String name;//队列的名称
    private Long queueSize;//队列中剩余的消息数
    private Long consumerCount;//消费者数
    private Long enqueueCount;//进入队列的总数量
    private Long dequeueCount;//出队列的数量
}
