package info.multithreading.problems;

public class ReadWriteLock {

    private int numReadersReading = 0;
    private boolean isWriting = false;
    private int numWritersWaiting = 0;
    private int numReadersWaiting = 0;
    private long lastReaderAllowedToEnterTimestamp = 0;
    private boolean writerEntering = true;

    public synchronized void acquireReadLock() throws InterruptedException {
        if (isWriting || numWritersWaiting > 0) {
            long timeArrived = System.currentTimeMillis();
            numReadersWaiting += 1;
            while (timeArrived >= lastReaderAllowedToEnterTimestamp) {
                wait();
            }
            numReadersWaiting--;
        }
        numReadersReading++;
    }

    public synchronized void releaseReadLock() {
        numReadersReading--;
        writerEntering = (numReadersReading == 0);
        if (writerEntering) {
            notifyAll();
        }
    }

    public synchronized void acquireWriteLock() throws InterruptedException {
        if (isWriting || numReadersReading > 0) {
            numWritersWaiting++;
            writerEntering = false;
            while (!writerEntering) {
                wait();
            }
            numWritersWaiting--;
        }
        writerEntering = false;
        isWriting = true;
    }

    public synchronized void releaseWriteLock() {
        isWriting = false;
        writerEntering = (numReadersWaiting == 0);
        lastReaderAllowedToEnterTimestamp = System.currentTimeMillis();
        notifyAll();
    }
}
