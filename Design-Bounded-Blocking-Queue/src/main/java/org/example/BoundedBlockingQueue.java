package org.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class BoundedBlockingQueue {
    private final Queue<Integer> queue;
    private final int capacity;
    private final Semaphore slots;
    private final Semaphore items;
    private final Semaphore mutex;

    public BoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
        this.slots = new Semaphore(capacity);
        this.items = new Semaphore(0);
        this.mutex = new Semaphore(1);
    }

    public void enqueue(int item) throws InterruptedException {
        slots.acquire(); // Wait for a slot to be available
        mutex.acquire(); // Lock the queue for exclusive access
        try {
            queue.offer(item);
        } finally {
            mutex.release(); // Release the lock
            items.release(); // Signal that an item has been added
        }
    }

    public int dequeue() throws InterruptedException {
        items.acquire(); // Wait for an item to be available
        mutex.acquire(); // Lock the queue for exclusive access
        try {
            return queue.poll(); // Remove and return the item from the queue
        } finally {
            mutex.release(); // Release the lock
            slots.release(); // Signal that a slot has been freed
        }
    }

    public int size() throws InterruptedException {
        mutex.acquire();         // Ensure thread-safe read
        int size = queue.size();
        mutex.release();
        return size;
    }

}
