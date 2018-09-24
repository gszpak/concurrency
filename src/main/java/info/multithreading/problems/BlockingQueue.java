package info.multithreading.problems;

class BlockingQueue<T> {

    private final int capacity;
    private final T[] items;
    private int writeIndex;
    private int readIndex;
    private int elemsInQueue = 0;

    @SuppressWarnings("unchecked")
    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        this.items = (T[]) new Object[capacity];
        this.writeIndex = 0;
        this.readIndex = 0;
        this.elemsInQueue = 0;
    }

    public void enqueue(T item) throws InterruptedException {
        assert elemsInQueue <= capacity && elemsInQueue >= 0;
        synchronized (items) {
            while (isFull()) {
                items.wait();
            }
            items[writeIndex] = item;
            writeIndex = (writeIndex + 1) % capacity;
            elemsInQueue++;
            items.notify();
        }
    }

    public T dequeue() throws InterruptedException {
        assert elemsInQueue <= capacity && elemsInQueue >= 0;
        T item;
        synchronized (items) {
            while (isEmpty()) {
                items.wait();
            }
            item = items[readIndex];
            items[readIndex] = null;
            readIndex = (readIndex + 1) % capacity;
            elemsInQueue--;
            items.notify();
        }
        return item;
    }

    private boolean isFull() {
        return elemsInQueue == capacity;
    }

    private boolean isEmpty() {
        return elemsInQueue == 0;
    }
}
