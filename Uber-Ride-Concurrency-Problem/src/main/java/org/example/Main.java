package org.example;

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

        for (int i = 0; i < totalRiders; i++) {
            new Thread(() -> {
                try {
                    ride.boardRider(board);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread interrupted: " + e.getMessage());
                }
            }, "Rider-" + (i + 1)).start();
        }

        for (int i = 0; i < totalDrivers; i++) {
            new Thread(() -> {
                try {
                    ride.boardDriver(board);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread interrupted: " + e.getMessage());
                }
            }, "Driver-" + (i + 1)).start();
        }
    }
}