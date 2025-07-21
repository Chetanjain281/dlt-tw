<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

# DLT Trade Link System - Copilot Instructions

This is a Spring Boot application demonstrating Distributed Ledger Technology (DLT) for trade processing.

## Project Context
- **Purpose**: Proof of concept showing DLT advantages over traditional API-based systems
- **Technology**: Spring Boot 3.5.3, Java 17, MongoDB, HTML/CSS/JS
- **Key Feature**: Immutable hash-chained transaction processing

## Code Style Guidelines
- Use professional, clean code without emojis in comments
- Follow Spring Boot best practices
- Maintain consistent naming conventions
- Include proper error handling and logging

## Architecture Patterns
- Repository pattern for data access
- Service layer for business logic
- RESTful API design
- Hash chain implementation for immutability

## Key Components
- **Models**: Order, Client, Fund, OrderStage, AuditLog
- **Services**: OrderProcessingService, AuditService, DataInitializationService
- **Hash Utility**: SHA-256 implementation for chain validation
- **Controllers**: REST endpoints and web page controllers

## Domain Knowledge
- Trade processing workflow: Solicitation → Suitability → Order Manager → Reviewer → Operations → Product Processor
- Each stage adds data and calculates hash based on previous stage
- Industry-standard suitability checks for investment products
- Complete audit trail for regulatory compliance

## Frontend Guidelines
- Simple, functional design without fancy features
- Responsive HTML/CSS/JS implementation
- Real-time data updates via REST APIs
- Hash chain visualization for demonstration

## MongoDB Collections
- orders: Main transaction ledger
- audit_logs: Event logging
- clients: Reference data
- funds: Investment product data

When generating code, focus on:
1. Immutability principles
2. Hash chain integrity
3. Complete audit trails
4. Professional presentation suitable for enterprise demonstration
