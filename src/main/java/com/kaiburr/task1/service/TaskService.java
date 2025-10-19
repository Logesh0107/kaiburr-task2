package com.kaiburr.task1.service;

import com.kaiburr.task1.model.Task;
import com.kaiburr.task1.model.TaskExecution;
import com.kaiburr.task1.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository repo;

    // Whitelist of allowed commands (first token)
    private static final String[] ALLOWED_CMD = {"echo", "date", "uptime", "whoami", "uname", "ls", "dir"};

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public List<Task> getAll() {
        return repo.findAll();
    }

    public Optional<Task> getById(String id) {
        return repo.findById(id);
    }

    public Task save(Task t) {
        validateCommandSafety(t.getCommand());
        return repo.save(t);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }

    public List<Task> searchByName(String name) {
        return repo.findByNameContainingIgnoreCase(name);
    }

    public TaskExecution runCommandForTask(String id) throws Exception {
        Task task = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        String command = task.getCommand();
        validateCommandSafety(command);

        Instant start = Instant.now();

        ProcessBuilder pb;

        // ✅ Fixed: Cross-platform handling (Windows vs Linux)
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            pb = new ProcessBuilder("cmd.exe", "/c", command);
        } else {
            String[] tokens = command.split("\\s+");
            pb = new ProcessBuilder(tokens);
        }

        pb.redirectErrorStream(true);
        Process process = pb.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();
        Instant end = Instant.now();

        TaskExecution exec = new TaskExecution(start, end, "exitCode=" + exitCode + "\n" + output.toString());
        task.getTaskExecutions().add(exec);
        repo.save(task);

        return exec;
    }

    // ✅ Validate command before execution
    private void validateCommandSafety(String command) {
        if (command == null || command.trim().isEmpty()) {
            throw new IllegalArgumentException("Command is empty");
        }

        String forbidden = ";|&$`<>\\";
        for (char c : forbidden.toCharArray()) {
            if (command.indexOf(c) >= 0) {
                throw new IllegalArgumentException("Command contains forbidden character: " + c);
            }
        }

        String first = command.trim().split("\\s+")[0];
        boolean ok = false;
        for (String a : ALLOWED_CMD) {
            if (a.equalsIgnoreCase(first)) {
                ok = true;
                break;
            }
        }
        if (!ok) {
            throw new IllegalArgumentException("Command not allowed. Allowed commands: echo, date, uptime, whoami, uname, ls, dir");
        }
    }
}
