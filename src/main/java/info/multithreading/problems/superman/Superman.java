package info.multithreading.problems.superman;

public class Superman {

    private static volatile Superman INSTANCE;
    private static final Object LOCK = new Object();

    private Superman() {
        System.out.println("Constructing Superman");
        System.out.flush();
    }

    public static Superman getInstance() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new Superman();
                }
            }
        }
        return INSTANCE;
    }

    public void fly() {
        System.out.println("I am Superman & I can fly !");
        System.out.flush();
    }
}
