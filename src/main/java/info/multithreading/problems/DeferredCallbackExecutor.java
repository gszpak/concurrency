package info.multithreading.problems;

import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeferredCallbackExecutor {

    PriorityQueue<CallBack> callBacks = new PriorityQueue<>(
            (o1, o2) -> (int) (o1.executeAt - o2.executeAt));
    private final Lock lock = new ReentrantLock();
    private final Condition nextCallBackReady = lock.newCondition();

    // Run by the Executor Thread
    public void start() throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (callBacks.isEmpty()) {
                    nextCallBackReady.await();
                }
                long currentTime = System.currentTimeMillis();
                while (!callBacks.isEmpty() && shouldExecute(callBacks.peek(), currentTime)) {
                    CallBack callBack = callBacks.poll();
                    System.out.println(
                            String.format("Executing callback: \"%s\", accuracy: %d ms",
                                    callBack.message, currentTime - callBack.executeAt));
                    System.out.println(callBack.message);
                }
                maybeWaitForExecution(currentTime);
            } finally {
                lock.unlock();
            }
        }
    }

    // Called by Consumer Threads to register callback
    public void registerCallback(CallBack callBack) {
        lock.lock();
        try {
            callBacks.add(callBack);
            if (callBacks.peek() == callBack) {
                nextCallBackReady.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    private void maybeWaitForExecution(long currentTime) throws InterruptedException {
        if (callBacks.isEmpty()) {
            return;
        }
        long awaitTime = callBacks.peek().executeAt - currentTime;
        if (awaitTime <= 0) {
            throw new IllegalStateException(
                    String.format("Trying to wait negative time: %d", awaitTime));
        }
        nextCallBackReady.await(awaitTime, TimeUnit.MILLISECONDS);
    }

    private boolean shouldExecute(CallBack callBack, long currentTime) {
        return callBack.executeAt <= currentTime;
    }

    /**
     * Represents the class which holds the callback. For simplicity instead of
     * executing a method, we print a message.
     */
    static class CallBack {
        long executeAt;
        String message;

        public CallBack(long executeAfter, String message) {
            this.executeAt = System.currentTimeMillis() + executeAfter * 1000;
            this.message = message;
        }
    }
}
