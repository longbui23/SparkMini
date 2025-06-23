package common;

import java.util.List;
import core.Operation;

public class Task {
    private int taskId;
    private List<Object> dataPartition; // The data chunk to process
    private List<Operation> operations; // The DAG operations for this task

    // constructor
    public Task(int taskId, List<Object> dataPartition, List<Operation> operations) {
        this.taskId = taskId;
        this.dataPartition = dataPartition;
        this.operations = operations;
    }

    public int getTaskId() {
        return taskId;
    }

    public List<Object> getDataPartition() {
        return dataPartition;
    }

    public List<Operation> getOperations() {
        return operations;
    }
}
