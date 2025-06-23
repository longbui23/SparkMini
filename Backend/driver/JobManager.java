package core;

import scheduler.Scheduler;
import common.Task;
import common.DAG;

import java.util.*;
import java.util.concurrent.*;

public class JobManager {

    private ExecutorService executorPool;
    private int numExecutors;
    private Scheduler scheduler;

    public JobManager(int numExecutors) {
        this.numExecutors = numExecutors;
        this.executorPool = Executors.newFixedThreadPool(numExecutors);
        this.scheduler = new Scheduler(numExecutors);
    }

    /**
     * Run a DAG over data using a scheduler and executor pool
     */
    public List<Object> runJob(List<Object> data, DAG dag) throws InterruptedException, ExecutionException {
        List<Task> tasks = scheduler.scheduleTasks(data, dag);

        List<Future<List<Object>>> futures = new ArrayList<>();

        // Submit tasks to executor pool
        for (Task task : tasks) {
            futures.add(executorPool.submit(() -> {
                Executor executor = new Executor(task.getTaskId());
                return executor.runTask(task.getDataPartition(), task.getOperations());
            }));
        }

        // Collect results
        List<Object> finalResult = new ArrayList<>();
        for (Future<List<Object>> future : futures) {
            finalResult.addAll(future.get());
        }

        return finalResult;
    }

    public void shutdown() {
        executorPool.shutdown();
    }
}