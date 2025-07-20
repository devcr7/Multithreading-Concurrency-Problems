package org.example;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

public class ZeroEvenOddSemaphore {
    private final int n;
    private final Semaphore zeroSemaphore;
    private final Semaphore oddSemaphore;
    private final Semaphore evenSemaphore;
    private int current;

    public ZeroEvenOddSemaphore(int n) {
        this.n = n;
        this.zeroSemaphore = new Semaphore(1);
        this.oddSemaphore = new Semaphore(0);
        this.evenSemaphore = new Semaphore(0);
        this.current = 1;
    }

    public void zero(IntConsumer printNumber) throws InterruptedException {
        while (true) {
            zeroSemaphore.acquire(); // this line replaces synchronized(this)
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
