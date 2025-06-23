package executor;

import java.util.*;
import java.util.function.Function;

public class Executor {

    private int executorId;

    // constructor
    public Executor(int executorId) {
        this.executorId = executorId;
    }

    // Execute a task
    public List<Object> runTask(List<Object> partition, List<Operation> dag) {
        List<Object> result = new ArrayList<>(partition);

        for (Operation op : dag) {
            switch (op.getType()) {
                case "map":
                    List<Object> mapped = new ArrayList<>();
                    for (Object x : result) {
                        mapped.add(op.getFunc().apply(x));
                    }
                    result = mapped;
                    break;

                case "filter":
                    List<Object> filtered = new ArrayList<>();
                    for (Object x : result) {
                        if ((Boolean) op.getFunc().apply(x)) {
                            filtered.add(x);
                        }
                    }
                    result = filtered;
                    break;

                default:
                    throw new UnsupportedOperationException("Unknown operation: " + op.getType());
            }
        }

        return result;
    }

    public int getExecutorId() {
        return executorId;
    }
}
