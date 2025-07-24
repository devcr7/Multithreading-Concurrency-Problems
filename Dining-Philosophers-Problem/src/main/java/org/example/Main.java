package org.example;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of philosophers: ");
        int philosophersCount = scanner.nextInt();

        DiningPhilosophers diningPhilosophers = new DiningPhilosophers(philosophersCount);
        Thread[] philosophers = new Thread[philosophersCount];

        for (int i = 0; i < philosophersCount; i++) {
            final int philosopherId = i;
            philosophers[i] = new Thread(() -> {
                try {
                    diningPhilosophers.dine(philosopherId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Philosopher " + philosopherId + " was interrupted.");
                }
            }, "Philosopher-" + i);
        }

        for (Thread philosopher: philosophers) {
            philosopher.start();
        }

        for (Thread philosopher: philosophers) {
            try {
                philosopher.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Main thread was interrupted.");
            }
        }
    }
}