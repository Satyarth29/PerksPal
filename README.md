# **PerksPal**

PerksPal is a rewards program application built using Java 17 and Spring Boot. This project aims to create a RESTful application with endpoints to manage customers, transactions, and rewards. The application's database will store reward information. Additionally, the system will be preloaded with sample data.

### Features

Award points are based on purchase amounts.

Calculate rewards for each customer per month and total.

RESTful endpoints for creating and managing customers and transactions.

Global exception handling for robust error management.

### Technologies Used

Java 17

Spring Boot

Spring Data JPA

MySQL

JUnit 5

Mockito

### Getting Started

#### Prerequisites

Java 17

Maven

MySQL

#### #Installation

**Clone the repository:**

**bash**
git clone https://github.com/Satyarth29/PerksPal.git
cd PerksPal
Configure the MySQL database:

Create a database named "perkspal".

Update the 
src/main/resources/application.properties file with your MySQL credentials:

**properties**
* spring.datasource.url=jdbc:mysql://localhost:3306/perkspal
* spring.datasource.username=your_mysql_username
* spring.datasource.password=your_mysql_password
* spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
* spring.jpa.hibernate.ddl-auto=update
* spring.jpa.database-platform=org.hibernate.dialect.MySQL5Dialect

#### Build and run the application:

**bash**
mvn spring-boot:run

### Usage

#### Endpoints
Create Transaction

URL: _**/api/transaction/create**_

Method: POST

Request Body:

json
{
    "name": "John Doe",
    "price": 520.0,
    "date": "2023-11-08"
}

#### Calculate Rewards 

**_per month_**

URL: _/api/rewards/calculate/month/{customerID}_

Method: **GET**
**_all_**
URL: _/api/rewards/calculate/all/{customerID}_

Method: **GET**

#### Testing:

Framework
Mockito and Junit 5
Run the tests using Maven:

**bash**
mvn test

### Project Structure

**Controller:** Handles HTTP requests and responses.

**Service:** Contains business logic.

**Repository:** Manages database interactions.

Model: Represents the data structure.

**DTO:** Data Transfer Objects for transferring data between layers.

**Exception Handling:** Global exception handler for managing errors.

### Contributing

Contributions are welcome! Please fork the repository and create a pull request with your change
