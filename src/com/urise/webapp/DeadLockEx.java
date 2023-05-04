package com.urise.webapp;

public class DeadLockEx {


    public static void main(String[] args) throws InterruptedException {
        final Lock lock1 = new Lock("lock1");
        final Lock lock2 = new Lock("lock2");
        Example example = new Example();

        Thread thread1 = new Thread(() -> {
            example.firstThread(lock1, lock2);
        });
        Thread thread2 = new Thread(() -> {
            example.firstThread(lock2, lock1);
        });
        thread1.setName("firstThread");
        thread2.setName("secondThread");
        thread1.start();
        thread2.start();
    }
}

class Example {
    void firstThread(Object lock1, Object lock2) {
        synchronized (lock1) {
            System.out.println(Thread.currentThread().getName() + "  " + lock1);
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + "  " + lock2);
            }
        }
    }
}

class Lock {
    private String name;

    public Lock(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}