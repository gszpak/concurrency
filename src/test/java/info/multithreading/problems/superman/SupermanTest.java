package info.multithreading.problems.superman;

import org.junit.Test;

public class SupermanTest {

    @Test
    public void testNaiveButCorrect() {
        SupermanNaiveButCorrect superman = SupermanNaiveButCorrect.getInstance();
        superman.fly();
    }

    @Test
    public void test() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            Superman s = Superman.getInstance();
            s.fly();
        });
        Thread t2 = new Thread(() -> {
            Superman s = Superman.getInstance();
            s.fly();
        });
        Thread t3 = new Thread(() -> {
            Superman s = Superman.getInstance();
            s.fly();
        });
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
    }
}
