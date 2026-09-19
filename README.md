# SentinelPay

**A real-time payment fraud detection engine that explains every decision it makes.**

[Demo Video](https://youtu.be/pU54R6ApH4U?si=jp7FFWHzjQVvF6Rc) · [Backend Repo](https://github.com/priya-codesdaily/sentinelpay-backend) · [Frontend Repo](https://github.com/priya-codesdaily/sentinelpay-web)

---

## The Problem

Most payment demo projects store a transaction and show a success message. That's not where real financial harm happens — it happens in the decision *before* that: whether a transaction should be trusted at all. Real fintech companies (Stripe Radar, Razorpay's internal risk engine) run dedicated systems for exactly this. SentinelPay is a working, explainable version of that same idea: a risk engine that evaluates every transaction live, and tells you exactly why it made the call it did.

---

## What It Does

| Feature | Problem It Solves | How It Works | Example |
|---|---|---|---|
| **Explainable Risk Scoring** | Black-box "blocked" decisions aren't trustworthy or debuggable | Every transaction is scored against independent rules (amount, time, velocity, device), each contributing points and a plain-English reason | `Risk Score: 45, FLAGGED — "High amount (+30)", "Unusual hour (+15)"` |
| **Live Attack Simulator** | Fraud reveals itself as a *pattern* over time, not one isolated transaction | A burst of rapid transactions is fired at one payee, and the decision escalates live as the system recognizes the pattern | Transactions 1–3: `APPROVED` → Transaction 4: `FLAGGED` → Transaction 6: `BLOCKED` |
| **Device Fingerprinting** | A known user suddenly appearing on a new device is a real, common fraud signal | Browser signals (user agent, screen size, timezone, language) are fingerprinted client-side and checked against each payee's known devices | Known user on a new browser → `+20, "Transaction from unrecognized device"` |
| **Real-Time Dashboard** | Raw transaction rows aren't useful without aggregation | Live summary cards and a history table, computed from real stored data | `137 Total · 47 Blocked · 27 Flagged · 46% Approval Rate` |
| **Permanent Persistence** | A fraud system that forgets on restart isn't a real system | Every transaction is saved to PostgreSQL via Spring Data JPA — nothing lives only in memory | History survives backend restarts; queryable via `GET /transactions` |

---

## Architecture

```
React (client)
    │  POST /transaction { amount, payee, deviceFingerprint }
    ▼
Controller  →  Service (rules engine)  →  Repository  →  PostgreSQL
    │
    ▼
{ riskScore, decision, reasons }  →  rendered live in React
```

Classic 3-layer separation: Controller handles HTTP only, Service holds all fraud logic, Repository talks to the database — each layer stays independent of the others' internals.

---

## Risk Rules

| Rule | Trigger | Points |
|---|---|---|
| High amount | Transaction over ₹10,000 | +30 |
| Unusual hour | Before 5am or after 11pm | +15 |
| Velocity | More than 3 transactions in 60 seconds | Escalating, up to +80 |
| Unrecognized device | Known payee, new device fingerprint | +20 |

**Decision thresholds:** 0–39 → Approved · 40–69 → Flagged · 70+ → Blocked

---

## Tech Stack

| Layer | Technology | Why |
|---|---|---|
| Backend | Java, Spring Boot | Mirrors production fintech backend conventions |
| Frontend | React (Vite) | Industry-standard for dashboards |
| Database | PostgreSQL | ACID guarantees suit transactional data |
| Architecture | REST API, 3-layer pattern | Clean separation of HTTP, logic, and persistence |

---

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/transaction` | Submit a transaction — returns risk score, decision, and reasons |
| `GET` | `/transactions` | Returns full transaction history |

---

## Running Locally

**Backend**
```bash
# 1. Create a PostgreSQL database named `sentinelpay`
# 2. Copy application.properties.example → application.properties and add your credentials
./mvnw spring-boot:run
# runs on http://localhost:8081
```

**Frontend**
```bash
npm install
npm run dev
# runs on http://localhost:5173
```

Both must run simultaneously.

---

## What's Next

| Phase | Addition | Why |
|---|---|---|
| Next | **Scam-intent detection** — asks *why* a risky payment is being made, checks the answer against known scam-language signals | Real fraud (especially investment/MLM scams) often relies on social engineering, not just transaction patterns |
| Next | **Redis-backed velocity tracking** | Current in-memory tracking doesn't scale across multiple server instances |
| Next | **Beneficiary verification check** | Flags new/suspicious payees using existing transaction data |
| Later | **Graph-based mule account detection (Neo4j)** | Coordinated fraud often hides in relationships between accounts, not single transactions |
| Later | **ML-based anomaly scoring**, layered on top of existing rules | Adapts to patterns rules alone can't capture |
| Later | JWT authentication, Docker deployment | Required for anything beyond a local demo |

---

## Known Limitations

- No authentication yet — the API is open
- Velocity tracking is in-memory (single instance only, for now)
- Device fingerprinting uses spoofable browser signals — one honest layer, not a complete solution
- Rules are hand-tuned thresholds, not learned from data

---

Built by Anshu Priya. Actively in development.