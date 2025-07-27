package org.example;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class UberRide {
    private int waitingRiders = 0;
    private int waitingDrivers = 0;

    private final Semaphore riderSemaphore = new Semaphore(0);
    private final Semaphore driverSemaphore = new Semaphore(0);
    private final Lock lock = new ReentrantLock();

    // To signal group formation
    private final CyclicBarrier barrier = new CyclicBarrier(4, () -> {
        System.out.println("🚕 Ride starting with a valid group!");
    });

    public void boardRider(Runnable board) throws InterruptedException {
        boolean isInGroup = false;

        lock.lock();
        try {
            waitingRiders++;

            if (waitingRiders >= 4) {
                riderSemaphore.release(4);
                waitingRiders -= 4;
            } else if (waitingRiders >= 2 && waitingDrivers >= 2) {
                riderSemaphore.release(2);
                driverSemaphore.release(2);
                waitingRiders -= 2;
                waitingDrivers -= 2;
            }
        } finally {
            lock.unlock();
        }

        if (!riderSemaphore.tryAcquire(5, TimeUnit.SECONDS)) {
            System.out.println(Thread.currentThread().getName() + " gave up waiting (Rider).");
            return;
        }

        board.run();

        try {
            barrier.await();
        } catch (Exception e) {
            System.out.println("Barrier broken in Rider: " + e.getMessage());
        }
    }

    public void boardDriver(Runnable board) throws InterruptedException {
        boolean isInGroup = false;

        lock.lock();
        try {
            waitingDrivers++;

            if (waitingDrivers >= 2 && waitingRiders >= 2) {
                riderSemaphore.release(2);
                driverSemaphore.release(2);
                waitingRiders -= 2;
                waitingDrivers -= 2;
            }
        } finally {
            lock.unlock();
        }

        if (!driverSemaphore.tryAcquire(5, TimeUnit.SECONDS)) {
            System.out.println(Thread.currentThread().getName() + " gave up waiting (Driver).");
            return;
        }

        board.run();

        try {
            barrier.await();
        } catch (Exception e) {
            System.out.println("Barrier broken in Driver: " + e.getMessage());
        }
    }
}
