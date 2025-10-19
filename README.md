# Kaiburr Task 1 - Java REST API with MongoDB

## Overview
This project implements a Spring Boot REST API that manages **Task** objects.  
Each Task represents a shell command that can be executed, with execution history stored in MongoDB.

---

## Features
- Create, delete, update, and search tasks.
- Execute commands securely (with command validation).
- Store execution details (start time, end time, and output) in MongoDB.
- REST endpoints accessible via Postman, curl, or PowerShell.

---

## Technologies Used
- Java 17+
- Spring Boot 3.2.6
- MongoDB Community Edition
- Maven

---

## API Endpoints

| Method | Endpoint | Description |
|--------|-----------|-------------|
| `GET` | `/api/tasks` | Get all tasks |
| `GET` | `/api/tasks?id={id}` | Get task by ID |
| `PUT` | `/api/tasks` | Create or update a task |
| `DELETE` | `/api/tasks/{id}` | Delete a task |
| `GET` | `/api/tasks/search?name={name}` | Search task by name |
| `PUT` | `/api/tasks/{id}/execute` | Execute command for task |

---

## Example Request

### Create a task
```powershell
Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/tasks" `
  -ContentType "application/json" `
  -Body '{"id":"t1","name":"Print Hello","owner":"Logesh","command":"echo Hello World!"}'
