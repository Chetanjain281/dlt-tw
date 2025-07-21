# DLT Trade Link System

A Spring Boot application demonstrating Distributed Ledger Technology (DLT) for trade processing. This proof of concept showcases how DLT can provide immutability, transparency, and trustless operations compared to traditional API-based systems.

## Features

- **Immutable Transaction Ledger**: Each order progresses through stages with cryptographic hash chaining
- **Hash Chain Validation**: SHA-256 hashing ensures data integrity and detects tampering
- **Complete Audit Trail**: Every action is logged with timestamps and status
- **Multi-Stage Processing**: Automated workflow through Solicitation → Suitability → Order Manager → Reviewer → Operations → Product Processor
- **Web Interface**: Simple HTML/CSS/JS frontend for order management and monitoring
- **Real-time Reporting**: Track order status, system metrics, and audit logs

## Technology Stack

- **Backend**: Spring Boot 3.5.3, Java 17
- **Database**: MongoDB
- **Frontend**: HTML, CSS, JavaScript
- **Build Tool**: Maven
- **Hash Algorithm**: SHA-256

## Getting Started

### Prerequisites

- Java 17 or higher
- MongoDB (running on localhost:27017)
- Maven 3.6+

### Installation

1. Clone the repository
2. Ensure MongoDB is running
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Access the application at `http://localhost:8080`

### Default Data

The application comes pre-loaded with:
- 8 sample clients with different risk profiles
- 8 sample funds with varying characteristics
- No authentication required for this POC

## Architecture

### Data Flow

1. **Client Data & Fund Data** stored in MongoDB collections
2. **Order Creation** triggers automatic processing through all stages
3. **Each Stage** adds data and calculates hash based on previous stage
4. **Hash Chain** ensures immutability and detects tampering
5. **Audit Logs** record every action for complete transparency

### Hash Chain Implementation

- Genesis hash: All zeros (64 characters)
- Each stage hash = SHA-256(previous_hash + stage_data_json)
- Validation compares calculated vs stored hashes
- Broken chain detection highlights tampering

### API Endpoints

#### Orders
- `POST /api/orders/create` - Create new order
- `GET /api/orders/all` - Get all orders
- `GET /api/orders/{orderId}` - Get specific order
- `GET /api/orders/{orderId}/validate` - Validate hash chain
- `GET /api/orders/clients` - Get all clients
- `GET /api/orders/funds` - Get all funds

#### Audit
- `GET /api/audit/all` - Get all audit logs
- `GET /api/audit/order/{orderId}` - Get order-specific logs
- `GET /api/audit/stage/{stage}` - Get stage-specific logs

### Web Pages

- `/` - Home dashboard with order creation
- `/orders` - Order management and tracking
- `/audit` - Audit log viewer with filtering
- `/validation` - Hash chain validation and visualization

## Project Structure

```
src/
├── main/
│   ├── java/com/dlt/
│   │   ├── model/          # Data models (Order, Client, Fund, etc.)
│   │   ├── repository/     # MongoDB repositories
│   │   ├── service/        # Business logic services
│   │   ├── controller/     # REST controllers
│   │   ├── util/           # Hash utility class
│   │   └── DltTradeApplication.java
│   └── resources/
│       ├── static/         # CSS, JS files
│       ├── templates/      # HTML templates
│       └── application.properties
└── pom.xml
```

## Key Concepts Demonstrated

### 1. Immutability
- No UPDATE operations on completed orders
- New entries for any changes (append-only)
- Hash chain prevents retroactive modifications

### 2. Transparency
- Complete audit trail of all actions
- Real-time status tracking
- Detailed reporting and analytics

### 3. Trustless Operations
- Mathematical proof via hash chains
- No need to trust other domains
- Cryptographic verification of data integrity

### 4. Industry-Standard Processes
- Realistic suitability checks (risk tolerance, age, investment capacity)
- Multi-level approval workflow
- Compliance and operational validations

## Monitoring and Validation

### System Status
- Total orders processed
- Success/failure rates
- Orders in progress
- Failed order analysis

### Hash Chain Validation
- Individual order validation
- System-wide integrity checks
- Visual hash chain representation
- Tamper detection alerts

## Configuration

### MongoDB Settings
```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=dlt_trade_db
```

### Application Settings
```properties
server.port=8080
logging.level.com.dlt=DEBUG
```

## Development Notes

This is a proof of concept designed to demonstrate DLT principles rather than a production system. Features intentionally simplified:

- No authentication/authorization
- Automatic approval at all stages
- Hardcoded validation rules
- Simple web interface
- Local MongoDB deployment

## Future Enhancements

Potential areas for expansion:
- Manual approval workflows
- Advanced suitability algorithms
- User authentication and roles
- Distributed database deployment
- Advanced cryptographic features
- Integration with external systems
- Performance optimization
- Enhanced security measures

## License

This project is for educational and demonstration purposes.
