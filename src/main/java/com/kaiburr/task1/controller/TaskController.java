package com.kaiburr.task1.controller;

import com.kaiburr.task1.model.Task;
import com.kaiburr.task1.model.TaskExecution;
import com.kaiburr.task1.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService svc;

    public TaskController(TaskService svc) {
        this.svc = svc;
    }

    // ✅ GET /api/tasks  or  /api/tasks?id=...
    @GetMapping
    public ResponseEntity<?> getAllOrById(@RequestParam(required = false) String id) {
        if (id == null) {
            return ResponseEntity.ok(svc.getAll());
        } else {
            Optional<Task> t = svc.getById(id);
            if (t.isPresent()) {
                return ResponseEntity.ok(t.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
            }
        }
    }

    // ✅ PUT /api/tasks  (create or update)
    @PutMapping
    public ResponseEntity<?> putTask(@RequestBody Task task) {
        try {
            Task saved = svc.save(task);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ✅ DELETE /api/tasks/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        svc.delete(id);
        return ResponseEntity.ok("Deleted");
    }

    // ✅ GET /api/tasks/search?name=abc
    @GetMapping("/search")
    public ResponseEntity<?> searchByName(@RequestParam String name) {
        List<Task> res = svc.searchByName(name);
        if (res.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tasks found");
        }
        return ResponseEntity.ok(res);
    }

    // ✅ PUT /api/tasks/{id}/execute  (runs the command and stores TaskExecution)
    @PutMapping("/{id}/execute")
    public ResponseEntity<?> executeTask(@PathVariable String id) {
        try {
            TaskExecution exec = svc.runCommandForTask(id);
            return ResponseEntity.ok(exec);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error running command: " + e.getMessage());
        }
    }
}
