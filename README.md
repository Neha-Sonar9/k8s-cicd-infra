# 🚀 Kubernetes CI/CD Infrastructure Challenge

Production-style DevOps project built for a **Kubernetes & CI/CD Infrastructure Challenge**.

The project demonstrates containerization, Kubernetes deployment, CI/CD automation, reliability improvement, observability endpoints, and failure debugging.

---

## 📌 Challenge Context

**Objective:** Build and deploy a minimal production-style application stack using Kubernetes and CI/CD.

**Approximate Time Constraint:** 120 minutes

**Focus Areas:**

* Containerization
* Kubernetes deployment
* CI/CD automation
* Observability
* Reliability improvement
* Operational debugging

---

## 🎯 Key Highlights

* Built a Spring Boot REST API
* Containerized the application using Docker
* Deployed backend service to Kubernetes
* Deployed MySQL using StatefulSet
* Configured persistent storage for MySQL
* Used Kubernetes Secrets for database configuration
* Implemented readiness and liveness probes
* Automated build, image push, and deployment using GitHub Actions
* Used Docker Hub as container registry
* Used immutable Git SHA image tags in CI/CD
* Simulated and debugged a database connectivity failure

---

## 🏗️ Architecture

```text
Developer
   |
   v
GitHub Repository
   |
   v
GitHub Actions CI/CD
   |
   |-- Build Spring Boot Application
   |-- Build Docker Image
   |-- Push Image to Docker Hub
   |
   v
Kubernetes Deployment Update
   |
   v
Minikube Cluster

Inside Kubernetes:

+------------------------------------------------+
| Namespace: infra-challenge                     |
|                                                |
|  +-------------------------------+             |
|  | Spring Boot Deployment        |             |
|  | Replicas: 2                   |             |
|  | Port: 8081                    |             |
|  +-------------------------------+             |
|                  |                             |
|                  v                             |
|  +-------------------------------+             |
|  | NodePort Service              |             |
|  | infrachallenge-service        |             |
|  | NodePort: 30081               |             |
|  +-------------------------------+             |
|                  |                             |
|                  v                             |
|  +-------------------------------+             |
|  | MySQL StatefulSet             |             |
|  | mysql-0                       |             |
|  | Persistent Storage            |             |
|  +-------------------------------+             |
|                  |                             |
|                  v                             |
|  +-------------------------------+             |
|  | Persistent Volume Claim       |             |
|  | mysql-storage-mysql-0         |             |
|  +-------------------------------+             |
+------------------------------------------------+
```

---

## ⚙️ Tech Stack

| Area                 | Technology                                |
| -------------------- | ----------------------------------------- |
| Backend              | Java 17, Spring Boot                      |
| API                  | Spring Web                                |
| Database             | MySQL 8                                   |
| ORM                  | Spring Data JPA                           |
| Health/Observability | Spring Boot Actuator                      |
| Containerization     | Docker                                    |
| Orchestration        | Kubernetes with Minikube                  |
| CI/CD                | GitHub Actions                            |
| Registry             | Docker Hub                                |
| Runtime Config       | Environment Variables, Kubernetes Secrets |

---

## 📁 Repository Structure

```text
.
├── .github/
│   └── workflows/
│       └── ci-cd.yaml
│
├── k8s/
│   ├── app/
│   │   ├── deployment.yaml
│   │   └── service.yaml
│   │
│   ├── mysql/
│   │   ├── secret.example.yaml
│   │   ├── service.yaml
│   │   └── statefulset.yaml
│   │
│   └── namespace.yaml
│
├── src/
│   └── main/
│       ├── java/com/neha/infrachallenge/
│       └── resources/application.yaml
│
├── Dockerfile
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

## 🐳 Docker Implementation

The Dockerfile uses a multi-stage build:

| Stage         | Purpose                                     |
| ------------- | ------------------------------------------- |
| Build Stage   | Uses JDK image to build the Spring Boot JAR |
| Runtime Stage | Uses JRE image to run the application       |

Implemented container practices:

* Multi-stage Docker build
* Non-root container user
* Application exposed on port `8081`
* Runtime configuration through environment variables

Docker image format:

```text
nsonar/infrachallenge-api:<git-sha>
```

---

## ☸️ Kubernetes Implementation

### Namespace

All resources are deployed under:

```text
infra-challenge
```

### Backend Application

The backend runs as a Kubernetes Deployment:

| Setting         | Value                |
| --------------- | -------------------- |
| Deployment Name | `infrachallenge-api` |
| Replicas        | `2`                  |
| Container Port  | `8081`               |
| Service Type    | `NodePort`           |
| NodePort        | `30081`              |

### Database

MySQL runs as a StatefulSet:

| Setting          | Value                   |
| ---------------- | ----------------------- |
| StatefulSet Name | `mysql`                 |
| Pod Name         | `mysql-0`               |
| Service Name     | `mysql-service`         |
| Storage          | Persistent Volume Claim |
| Database         | `infrachallenge`        |

---

## 🔐 Secret Management

The repository includes a safe template:

```text
k8s/mysql/secret.example.yaml
```

The actual secret file is ignored from Git:

```text
k8s/mysql/secret.yaml
```

This avoids committing real credentials into version control.

Create the real local secret from the example file:

```bash
cp k8s/mysql/secret.example.yaml k8s/mysql/secret.yaml
```

Then update the values in `secret.yaml` before applying Kubernetes manifests.

---

## 🔄 CI/CD Pipeline

GitHub Actions workflow:

```text
.github/workflows/ci-cd.yaml
```

The pipeline performs:

```text
Code Push to main
   |
   v
