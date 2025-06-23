package common;

import java.util.List;
import driver.Operation;

public class DAG {
    private List<Operation> operations;

    // constructor
    public DAG(List<Operation> operations) {
        this.operations = operations;
    }

    public List<Operation> getOperations() {
        return operations;
    }
}
