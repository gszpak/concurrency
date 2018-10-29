package info.multithreading.problems;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BarberShopProblem {

    private final int CHAIRS = 3;
    private int waitingCustomers = 0;
    private ReentrantLock lock = new ReentrantLock();
    private Condition barberBusy = lock.newCondition();
    private Condition noCustomers = lock.newCondition();
    private String currentCustomerId = null;

    void customerWalksIn() throws InterruptedException {
        String customerId = Thread.currentThread().getName();
        lock.lock();
        try {
            if (waitingCustomers == CHAIRS) {
                System.out.println(
                        String.format("Barber shop full, customer %s leaving", customerId));
                System.out.flush();
                return;
            }
            waitingCustomers++;
            while (currentCustomerId != null) {
                barberBusy.await();
            }
            currentCustomerId = customerId;
            noCustomers.signal();
        } finally {
            lock.unlock();
        }
    }

    void barber() throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (currentCustomerId == null) {
                    noCustomers.await();
                }
                waitingCustomers--;
                System.out.println(String.format("Cutting customer %s", currentCustomerId));
                Thread.sleep(1000);
                currentCustomerId = null;
                barberBusy.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}