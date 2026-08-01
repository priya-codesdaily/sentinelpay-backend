# SentinelPay — Real-Time Payment Fraud Detection Engine

SentinelPay is a real-time payment risk engine that analyzes every transaction before approval and explains every decision, instead of acting as a black box.

**Demo video:** https://youtu.be/pU54R6ApH4U

**Frontend repo:** https://github.com/priya-codesdaily/sentinelpay-web

## Why this project

Most student payment projects are CRUD apps — forms that save data. SentinelPay is closer to what real fintech companies (Stripe Radar, Razorpay's fraud engine) run internally: a decision-making layer that reasons about risk in real time, not just a form.

## Features

- Explainable risk engine with human-readable fraud reasons for every decision
- Live fraud attack simulator demonstrating velocity-based attacks in real time
- Device fingerprinting for behavioral risk analysis across sessions
- Real-time dashboard with approval rate and fraud metrics
- Transaction history permanently backed by PostgreSQL, not in-memory state

## Tech stack

| Layer | Technology |
|---|---|
| Backend | Java, Spring Boot |
| Frontend | React (Vite) |
| Database | PostgreSQL |
| Architecture | REST API, 3-layer (Controller → Service → Repository) |

## How the risk engine works

Each transaction is scored against independent rules, and points accumulate:

- **High amount** (> ₹10,000) → +30
- **Unusual hour** (before 5am or after 11pm) → +15
- **Velocity** — more than 3 transactions in 60 seconds → escalating penalty, up to +80
- **Unrecognized device** — a known payee transacting from a device never seen before → +20

**Decision thresholds:** 0–39 = Approved · 40–69 = Flagged · 70+ = Blocked

## API endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/transaction` | Submit a transaction, returns risk score, decision, and reasons |
| GET | `/transactions` | Returns full transaction history |

## Running locally

1. Create a PostgreSQL database named `sentinelpay`
2. Configure `src/main/resources/application.properties` with your local database credentials (see `application.properties.example`)
3. Run with Maven: `./mvnw spring-boot:run`
4. Backend runs on `http://localhost:8081`

## Roadmap

Redis-based rate limiting, ML-powered risk scoring, JWT authentication, and Docker deployment.