package driver; 

import java.util.function.Function;

public class Operation { 

    private String type; // e.g., "map", "filter"
    private Function<Object, Object> func;

    public Operation(String type, Function<Object, Object> func) {
        this.type = type;
        this.func = func;
    }

    public String getType() {
        return type;  
    }

    public Function<Object, Object> getFunc() {
        return func;
    }
}
