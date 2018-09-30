package info.multithreading.problems;

public class Barrier {

    int count = 0;
    int numLeaving = 0;
    int totalThreads;

    public Barrier(int totalThreads) {
        this.totalThreads = totalThreads;
    }

    public synchronized void await() throws InterruptedException {
        while (numLeaving > 0) {
            wait();
        }
        count++;
        if (count == totalThreads) {
            notifyAll();
        } else {
            while (count < totalThreads) {
                wait();
            }
        }
        numLeaving++;
        if (numLeaving == totalThreads) {
            numLeaving = 0;
            count = 0;
            notifyAll();
        }
    }
}
