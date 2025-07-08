# Auction Microservices - Local Setup & API Usage Guide

## Step 1: Start Docker Compose
Run the following command to start all services defined in `docker-compose.yml`:
```bash
docker compose up -d
```

## Step 2: Run Microservices
Ensure the following Spring Boot microservices are up and running:
- **Auth Service** (Port 8081)
- **Auction Service** (Port 8082)
- **Bidding Service** (Port 8083)

## Step 3: Test the System via REST APIs

### a. Create User
```bash
curl --location 'http://localhost:8081/api/auth/signup' --header 'Content-Type: application/json' --data-raw '{
    "name": "John2 Doe",
    "username": "johndoe2",
    "email": "john2@example.com",
    "password": "yourPassword123"
}'
```

### b. Sign In
```bash
curl --location 'http://localhost:8081/api/auth/signin' --header 'Content-Type: application/json' --data-raw '{
  "usernameOrEmail": "john2@example.com",
  "password": "yourPassword123"
}'
```

### c. Get User by Username
```bash
curl --location 'http://localhost:8081/api/users/johndoe2' --header 'Authorization: Bearer <JWT_TOKEN>'
```

### d. Create Auction
```bash
curl --location 'http://localhost:8082/api/auctions' --header 'Authorization: Bearer <JWT_TOKEN>' --header 'Content-Type: application/json' --data '{
  "title": "Antique Vase",
  "description": "Ming Dynasty replica",
  "startingPrice": 2500.0,
  "minimumIncrement": 100.0,
  "startTime": "2025-07-07T18:00:00Z",
  "endTime": "2025-07-10T18:00:00Z",
  "biddingIntervalMinutes": 60
}'
```

### e. Get All Auctions
```bash
curl --location 'http://localhost:8082/api/auctions' --header 'Authorization: Bearer <JWT_TOKEN>'
```

### f. Place Bid
```bash
curl --location 'http://localhost:8083/api/bids' --header 'Authorization: Bearer <JWT_TOKEN>' --header 'Content-Type: application/json' --data '{
    "auctionId": "686bae7382de403cf1ff5fa6",
    "amount": 10050.50
}'
```

### g. Get Leaderboard for an Auction
```bash
curl --location 'http://localhost:8083/api/bids/leaderboard/686bae7382de403cf1ff5fa6?limit=10' --header 'Authorization: Bearer <JWT_TOKEN>'
```

> ⚠️ Replace `<JWT_TOKEN>` with the actual token obtained from the Sign In response.
