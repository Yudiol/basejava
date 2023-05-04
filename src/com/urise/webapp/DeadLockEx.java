package com.urise.webapp;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLockEx {
    public static void main(String[] args) throws InterruptedException {
        Example example = new Example();
        Thread thread1 = new Thread(example::firstThread);
        Thread thread2 = new Thread(example::secondThread);
        thread1.start();
        thread2.start();
    }
}

class Example {
    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();

    void firstThread() {
        lock1.lock();
        System.out.println("firstThread lock1");
        lock2.lock();
        System.out.println("firstThread lock2");
        lock1.unlock();
        lock2.unlock();
    }

    void secondThread() {
        lock2.lock();
        System.out.println("secondThread lock2");
        lock1.lock();
        System.out.println("secondThread lock1");
        lock2.unlock();
        lock1.unlock();
    }

}