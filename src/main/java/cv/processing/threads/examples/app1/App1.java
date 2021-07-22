package cv.processing.threads.examples.app1;

public class App1 {
    private static Integer val = 0;

    public static void main(String[] args) {

        System.out.println(App1.getVal());

        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 " + App1.getVal());
        });
        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 " + App1.getVal());
        });

        thread1.start();
        thread2.start();
        //thread1 2
        //thread2 2


    }

    public static int getVal() {
        val++;
        return val;
    }
}
