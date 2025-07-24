package org.example;

import java.util.concurrent.Semaphore;

public class DiningPhilosophers {
    private final int philosophersCount;

    private final Semaphore arbitrator;
    private final Semaphore[] forks;

    public DiningPhilosophers(int philosophersCount) {
        this.philosophersCount = philosophersCount;
        this.arbitrator = new Semaphore(philosophersCount - 1);
        this.forks = new Semaphore[philosophersCount];
        for (int i = 0; i < philosophersCount; i++) {
            forks[i] = new Semaphore(1); // Each fork is initially available
        }
    }

    public void dine(int philosophersId) throws InterruptedException {
        int leftFork = philosophersId;
        int rightFork = (philosophersId + 1) % philosophersCount;

        while (true) {
            think();
            // Request permission to eat
            arbitrator.acquire();
            try {
                // Since both forks must be acquired, we need to acquire them in a specific order to avoid deadlock
                forks[leftFork].acquire();
                forks[rightFork].acquire();
                eat();
            } finally {
                forks[leftFork].release(); // Release left fork
                forks[rightFork].release(); // Release right fork
                arbitrator.release(); // Release permission to eat
            }
        }
    }

    private void think() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " is thinking...");
        Thread.sleep(1000); // Simulate thinking time
    }

    private void eat() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " is eating...");
        Thread.sleep(1000); // Simulate eating time
    }
}
