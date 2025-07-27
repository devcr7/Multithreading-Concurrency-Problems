package org.example;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SleepingBarber {

    private final int TOTAL_CHAIRS;

    private final Semaphore barberSemaphore;     // Signals barber to wake up
    private final Semaphore customerSemaphore;   // Signals customer haircut is done
    private final Semaphore chairAccess;         // Controls access to the chair count

    private int waitingCustomers = 0;

    public SleepingBarber(int numberOfChairs) {
        this.TOTAL_CHAIRS = numberOfChairs;
        barberSemaphore = new Semaphore(0);
        customerSemaphore = new Semaphore(0);
        chairAccess = new Semaphore(1);
    }

    public void barber() throws InterruptedException {
        while (true) {
            // Wait for a customer up to 5 seconds before closing
            if (!barberSemaphore.tryAcquire(5, TimeUnit.SECONDS)) {
                System.out.println("Barber is closing shop due to inactivity.");
                return;
            }

            chairAccess.acquire();
            waitingCustomers--;
            System.out.println("Barber is cutting hair. Customers waiting: " + waitingCustomers);
            chairAccess.release();

            Thread.sleep(1000); // Simulate haircut time
            customerSemaphore.release(); // Notify one customer
        }
    }

    public void customer(int customerId) throws InterruptedException {
        chairAccess.acquire();
        if (waitingCustomers < TOTAL_CHAIRS) {
            waitingCustomers++;
            System.out.println("Customer " + customerId + " is waiting. Waiting customers: " + waitingCustomers);
            chairAccess.release();

            barberSemaphore.release();         // Notify barber
            customerSemaphore.acquire();       // Wait for haircut
        } else {
            chairAccess.release();
            System.out.println("Customer " + customerId + " left. No free chairs.");
        }
    }
}
