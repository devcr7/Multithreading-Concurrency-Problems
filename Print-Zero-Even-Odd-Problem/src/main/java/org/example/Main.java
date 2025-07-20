package org.example;

import java.util.Scanner;
import java.util.function.IntConsumer;

public class Main {
    public static void main(String[] args) {
        Scanner scanner  = new Scanner(System.in);
        System.out.print("Enter the value of the n: ");
        int n = scanner.nextInt();

        ZeroEvenOdd zeroEvenOdd = new ZeroEvenOdd(n);
        ZeroEvenOddSemaphore zeroEvenOddSemaphore = new ZeroEvenOddSemaphore(n);
        IntConsumer printer = number -> System.out.print(number + " ");

        Thread zeroThread = new Thread(() -> {
            try {
                zeroEvenOddSemaphore.zero(printer);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "ZeroThread");

        Thread oddThread = new Thread(() -> {
            try {
                zeroEvenOddSemaphore.odd(printer);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "OddThread");

        Thread evenThread = new Thread(() -> {
            try {
                zeroEvenOddSemaphore.even(printer);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "EvenThread");

        zeroThread.start();
        oddThread.start();
        evenThread.start();

        try {
            zeroThread.join();
            oddThread.join();
            evenThread.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        }

        /// Using Semaphore allows explicit control over which thread runs next by acquiring and releasing permits,
        /// making thread coordination more predictable and less error-prone than synchronized with wait/notify,
        /// which depends on shared flags and notifyAll. Semaphores help cleanly separate thread responsibility (zero, even, odd) and avoid unnecessary thread wake-ups.
    }
}