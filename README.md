# EIGA — Online Movie Ticket Booking System

> **Your Movie. Your Seat. Your Experience.**

## Overview
EIGA is an end-to-end Online Movie Ticket Booking System designed to streamline the movie-going experience. Acting as a bridge between moviegoers and theatre owners, the platform provides robust user authentication, external TMDB movie metadata synchronization, and hierarchical role management, all built without a traditional relational database framework to demonstrate efficient file-based XML persistence.

## Technologies
- **Java 17**
- **JSP (JavaServer Pages)**
- **Jakarta Servlets 6.0**
- **Maven**
- **Apache Tomcat 10.1**
- **XML** (for persistence storage)
- **TMDB API** (for external movie metadata)

## Architecture
The application strictly follows an MVC-style multi-tiered architectural pattern ensuring deep separation of concerns:
**JSP (View) → Servlet (Controller) → Service (Business Logic) → DAO (Data Access Object) → XML (Persistence)**

## Main Features
- Secure multi-role authentication system with PBKDF2 native password hashing.
- Role-Based Access Control (RBAC) securely intercepting unauthorized paths.
- Thread-safe XML Data access and serialization via Java DOM.
- External TMDB (The Movie Database) metadata synchronization.

## User Roles
EIGA supports a rigid hierarchical role structure for specific platform functionality:
- **USER:** Public moviegoers able to browse movies, book tickets, and manage their own profiles.
- **OWNER:** Independent theatre owners assigned to manage specific registered theatres.
- **STAFF:** Theatre personnel capable of handling ticket check-ins and on-site operations.
- **ADMIN:** Platform-level administrators capable of synchronizing TMDB metadata and managing global theatre listings.

## TMDB Integration
EIGA actively utilizes the official TMDB API to pull rich movie catalogue data including official titles, posters, backdrops, cast, genres, ratings, and YouTube trailer keys. EIGA is designed to operate primarily offline; TMDB is only contacted when explicitly searching or importing data via the administrator dashboard. 

## Installation Requirements
- **Java Development Kit (JDK) 17**
- **Apache Maven 3.6+**
- **Apache Tomcat 10.1** (or another compatible Servlet 6.0 container)

## Build Instructions
Clone the repository and package it into a WAR (Web Application Archive) using Maven:
```bash
mvn clean package
```
The resulting `EIGA.war` file will be generated in the `target/` directory and can be deployed directly into Tomcat's `webapps/` folder.

## Environment Configuration (TMDB_API_KEY)
EIGA securely consumes the TMDB API Key from your server's environment variables. 
**Do NOT hardcode your API key in the source code or commit it to this repository.**

Before launching your Tomcat server, export the variable in your terminal environment:
```bash
export TMDB_API_KEY="your_actual_tmdb_api_key_here"
```
Alternatively, configure the environment variable directly within your IDE (e.g., Eclipse, IntelliJ) run configurations or inside your Tomcat `setenv.sh` file.

## Disclaimer
*This is an academic/non-commercial project created for learning and demonstration purposes. It is not currently deployed online and does not process real transactions or officially represent the theatres registered within.*
