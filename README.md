# 📱 SocialApp — RESTful Social Media Backend API

> A fully functional, production-structured **RESTful backend** for a social media platform built with **Spring Boot 4**, **Spring Security**, **Spring Data JPA**, and **MySQL**. The application covers the complete core feature set of a social network — user authentication, posts, comments, likes, follow relationships, and direct messaging — all delivered through a clean, layered MVC architecture.

<br/>

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.2-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-enabled-blue?style=flat-square&logo=springsecurity)](https://spring.io/projects/spring-security)
[![MySQL](https://img.shields.io/badge/MySQL-8-blue?style=flat-square&logo=mysql)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-red?style=flat-square&logo=apachemaven)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](LICENSE)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Tech Stack](#-tech-stack)
- [Features](#-features)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Database Schema](#-database-schema)
- [API Reference](#-api-reference)
- [Request & Response Examples](#-request--response-examples)
- [Security](#-security)
- [Business Logic & Validations](#-business-logic--validations)
- [DTOs & Data Projection](#-dtos--data-projection)
- [Error Handling](#-error-handling)
- [Getting Started](#-getting-started)
- [Configuration Reference](#-configuration-reference)
- [Roadmap](#-roadmap)
- [Author](#-author)

---

## 🌐 Overview

SocialApp is a backend REST API that replicates the core behaviour of a modern social media platform. It is built to demonstrate real-world Spring Boot development practices including layered separation of concerns, entity relationship modelling, DTO-based projection, BCrypt password security, and standardised API response design.

The project evolved from raw foreign-key IDs to fully resolved JPA entity relationships (`@ManyToOne`, `@OneToMany`), demonstrating a mature understanding of ORM design decisions and clean refactoring practices.

---

## 🚀 Tech Stack

| Layer | Technology | Purpose |
|---|---|---|
| Language | Java 21 | Core application language |
| Framework | Spring Boot 4.0.2 | Application bootstrapping & auto-configuration |
| Web | Spring MVC | REST controller layer |
| Security | Spring Security + BCrypt | Authentication & password hashing |
| ORM | Spring Data JPA + Hibernate | Database interaction & entity mapping |
| Database | MySQL 8 | Relational data persistence |
| Build Tool | Apache Maven | Dependency management & build lifecycle |

---

## ✨ Features

- **User Registration & Login** — Secure signup with BCrypt-hashed passwords; credential-based login via `AuthService`
- **User Profile** — Full profile fields including bio, profile image, cover image, and active status
- **User Stats** — Follower and following counts computed dynamically and served via `UserDTO`
- **Post Creation & Feed** — Create posts with captions and image URLs; fetch all posts or filter by user
- **Enriched Post Feed** — Posts returned with embedded comments, live like count, and list of usernames who liked
- **Comments** — Add comments tied to a specific post and user; full comment retrieval
- **Like System** — Like a post with duplicate-like prevention enforced at the service layer
- **Follow System** — Follow/unfollow users with a self-follow guard (`user cannot follow themselves`)
- **Direct Messaging** — Send messages between users; retrieve full chronological conversation between any two users
- **DTO Projection** — All sensitive or noisy entity data stripped via DTOs before reaching the client
- **Standardised API Responses** — Every endpoint returns a typed `ApiResponse<T>` envelope with `success`, `message`, `data`, and `error` fields
- **Global Exception Handling** — `@ControllerAdvice` catches unhandled exceptions and returns consistent error responses

---

## 🏗️ Architecture

The application follows a strict **3-Layer MVC architecture**:

```
HTTP Request
     │
     ▼
┌─────────────────┐
│   Controller    │  ← Receives HTTP request, delegates to service, returns ResponseEntity
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    Service      │  ← Contains all business logic, validations, DTO mappings
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│   Repository    │  ← Spring Data JPA interfaces; talks to MySQL
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│     MySQL       │  ← Persistent relational data store
└─────────────────┘
```

**Key design decisions:**
- `EntityManager` is used directly in services alongside repositories where fine-grained JPA control is needed
- DTOs are constructed in the service layer — the controller never exposes raw JPA entities
- `ApiResponse<T>` is a generic wrapper used universally, keeping the client contract consistent

---

## 📁 Project Structure

```
application/
└── src/
    └── main/
        └── java/com/social/application/
            │
            ├── Application.java                        # @SpringBootApplication entry point
            │
            ├── Config/
            │   └── SecurityConfig.java                 # Security filter chain, BCrypt bean
            │
            ├── Controller/
            │   ├── UserController.java                 # /user/** endpoints
            │   ├── PostController.java                 # /post/** endpoints
            │   ├── CommentController.java              # /comment/** endpoints
            │   ├── LikeController.java                 # /like/** endpoints
            │   ├── FollowController.java               # /follow/** endpoints
            │   └── MessageController.java              # /message/** endpoints
            │
            ├── Services/
            │   ├── AuthService.java                    # Login logic + BCrypt verify
            │   ├── UserService.java                    # User CRUD + follower/following stats
            │   ├── PostService.java                    # Post CRUD + enriched DTO assembly
            │   ├── CommentService.java                 # Comment CRUD + entity resolution
            │   ├── LikeService.java                    # Like CRUD + duplicate-like guard
            │   ├── FollowService.java                  # Follow CRUD + self-follow guard
            │   └── MessageService.java                 # Message CRUD + chat history builder
            │
            ├── Repositories/
            │   ├── UserRepository.java                 # findByEmail
            │   ├── PostRepository.java                 # findByUser_Id
            │   ├── CommentRepository.java              # JpaRepository defaults
            │   ├── LikeRepository.java                 # countByPost_Id, existsByUserIdAndPostId
            │   ├── FollowRepository.java               # countByFollowerUser_Id, countByFollowingUser_Id
            │   └── MessageRepository.java              # findBySender_IdAndReceiver_IdOrderByCreatedAtAsc
            │
            ├── Models/
            │   ├── User.java                           # users table
            │   ├── Post.java                           # posts table — ManyToOne User, OneToMany Comments/Likes
            │   ├── Comment.java                        # comments table — ManyToOne Post + User
            │   ├── Like.java                           # likes table — ManyToOne Post + User
            │   ├── Follow.java                         # follows table — followerUser, followingUser
            │   └── Message.java                        # messages table — sender, receiver, isSeen
            │
            ├── DTOs/
            │   ├── UserDTO.java                        # id, username, bio, profileImage, followers, following
            │   ├── PostDTO.java                        # id, caption, likeCount, comments[], likedBy[]
            │   ├── CommentDTO.java                     # id, text
            │   ├── MessageDTO.java                     # senderUsername, content
            │   └── LoginRequestDTO.java                # email, password
            │
            └── Utility/
                ├── ApiResponse.java                    # Generic typed response envelope
                └── GlobalExceptionHandler.java         # @ControllerAdvice — catches all exceptions
```

---

## 🗄️ Database Schema

### `users`
| Column | Type | Notes |
|---|---|---|
| id | BIGINT PK | Auto-generated |
| full_name | VARCHAR | User's full name |
| username | VARCHAR | Display handle |
| email | VARCHAR | Used for login |
| password | VARCHAR | BCrypt hashed — never exposed |
| bio | VARCHAR | Short bio |
| profile_image | VARCHAR | Profile photo URL |
| cover_image | VARCHAR | Cover photo URL |
| is_active | BOOLEAN | Soft-delete flag, default `true` |
| created_at | DATETIME | |
| updated_at | DATETIME | |

### `posts`
| Column | Type | Notes |
|---|---|---|
| id | BIGINT PK | |
| user_id | BIGINT FK → users | Post author |
| caption | VARCHAR(1000) | Post text |
| image_url | VARCHAR | Optional media |
| like_count | BIGINT | Denormalised count |
| comment_count | BIGINT | Denormalised count |
| created_at | DATETIME | |

### `comments`
| Column | Type | Notes |
|---|---|---|
| id | BIGINT PK | |
| post_id | BIGINT FK → posts | |
| user_id | BIGINT FK → users | |
| text | VARCHAR | Comment body |
| created_at | DATETIME | |

### `likes`
| Column | Type | Notes |
|---|---|---|
| id | BIGINT PK | |
| post_id | BIGINT FK → posts | NOT NULL |
| user_id | BIGINT FK → users | NOT NULL |
| created_at | DATETIME | |

### `follows`
| Column | Type | Notes |
|---|---|---|
| id | BIGINT PK | |
| follower_id | BIGINT FK → users | The user who follows |
| following_id | BIGINT FK → users | The user being followed |
| created_at | DATETIME | |

### `messages`
| Column | Type | Notes |
|---|---|---|
| id | BIGINT PK | |
| sender_id | BIGINT FK → users | |
| receiver_id | BIGINT FK → users | |
| content | VARCHAR(2000) | Message body |
| is_seen | BOOLEAN | Read receipt, default `false` |
| created_at | DATETIME | Used for chronological sort |

---

## 📡 API Reference

### Base URL
```
http://localhost:8080
```

### Response Envelope
Every endpoint returns:
```json
{
  "success": true,
  "message": "Human readable message",
  "data": { },
  "error": null
}
```
On failure:
```json
{
  "success": false,
  "message": "Something went wrong",
  "data": null,
  "error": "ERROR_CODE or exception message"
}
```

---

### 👤 User Endpoints

| Method | Endpoint | Description | Status |
|---|---|---|---|
| `GET` | `/user` | Fetch all users as UserDTO | `200` |
| `POST` | `/user` | Register a new user | `201` |
| `GET` | `/user/{id}` | Fetch user by ID as UserDTO | `200` |
| `GET` | `/user/{id}/stats` | Fetch user with follower/following counts | `200` |
| `POST` | `/user/login` | Authenticate with email + password | `200` |
| `GET` | `/user/reponse/{id}` | Fetch raw User entity with null check | `200 / 404` |

### 📝 Post Endpoints

| Method | Endpoint | Description | Status |
|---|---|---|---|
| `GET` | `/post` | Get all posts as PostDTO | `200` |
| `POST` | `/post` | Create a new post | `201` |
| `GET` | `/post/{id}` | Get a post by ID | `200` |
| `GET` | `/post/user/{userId}` | Get all posts by a specific user | `200` |
| `GET` | `/post/postWithComment` | Posts enriched with comments + likes | `200` |

### 💬 Comment Endpoints

| Method | Endpoint | Description | Status |
|---|---|---|---|
| `GET` | `/comment` | Get all comments | `200 / 404` |
| `POST` | `/comment` | Add a comment to a post | `201` |
| `GET` | `/comment/{id}` | Get a comment by ID | `200` |

### ❤️ Like Endpoints

| Method | Endpoint | Description | Status |
|---|---|---|---|
| `GET` | `/like` | Get all likes | `200` |
| `POST` | `/like` | Like a post (duplicate-safe) | `201 / 400` |
| `GET` | `/like/{id}` | Get a like by ID | `200` |

### 🤝 Follow Endpoints

| Method | Endpoint | Description | Status |
|---|---|---|---|
| `GET` | `/follow` | Get all follow records | `200` |
| `POST` | `/follow` | Follow a user (self-follow blocked) | `201 / 400` |
| `GET` | `/follow/{id}` | Get a follow record by ID | `200` |

### ✉️ Message Endpoints

| Method | Endpoint | Description | Status |
|---|---|---|---|
| `GET` | `/message` | Get all messages | `200` |
| `POST` | `/message` | Send a message | `200` |
| `GET` | `/message/{id}` | Get a message by ID | `200` |
| `GET` | `/message/chat/{user1}/{user2}` | Full chronological chat between two users | `200 / 404` |

---

## 📦 Request & Response Examples

### Register a User
```http
POST /user
Content-Type: application/json

{
  "fullName": "Siddhesh Sonawane",
  "username": "siddhesh",
  "email": "siddhesh@example.com",
  "password": "mypassword123",
  "bio": "Backend developer",
  "profileImage": "https://example.com/img.jpg"
}
```
```json
{
  "success": true,
  "message": "User created",
  "data": {
    "id": 1,
    "username": "siddhesh",
    "bio": "Backend developer",
    "profileImage": "https://example.com/img.jpg",
    "follower": 0,
    "following": 0
  },
  "error": null
}
```

### Login
```http
POST /user/login
Content-Type: application/json

{
  "email": "siddhesh@example.com",
  "password": "mypassword123"
}
```

### Create a Post
```http
POST /post
Content-Type: application/json

{
  "caption": "My first post!",
  "imageUrl": "https://example.com/photo.jpg",
  "user": { "id": 1 }
}
```

### Like a Post
```http
POST /like
Content-Type: application/json

{
  "user": { "id": 1 },
  "post": { "id": 3 }
}
```

### Add a Comment
```http
POST /comment
Content-Type: application/json

{
  "text": "Great post!",
  "user": { "id": 2 },
  "post": { "id": 3 }
}
```

### Follow a User
```http
POST /follow
Content-Type: application/json

{
  "followerUser": { "id": 1 },
  "followingUser": { "id": 2 }
}
```

### Send a Message
```http
POST /message
Content-Type: application/json

{
  "content": "Hey! How are you?",
  "sender": { "id": 1 },
  "receiver": { "id": 2 }
}
```

### Get Chat Between Two Users
```http
GET /message/chat/1/2
```
```json
{
  "success": true,
  "message": "Chat fetched",
  "data": [
    { "senderUsername": "siddhesh", "content": "Hey!" },
    { "senderUsername": "alice", "content": "Hi there!" },
    { "senderUsername": "siddhesh", "content": "How are you?" }
  ],
  "error": null
}
```

### Enriched Post Feed
```http
GET /post/postWithComment
```
```json
{
  "success": true,
  "message": "Posts fetched",
  "data": [
    {
      "id": 1,
      "caption": "Hello world!",
      "likeCount": 3,
      "comments": [
        { "id": 2, "text": "Nice post!" }
      ],
      "likedBy": ["alice", "bob", "charlie"]
    }
  ],
  "error": null
}
```

---

## 🔒 Security

Security is configured in `SecurityConfig.java` using Spring Security's `SecurityFilterChain`:

```
CSRF        → Disabled  (stateless REST API — no session cookies)
Form Login  → Disabled
HTTP Basic  → Enabled   (for protected routes)

Public routes (no auth required):
  /user/**   /post/**   /like/**
  /comment/**  /follow/**  /message/**

All other routes → require authentication
```

**Password Security:**
- Passwords are encoded using `BCryptPasswordEncoder` before persisting to the database
- The `password` field on the `User` entity is annotated with `@JsonProperty(access = WRITE_ONLY)` — accepted on input but **never serialised in any response**
- `AuthService.login()` uses `passwordEncoder.matches()` to verify credentials against the stored BCrypt hash — the raw password is never stored or compared directly

> ⚠️ **Production Note:** For a deployed application, replace HTTP Basic with **JWT (JSON Web Token)** stateless authentication for security and scalability.

---

## 🧠 Business Logic & Validations

All business rules are enforced in the **service layer**, never in the controller:

### Duplicate Like Prevention
```java
// LikeService.java
boolean alreadyLiked = likeRepository.existsByUserIdAndPostId(userId, postId);
if (alreadyLiked) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post already liked by user");
}
```

### Self-Follow Guard
```java
// FollowService.java
if (followerId.equals(followingId)) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot follow themselves");
}
```

### Entity Resolution Before Association
```java
User user = entityManager.find(User.class, userId);
if (user == null) throw new ResponseStatusException(NOT_FOUND, "User not found");
```

### Chronological Chat Assembly
```java
List<Message> aToB = repo.findBySender_IdAndReceiver_IdOrderByCreatedAtAsc(user1, user2);
List<Message> bToA = repo.findBySender_IdAndReceiver_IdOrderByCreatedAtAsc(user2, user1);
all.sort(Comparator.comparing(Message::getCreatedAt));
```

---

## 📐 DTOs & Data Projection

Raw JPA entities are never sent to the client. The service layer maps them to clean DTOs:

### UserDTO
Fields: `id`, `username`, `bio`, `profileImage`, `follower` (count), `following` (count)

```java
long followers = followRepository.countByFollowingUser_Id(userId);
long following = followRepository.countByFollowerUser_Id(userId);
```

### PostDTO
Fields: `id`, `caption`, `likeCount`, `comments` (List of CommentDTO), `likedBy` (List of usernames)

Like count fetched from `LikeRepository.countByPost_Id()`. The `likedBy` list assembled via Java Streams mapping `Like → username`.

### CommentDTO
Fields: `id`, `text`

### MessageDTO
Fields: `senderUsername`, `content`

### LoginRequestDTO
Fields: `email`, `password`

---

## ⚠️ Error Handling

### GlobalExceptionHandler
```java
@ExceptionHandler(Exception.class)
public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.error("Something went wrong", ex.getMessage()));
}
```

### Error Status Reference

| Scenario | HTTP Status |
|---|---|
| Entity not found | `404 Not Found` |
| Post already liked | `400 Bad Request` |
| User tries to follow themselves | `400 Bad Request` |
| Chat history empty | `404 Not Found` |
| Comment list empty | `404 Not Found` |
| Unhandled exception | `500 Internal Server Error` |

---

## ⚙️ Getting Started

### Prerequisites

- **Java 21** — [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.9+** — [Download](https://maven.apache.org/download.cgi)
- **MySQL 8** — [Download](https://dev.mysql.com/downloads/)

### 1. Clone the Repository

```bash
git clone https://github.com/siddhesh-sonawane/socialapp.git
cd socialapp/application
```

### 2. Create the Database

```sql
CREATE DATABASE socialdb;
```

### 3. Configure Application Properties

Edit `src/main/resources/application.properties`:

```properties
spring.application.name=application

spring.datasource.url=jdbc:mysql://localhost:3306/socialdb
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

> `ddl-auto=update` — Hibernate auto-creates and migrates tables on startup. No manual SQL scripts needed.

### 4. Build & Run

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The API will be live at `http://localhost:8080`

### 5. Test the API

```bash
curl -X POST http://localhost:8080/user \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test User","username":"testuser","email":"test@test.com","password":"pass123"}'
```

Or use **Postman** / **Insomnia** for full API exploration.

---

## ⚙️ Configuration Reference

| Property | Default | Description |
|---|---|---|
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/socialdb` | MySQL connection string |
| `spring.datasource.username` | `root` | DB username |
| `spring.datasource.password` | *(set yours)* | DB password |
| `spring.jpa.hibernate.ddl-auto` | `update` | Schema strategy |
| `spring.jpa.show-sql` | `true` | Log SQL to console |
| `server.port` | `8080` | HTTP port |

---

## 🛣️ Roadmap

- [ ] JWT-based stateless authentication
- [ ] Refresh token support
- [ ] Pagination & sorting on feed, comments, messages
- [ ] Post image upload via AWS S3 or Cloudinary
- [ ] Real-time messaging with WebSocket / STOMP
- [ ] Notification system for likes, follows, comments
- [ ] Unit & integration test coverage (JUnit 5 + MockMvc)
- [ ] Swagger / OpenAPI auto-generated documentation
- [ ] Docker + docker-compose support
- [ ] Bean Validation (`@Valid`) on all request bodies
- [ ] Duplicate follow prevention

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).

---

## 👨‍💻 Author

Built with ❤️ using Spring Boot.

**GitHub:** [@siddhesh-sonawane](https://github.com/siddhesh-sonawane)

For questions, feedback, or collaboration — feel free to open an issue or connect via GitHub.
