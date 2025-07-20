package org.example;

import java.util.function.IntConsumer;

public class ZeroEvenOdd {
    private final int n;
    private int count = 1;
    private boolean zeroTurn = true;

    public ZeroEvenOdd(int n) {
        this.n = n;
    }

    public void zero(IntConsumer printNumber) throws InterruptedException {
        while (true) {
            synchronized (this) {
                while (!zeroTurn) {
                    wait();
                    if (count > n) return;
                }
                printNumber.accept(0);
                zeroTurn = false;
                notifyAll();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (count > n) return;

                while (zeroTurn || count % 2 != 0) {
                    wait();
                    if (count > n) return;
                }

                printNumber.accept(count++);
                zeroTurn = true;
                notifyAll();
            }
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (count > n) return;

                while (zeroTurn || count % 2 == 0) {
                    wait();
                    if (count > n) return;
                }

                printNumber.accept(count++);
                zeroTurn = true;
                notifyAll();
            }
        }
    }
}
