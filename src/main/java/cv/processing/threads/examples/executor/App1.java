package cv.processing.threads.examples.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class App1 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        ExecutorService executorService =
                new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>());

        Runnable runnableTask = () -> {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Callable<String> callableTask = () -> {
            TimeUnit.MILLISECONDS.sleep(300);
            return "Task's execution";
        };

        List<Callable<String>> callableTasks = new ArrayList<>();
        callableTasks.add(callableTask);
        callableTasks.add(callableTask);
        callableTasks.add(callableTask);
    }
}
