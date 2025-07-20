package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a number to check FizzBuzz: ");
        int input = scanner.nextInt();

        FizzBuzzService service = new FizzBuzzService(input);

        Thread numberThread = new Thread(() -> {
            try {
                service.number(number -> System.out.print(number + " "));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread fizzThread = new Thread(() -> {
            try {
                service.fizz(() -> System.out.print("Fizz "));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread buzzThread = new Thread(() -> {
            try {
                service.buzz(() -> System.out.print("Buzz "));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread fizzBuzzThread = new Thread(() -> {
            try {
                service.fizzBuzz(() -> System.out.print("FizzBuzz "));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        numberThread.start();
        fizzThread.start();
        buzzThread.start();
        fizzBuzzThread.start();

        try {
            numberThread.join();
            fizzThread.join();
            buzzThread.join();
            fizzBuzzThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}