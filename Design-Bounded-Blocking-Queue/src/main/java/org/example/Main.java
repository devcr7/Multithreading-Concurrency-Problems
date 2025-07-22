package org.example;


public class Main {
    public static void main(String[] args) {
        /// Java’s Semaphore uses atomic variables and AbstractQueuedSynchronizer (AQS) to manage permits safely across threads.
        BoundedBlockingQueue bbq = new BoundedBlockingQueue(5); // queue with capacity 5

        // Producer thread
        Runnable producer = () -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    bbq.enqueue(i);
                    System.out.println(Thread.currentThread().getName() + " Enqueued: " + i);
                    Thread.sleep(100); // simulate some work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // Consumer thread
        Runnable consumer = () -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    int item = bbq.dequeue();
                    System.out.println(Thread.currentThread().getName() + " Dequeued: " + item);
                    Thread.sleep(100); // simulate processing time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // Start multiple producer and consumer threads
        Thread p1 = new Thread(producer, "Producer-1");
        Thread p2 = new Thread(producer, "Producer-2");
        Thread c1 = new Thread(consumer, "Consumer-1");
        Thread c2 = new Thread(consumer, "Consumer-2");

        p1.start();
        p2.start();
        c1.start();
        c2.start();

        try {
            p1.join();
            p2.join();
            c1.join();
            c2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}