package com.hdw.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Description com.hdw.thread
 * @Author TuMinglong
 * @Date 2019/3/20 22:20
 **/
public class Counter {

    int count = 0;

    public synchronized int getCount() {
        return count;
    }

    public synchronized void add() {
        count += 1;
    }

    public synchronized void dec() {
        count -= 1;
    }
}

class AddDataThread implements Runnable {

    private volatile Counter counter;

    public AddDataThread(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            counter.add();
            System.out.println(Thread.currentThread().getName() + " " + counter.getCount());
        }
    }
}

class DecDataThread implements Runnable {

    private volatile Counter counter;

    public DecDataThread(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            counter.dec();
            System.out.println(Thread.currentThread().getName() + " " + counter.getCount());
        }
    }
}

class CasClass {
    final static int LOOP = 10000;

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService pool = Executors.newFixedThreadPool(12);
        Counter counter = new Counter();
        AddDataThread addThread = new AddDataThread(counter);
        DecDataThread decThread = new DecDataThread(counter);
        pool.submit(addThread);
        pool.submit(decThread);
        pool.shutdown();
//        Thread t1=new Thread(addThread);
//        Thread t2=new Thread(decThread);
//        t1.start();
//        t2.start();
//        t1.join();
//        t2.join();
        System.out.println(counter.getCount());
        long endTime = System.currentTimeMillis();
        System.out.println("用时：" + (endTime - startTime));
    }
}

//多线程争用的数据类
class Counter2 {
    //使用AtomicInteger代替基本数据类型
    LongAdder count = new LongAdder();

    public int getCount() {
        return count.intValue();
    }


    public void add() {
        count.add(1);
    }

    public void dec() {
        count.decrement();
    }
}

//争用数据做加操作的线程
class AddDataThread2 implements Runnable {

    Counter2 counter;

    public AddDataThread2(Counter2 counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0; i < CasClass.LOOP; ++i) {
            counter.add();
            System.out.println(Thread.currentThread().getName() + " " + counter.getCount());
        }
    }
}

//争用数据做减法操作的线程
class DecDataThread2 implements Runnable {

    Counter2 counter;

    public DecDataThread2(Counter2 counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int j = 0; j < CasClass.LOOP; j++) {
            counter.dec();
            System.out.println(Thread.currentThread().getName() + " " + counter.getCount());
        }
    }
}

class CasClass2 {
    final static int LOOP = 10000;

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService pool = Executors.newFixedThreadPool(12);
        Counter2 counter = new Counter2();
        AddDataThread2 addThread = new AddDataThread2(counter);
        DecDataThread2 decThread = new DecDataThread2(counter);
        pool.submit(addThread);
        pool.submit(decThread);
        pool.shutdown();
//        Thread t1=new Thread(addThread);
//        Thread t2=new Thread(decThread);
//        t1.start();
//        t2.start();
//        t1.join();
//        t2.join();
        System.out.println(counter.getCount());
        long endTime = System.currentTimeMillis();
        System.out.println("用时：" + (endTime - startTime));
    }

}
