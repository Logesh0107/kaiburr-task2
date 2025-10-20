# Kaiburr Task 2 — Kubernetes Deployment

## Overview
This project containerizes a **Spring Boot REST API** that performs CRUD operations on **MongoDB**.  
Both the **application** and **MongoDB** are deployed on a **Kubernetes cluster (Docker Desktop)** with persistent storage and service exposure.

---

## Tech Stack
- **Java 17 / Spring Boot 3**
- **MongoDB 6.0**
- **Docker & Docker Desktop (with Kubernetes enabled)**
- **Kubernetes (kubectl)**
- **Persistent Volume / PVC**
- **Fabric8 Kubernetes Client** (for pod creation via API)

---

##  Project Structure
kaiburr-task1-restapi/
├── src/
├── k8s/
│ ├── namespace.yaml
│ ├── mongo-pv-pvc.yaml
│ ├── mongo-deployment.yaml
│ ├── rbac.yaml
│ └── app-deployment.yaml
├── Dockerfile
├── pom.xml
├── README.md
└── .gitignore

### Build Docker Image
Build the Spring Boot app as a Docker image:
```bash
docker build -t mytaskapp:local .

###Apply Kubernetes Manifests and Create all necessary resources:

kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/mongo-pv-pvc.yaml
kubectl apply -f k8s/mongo-deployment.yaml
kubectl apply -f k8s/rbac.yaml
kubectl apply -f k8s/app-deployment.yaml

### Verify Deployments and Check that pods and services are up:

kubectl get pods -n task-app
kubectl get svc -n task-app
kubectl get pvc -n task-app

### Access the Application, After the pods are running, open in browser:

http://localhost:30080/api/tasks