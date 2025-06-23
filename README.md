Replicate Spark

Server/
├── driver/
│   ├── JobManager.java         # Master node that manages job execution
│   ├── Operation.java          # Defines DAG operations (map/filter)
├── Executor/
│   └── Executor.java           # Worker node logic to process tasks
│
├── common/
│   ├── DAG.java                # Represents a list of operations
│   └── Task.java               # Represents a unit of work (data + ops)
│
├── scheduler/
│   └── Scheduler.java          # Splits data into tasks and assigns to executors
│
└── server/
    └── JobServer.java         # Socket-based server exposing JobManager API

Client/
├── client.py    # receives client interface to process