package info.multithreading.problems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class DiningPhilosophers2 {

    private static Random random = new Random(System.currentTimeMillis());

    private List<Semaphore> forks = new ArrayList<Semaphore>() {{
        add(new Semaphore(1));
        add(new Semaphore(1));
        add(new Semaphore(1));
        add(new Semaphore(1));
        add(new Semaphore(1));
    }};

    public DiningPhilosophers2() {}

    void getLeftFork(int id) throws InterruptedException {
        forks.get(getLeftForkId(id)).acquire();
    }

    void getRightFork(int id) throws InterruptedException {
        forks.get(getRightForkId(id)).acquire();
    }

    void putLeftFork(int id) throws InterruptedException {
        forks.get(getLeftForkId(id)).release();
    }

    void putRightFork(int id) throws InterruptedException {
        forks.get(getRightForkId(id)).release();
    }

    void contemplate() throws InterruptedException {
        Thread.sleep(random.nextInt(500));
    }

    private int getLeftForkId(int id) {
        return (id - 1 + 5) % 5;
    }

    private int getRightForkId(int id) {
        return id;
    }

    void eat(int id) throws InterruptedException {
        if (id % 2 == 0) {
            getRightFork(id);
            getLeftFork(id);
        } else {
            getLeftFork(id);
            getRightFork(id);
        }
        System.out.println(String.format("Philosopher %d is eating", id));
        System.out.flush();
        Thread.sleep(1000);
        System.out.println(String.format("Philosopher %d finished", id));
        System.out.flush();
        if (id % 2 == 0) {
            putLeftFork(id);
            putRightFork(id);
        } else {
            putRightFork(id);
            putLeftFork(id);
        }
    }

    public void lifecycleOfPhilosopher(int id) throws InterruptedException {
        while (true) {
            contemplate();
            eat(id);
        }
    }
}
