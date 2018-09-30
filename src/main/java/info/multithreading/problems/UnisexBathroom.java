package info.multithreading.problems;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UnisexBathroom {

    private static final int MAX_NUM_OF_PEOPLE_IN_BATHROOM = 3;
    private static final Random RANDOM = new Random();

    private static class Gender {
        private int numInBathroom = 0;
        private int numWaiting = 0;
        private boolean canEnterBathroom = false;
        private Condition useBathroom;

        private Gender(Condition useBathroom) {
            this.useBathroom = useBathroom;
        }
    }

    private Lock lock = new ReentrantLock();
    private Gender men = new Gender(lock.newCondition());
    private Gender women = new Gender(lock.newCondition());

    void maleUseBathroom(String name) throws InterruptedException {
        useBathroom(name, men, women);
    }

    void femaleUseBathroom(String name) throws InterruptedException {
        useBathroom(name, women, men);
    }

    private void useBathroom(
            String name, Gender requesting, Gender other) throws InterruptedException {
        lock.lock();
        try {
            if (other.numInBathroom > 0 ||
                    requesting.numInBathroom == MAX_NUM_OF_PEOPLE_IN_BATHROOM ||
                    other.numWaiting > 0) {
                requesting.numWaiting++;
                requesting.canEnterBathroom = false;
                while (!requesting.canEnterBathroom) {
                    requesting.useBathroom.await();
                }
                requesting.numWaiting--;
            }
            requesting.numInBathroom++;
            if (requesting.numInBathroom == MAX_NUM_OF_PEOPLE_IN_BATHROOM) {
                requesting.canEnterBathroom = false;
            }
            System.out.println(name + " is using bathroom. Current employees in bathroom = " +
                    (requesting.numInBathroom + other.numInBathroom));
            System.out.flush();
        } finally {
            lock.unlock();
        }

        useBathroom();

        lock.lock();
        try {
            requesting.numInBathroom--;
            System.out.println(name + " is leaving bathroom. Current employees in bathroom = " +
                    (requesting.numInBathroom + other.numInBathroom));
            System.out.flush();
            if (other.numWaiting > 0) {
                if (requesting.numInBathroom == 0) {
                    other.canEnterBathroom = true;
                    int numToEnter = Math.min(other.numWaiting, MAX_NUM_OF_PEOPLE_IN_BATHROOM);
                    for (int i = 0; i < numToEnter; ++i) {
                        other.useBathroom.signal();
                    }
                }
            } else {
                requesting.canEnterBathroom = true;
                requesting.useBathroom.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    void useBathroom() throws InterruptedException {
        Thread.sleep((1 + RANDOM.nextInt(3)) * 1000);
    }
}