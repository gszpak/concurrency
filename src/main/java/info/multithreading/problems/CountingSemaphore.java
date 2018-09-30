package info.multithreading.problems;

public class CountingSemaphore {

    private int maxCount;
    private int permitsAvailable;

    public CountingSemaphore(int count) {
        this.maxCount = count;
        this.permitsAvailable = count;
    }

    public synchronized void acquire() throws InterruptedException {
        checkPermitsAvailable();
        while (permitsAvailable == 0) {
            wait();
        }
        permitsAvailable--;
        System.out.flush();
        notify();
    }

    public synchronized void release() throws InterruptedException {
        checkPermitsAvailable();
        while (permitsAvailable == maxCount) {
            wait();
        }
        permitsAvailable++;
        System.out.flush();
        notify();
    }

    private void checkPermitsAvailable() {
        if (permitsAvailable < 0 || permitsAvailable > maxCount) {
            throw new IllegalStateException(
                    String.format("Permits available: %d out of bounds: [0, %d]",
                            permitsAvailable, maxCount));
        }
    }
}
