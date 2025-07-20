package org.example;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

public class FizzBuzzService {
    private final int n;
    private int current = 1;
    private final Semaphore numberSemaphore = new Semaphore(1);
    private final Semaphore fizzSemaphore = new Semaphore(0);
    private final Semaphore buzzSemaphore = new Semaphore(0);
    private final Semaphore fizzBuzzSemaphore = new Semaphore(0);

    public FizzBuzzService(int n) {
        this.n = n;
    }

    public void number(IntConsumer printNumber) throws InterruptedException {
        while (true) {
            numberSemaphore.acquire();
            if (current > n) {
                fizzSemaphore.release();
                buzzSemaphore.release();
                fizzBuzzSemaphore.release();
                break;
            }

            if (current % 3 == 0 && current % 5 == 0) {
                fizzBuzzSemaphore.release();
            } else if (current % 3 == 0) {
                fizzSemaphore.release();
            } else if (current % 5 == 0) {
                buzzSemaphore.release();
            } else {
                printNumber.accept(current);
                next();
            }
        }
    }

    public void fizz(Runnable printFizz) throws InterruptedException {
        while (true) {
            fizzSemaphore.acquire();
            if (current > n) break;
            printFizz.run();
            next();
        }
    }

    public void buzz(Runnable printBuzz) throws InterruptedException {
        while (true) {
            buzzSemaphore.acquire();
            if (current > n) break;
            printBuzz.run();
            next();
        }
    }

    public void fizzBuzz(Runnable printFizzBuzz) throws InterruptedException {
        while (true) {
            fizzBuzzSemaphore.acquire();
            if (current > n) break;
            printFizzBuzz.run();
            next();
        }
    }

    private void next() {
        current++;
        numberSemaphore.release();
    }
}
