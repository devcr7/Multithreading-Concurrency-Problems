package org.example;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of chairs in the waiting room:");
        int numberOfChairs = scanner.nextInt();

        System.out.print("Enter the number of customers:");
        int numberOfCustomers = scanner.nextInt();


        SleepingBarber sleepingBarber = new SleepingBarber(numberOfChairs);
        List<Thread> allVirtualThreads = new ArrayList<>();

        Thread barberThread = Thread.ofVirtual()
                .name("Barber ")
                .start(() -> {
                    try {
                        sleepingBarber.barber();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Barber thread interrupted");
                    }
                });

        allVirtualThreads.add(barberThread);

        for (int i = 0; i < numberOfCustomers; i++) {
            final int customerId = i + 1;
            Thread customerThread = Thread.ofVirtual()
                    .name("Customer " + customerId)
                    .start(() -> {
                        try {
                            sleepingBarber.customer(customerId);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("Customer " + customerId + " thread interrupted");
                        }
                    });
            allVirtualThreads.add(customerThread);
        }

        for(Thread virtualThread: allVirtualThreads) {
            try {
                virtualThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted: " + virtualThread.getName());
            }
        }

    }
}