package info.multithreading.problems;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class UberSeatingProblem {

    private class Party {
        private int numMembers = 0;
        private Semaphore semaphore;

        private Party(Semaphore semaphore) {
            this.semaphore = semaphore;
        }
    }

    private CyclicBarrier barrier = new CyclicBarrier(4);
    private ReentrantLock lock = new ReentrantLock();
    private Party republicans = new Party(new Semaphore(0));
    private Party democrats = new Party(new Semaphore(0));

    void seatDemocrat() throws InterruptedException, BrokenBarrierException {
        seat(democrats, republicans);
    }

    void seatRepublican() throws InterruptedException, BrokenBarrierException {
        seat(republicans, democrats);
    }

    private void seat(Party party, Party other) throws InterruptedException, BrokenBarrierException {
        boolean leader = false;
        lock.lock();
        try {
            party.numMembers++;
            if (party.numMembers == 4) {
                leader = true;
                party.semaphore.release(3);
                party.numMembers -= 4;
            } else if (party.numMembers == 2 && other.numMembers >= 2) {
                leader = true;
                party.semaphore.release(1);
                other.semaphore.release(2);
                party.numMembers -= 2;
                other.numMembers -= 2;
            } else {
                lock.unlock();
                party.semaphore.acquire();
            }
            seated();
        barrier.await();
        if (leader) {
            drive();
        }
        } finally {
            if (leader) {
                lock.unlock();
            }
        }
    }

    void seated() {
        System.out.println(Thread.currentThread().getName() + " is seated");
        System.out.flush();
    }

    void drive() {
        System.out.println("Driving, leader: " + Thread.currentThread().getName());
        System.out.flush();
    }
}
