package info.multithreading.problems;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TokenBucketFilter {

    private long numOfTokens = 0;
    private ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    private long lastBucketUpdate = -1;
    private int maxTokens;
    private ScheduledFuture<?> future;

    public TokenBucketFilter(int maxTokens) {
        this.maxTokens = maxTokens;
        future = executor.scheduleAtFixedRate(() -> {
            try {
                addToken();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, 1, 1, TimeUnit.SECONDS);
        lastBucketUpdate = System.currentTimeMillis();
    }

    public void getToken() throws InterruptedException {
        lock.lock();
        try {
            while (numOfTokens == 0) {
                notEmpty.await();
            }
            numOfTokens--;
            System.out.println(String.format("After get: %d/%d", numOfTokens, maxTokens));
            System.out.flush();
            notFull.signal();
        } finally {
            lock.unlock();
        }
    }

    private void addToken() throws InterruptedException {
        lock.lock();
        try {
            while (numOfTokens == maxTokens) {
                notFull.await();
            }
            long currentTime = System.currentTimeMillis();
            long prevTokens = numOfTokens;
            numOfTokens = getNextNumOfTokens(currentTime);
            if (prevTokens != numOfTokens) {
                lastBucketUpdate = currentTime;
            }
            System.out.println(String.format("After add: %d/%d", numOfTokens, maxTokens));
            System.out.flush();
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private long getNextNumOfTokens(long currentTimestamp) {
        long secondsSinceLastUpdate = TimeUnit.MILLISECONDS.toSeconds(
                currentTimestamp - lastBucketUpdate);
        return Math.min(maxTokens, numOfTokens + secondsSinceLastUpdate);
    }

    public void stop() {
        future.cancel(false);
    }
}