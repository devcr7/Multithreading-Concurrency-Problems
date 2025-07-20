package org.example;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

public class ZeroEvenOddSemaphore {
    private final int n;
    private final Semaphore zeroSemaphore = new Semaphore(1);
    private final Semaphore oddSemaphore = new Semaphore(0);
    private final Semaphore evenSemaphore = new Semaphore(0);
    private int current = 1;

    public ZeroEvenOddSemaphore(int n) {
        this.n = n;
    }

    public void zero(IntConsumer printNumber) throws InterruptedException {
        while (true) {
            zeroSemaphore.acquire();
            if (current > n) {
                oddSemaphore.release();
                evenSemaphore.release();
                return;
            }
            printNumber.accept(0);
            if ((current & 1) == 1) {
                oddSemaphore.release();
            } else {
                evenSemaphore.release();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        while (true) {
            evenSemaphore.acquire();
            if (current > n) {
                zeroSemaphore.release();
                return;
            }
            printNumber.accept(current++);
            zeroSemaphore.release();
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        while (true) {
            oddSemaphore.acquire();
            if (current > n) {
                zeroSemaphore.release();
                return;
            }
            printNumber.accept(current++);
            zeroSemaphore.release();
        }
    }
}
