package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UberRide ride = new UberRide();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter total number of riders: ");
        int totalRiders = scanner.nextInt();
        System.out.println();

        System.out.print("Enter total number of drivers: ");
        int totalDrivers = scanner.nextInt();
        System.out.println();

        Runnable board = () -> System.out.println(Thread.currentThread().getName() + " is boarding the ride.");

        List<Thread> allThreads = new ArrayList<>();

        for (int i = 0; i < totalRiders; i++) {
            int riderId = i + 1;
            Thread t = Thread.ofVirtual()
                    .name("Rider-" + riderId)
                    .start(() -> {
                        try {
                            ride.boardRider(board);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("Thread interrupted: " + e.getMessage());
                        }
                    });
            allThreads.add(t);
        }

        for (int i = 0; i < totalDrivers; i++) {
            int driverId = i + 1;
            Thread t = Thread.ofVirtual()
                    .name("Driver-" + driverId)
                    .start(() -> {
                        try {
                            ride.boardDriver(board);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("Thread interrupted: " + e.getMessage());
                        }
                    });
            allThreads.add(t);
        }

        // ✅ Wait for all virtual threads to finish
        // since virtual threads are deamon threads, we need to ensure they complete
        // before the main thread exits.
        // as jvm doesn't care about daemon threads, we need to wait for them explicitly.
        for (Thread t : allThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Join interrupted: " + e.getMessage());
            }
        }

        System.out.println("All threads completed.");
    }
}
