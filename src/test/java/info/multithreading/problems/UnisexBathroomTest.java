package info.multithreading.problems;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UnisexBathroomTest {

    @Test
    public void runTest() throws InterruptedException {

        final UnisexBathroom unisexBathroom = new UnisexBathroom();

        Thread female1 = new Thread(new Runnable() {

            public void run() {
                try {
                    unisexBathroom.femaleUseBathroom("Lisa");
                } catch (InterruptedException ie) {

                }
            }
        });

        Thread female2 = new Thread(new Runnable() {

            public void run() {
                try {
                    unisexBathroom.femaleUseBathroom("Joanna");
                } catch (InterruptedException ie) {

                }
            }
        });

        Thread female3 = new Thread(new Runnable() {

            public void run() {
                try {
                    unisexBathroom.femaleUseBathroom("Anna");
                } catch (InterruptedException ie) {

                }
            }
        });

        Thread female4 = new Thread(new Runnable() {

            public void run() {
                try {
                    unisexBathroom.femaleUseBathroom("Kate");
                } catch (InterruptedException ie) {

                }
            }
        });

        Thread male1 = new Thread(new Runnable() {

            public void run() {
                try {
                    unisexBathroom.maleUseBathroom("John");
                } catch (InterruptedException ie) {

                }
            }
        });

        Thread male2 = new Thread(new Runnable() {

            public void run() {
                try {
                    unisexBathroom.maleUseBathroom("Bob");
                } catch (InterruptedException ie) {

                }
            }
        });

        Thread male3 = new Thread(new Runnable() {

            public void run() {
                try {
                    unisexBathroom.maleUseBathroom("Anil");
                } catch (InterruptedException ie) {

                }
            }
        });

        Thread male4 = new Thread(new Runnable() {

            public void run() {
                try {
                    unisexBathroom.maleUseBathroom("Wentao");
                } catch (InterruptedException ie) {

                }
            }
        });

        List<Thread> threads = new ArrayList<Thread>() {{
            add(female1);
            add(female2);
            add(female3);
            add(female4);
            add(male1);
            add(male2);
            add(male3);
            add(male4);
        }};
        Collections.shuffle(threads);
        threads.forEach(Thread::start);

        female1.join();
        female2.join();
        female3.join();
        female4.join();
        male1.join();
        male2.join();
        male3.join();
        male4.join();
    }
}
