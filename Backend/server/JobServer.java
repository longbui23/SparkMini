package server;

import core.JobManager;
import core.Operation;
import common.DAG;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class JobServer {

    private int port;
    private JobManager jobManager;

    // constructor
    public JobServer(int port, int numExecutors) {
        this.port = port;
        this.jobManager = new JobManager(numExecutors);
    }

    // start server socker
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[JobServer] Listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[JobServer] Accepted connection from " + clientSocket.getRemoteSocketAddress());

                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            jobManager.shutdown();
        }
    }

    // handle client request
    private void handleClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
        ) {
            // Read full JSON payload from client
            StringBuilder jsonInput = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                jsonInput.append(line);
            }

            // Parse JSON manually (or use GSON/Jackson if preferred)
            Map<String, Object> payload = parseInput(jsonInput.toString());

            List<Object> data = (List<Object>) payload.get("data");
            List<String> opStrings = (List<String>) payload.get("operations");

            List<Operation> operations = parseOperations(opStrings);
            DAG dag = new DAG(operations);

            List<Object> result = jobManager.runJob(data, dag);

            out.write(result.toString());
            out.newLine();
            out.flush();

            System.out.println("[JobServer] Job done, result sent.");

        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> parseInput(String json) {
        // Simulate JSON parsing (use proper parser like Gson for production)
        Map<String, Object> result = new HashMap<>();

        List<Object> data = new ArrayList<>();
        List<String> ops = new ArrayList<>();

        if (json.contains("data")) {
            String dataStr = json.split("\"data\":\\[")[1].split("]")[0];
            for (String x : dataStr.split(",")) {
                data.add(Integer.parseInt(x.trim()));
            }
        }

        if (json.contains("operations")) {
            String opsStr = json.split("\"operations\":\\[")[1].split("]")[0];
            for (String s : opsStr.split(",")) {
                ops.add(s.replace("\"", "").trim());
            }
        }

        result.put("data", data);
        result.put("operations", ops);
        return result;
    }

    private List<Operation> parseOperations(List<String> ops) {
        List<Operation> parsed = new ArrayList<>();
        for (String op : ops) {
            if (op.startsWith("map:")) {
                parsed.add(new Operation("map", evalLambda(op.substring(4))));
            } else if (op.startsWith("filter:")) {
                parsed.add(new Operation("filter", evalLambda(op.substring(7))));
            }
        }
        return parsed;
    }
}
