package scheduler;

import java.util.*;
import common.Task;
import common.DAG;

public class Scheduler {

    private int numExecutors;

    public Scheduler(int numExecutors) {
        this.numExecutors = numExecutors;
    }

    /**
     * task & data per partitions
     */
    public List<Task> scheduleTasks(List<Object> data, DAG dag) {
        List<List<Object>> partitions = partitionData(data, numExecutors);
        List<Task> tasks = new ArrayList<>();

        int taskId = 0;
        for (List<Object> partition : partitions) {
            Task task = new Task(taskId++, partition, dag.getOperations());
            tasks.add(task);
        }

        return tasks;
    }

    /**
     * data partitioning
     */
    private List<List<Object>> partitionData(List<Object> data, int numPartitions) {
        List<List<Object>> partitions = new ArrayList<>();
        int partitionSize = (int) Math.ceil(data.size() / (double) numPartitions);

        for (int i = 0; i < data.size(); i += partitionSize) {
            partitions.add(data.subList(i, Math.min(i + partitionSize, data.size())));
        }

        return partitions;
    }
}