Checkout Source Code
   |
   v
Build Spring Boot Application
   |
   v
Login to Docker Hub
   |
   v
Build Docker Image
   |
   v
Push Docker Image with Git SHA Tag
   |
   v
Update Kubernetes Deployment Image
   |
   v
Verify Deployment Rollout
```

### Why Git SHA Tags?

The pipeline uses Git commit SHA as the Docker image tag.

Benefits:

* Immutable deployments
* Better traceability
* Easier rollback
* No dependency on mutable `latest` tag

Example:

```text
nsonar/infrachallenge-api:<github-sha>
```

---

## 🛡️ Reliability Improvement

The selected reliability improvement is:

```text
Readiness and Liveness Probes
```

### Readiness Probe

The readiness probe checks whether the application is ready to receive traffic.

Purpose:

* Prevents traffic from reaching an unready pod
* Makes rolling deployments safer
* Reduces failed requests during startup

### Liveness Probe

The liveness probe checks whether the application is still healthy after startup.

Purpose:

* Restarts unhealthy containers automatically
* Provides self-healing behavior
* Reduces manual recovery effort

### Tradeoff

Probe settings must be tuned carefully.

Incorrect probe timings can cause:

* False failures
* Unnecessary restarts
* Slow rollout
* Delayed traffic routing

---

## 📊 Observability

Spring Boot Actuator is enabled.

Available health endpoints:

```text
/api/health
/actuator/health
/actuator/metrics
```

The actuator health endpoint validates both:

* Application health
* Database connectivity

Example:

```bash
curl http://<APPLICATION_URL>/actuator/health
```

Expected result:

```json
{
  "status": "UP"
}
```

---

## ✅ Deployment Verification

### Check Kubernetes Resources

```bash
kubectl get all -n infra-challenge
```

### Check Persistent Volume Claim

```bash
kubectl get pvc -n infra-challenge
```

### Check Pod Resource Usage

```bash
kubectl top pods -n infra-challenge
```

### Access Application in Minikube

For Minikube with Docker driver on macOS:

```bash
minikube service infrachallenge-service -n infra-challenge --url
```

Use the returned localhost URL.

Example:

```bash
curl http://127.0.0.1:<PORT>/api/health
```

### Create User

```bash
curl -X POST http://127.0.0.1:<PORT>/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Kubernetes User","email":"k8s@example.com"}'
```

### Get Users

```bash
curl http://127.0.0.1:<PORT>/api/users
```

---

## 🧪 Failure Simulation and Debugging

A database connectivity issue was intentionally introduced to demonstrate troubleshooting methodology.

### Failure Introduced

The application datasource URL was changed to use an invalid Kubernetes service name:

```text
wrong-mysql-service
```

instead of:

```text
mysql-service
```

Command used:

```bash
kubectl set env deployment/infrachallenge-api \
  SPRING_DATASOURCE_URL=jdbc:mysql://wrong-mysql-service:3306/infrachallenge \
  -n infra-challenge
```

---

## 🔍 Debugging Process

### 1. Check Pod Status

```bash
kubectl get pods -n infra-challenge
```

Observed:

```text
One new pod entered CrashLoopBackOff
Existing healthy pod continued running
```

This happened because Kubernetes performed a rolling update and kept an old healthy replica available.

### 2. Inspect Pod Events

```bash
kubectl describe pod <pod-name> -n infra-challenge
```

### 3. Check Application Logs

```bash
kubectl logs <pod-name> -n infra-challenge
```

The logs showed database connectivity failure.

### 4. Verify Kubernetes Services

```bash
kubectl get svc -n infra-challenge
```

Actual MySQL service:

```text
mysql-service
```

Incorrect configured service:

```text
wrong-mysql-service
```

---

## 🛠️ Root Cause

The application was configured with an incorrect Kubernetes service name.

The backend tried to connect to:

```text
wrong-mysql-service
```

but the actual database service was:

```text
mysql-service
```

---

## ✅ Fix

Restore the correct datasource URL:

```bash
kubectl set env deployment/infrachallenge-api \
  SPRING_DATASOURCE_URL=jdbc:mysql://mysql-service:3306/infrachallenge \
  -n infra-challenge
```

Verify recovery:

```bash
kubectl get pods -n infra-challenge
```

Expected:

```text
Application pods return to Running state
```

---

## 🧠 Production Considerations

This project is production-style, but intentionally runs in a local Minikube environment.

| Current Setup               | Production Alternative                  |
| --------------------------- | --------------------------------------- |
| Minikube                    | Amazon EKS / AKS / GKE                  |
| Local self-hosted runner    | Dedicated CI runners                    |
| MySQL StatefulSet           | Managed database such as Amazon RDS     |
| NodePort                    | Ingress Controller / Load Balancer      |
| Basic Actuator endpoints    | Prometheus + Grafana                    |
| Manual Kubernetes manifests | Helm / Kustomize / GitOps               |
| Kubernetes Secret           | External Secrets / Cloud Secret Manager |

---

## 🚀 Future Improvements

Possible next improvements:

* Add Ingress Controller
* Add Horizontal Pod Autoscaler
* Add Prometheus and Grafana dashboards
* Add centralized logging
* Add Helm chart
* Add Terraform infrastructure provisioning
* Add ArgoCD-based GitOps deployment
* Add automated rollback strategy
* Add vulnerability scanning in CI/CD
* Add load testing using k6 or hey

---

## 👩‍💻 Author

**Neha Sonar**

DevOps Engineer
Kubernetes | Docker | AWS | CI/CD | Terraform | Jenkins | Linux

---
