# **PerksPal**

PerksPal is a rewards program application built using Java 17 and Spring Boot. This project aims to create a RESTful application with endpoints to manage customers, transactions, and rewards. The application's database will store reward information. Additionally, the system will be preloaded with sample data.

### Features

Award points are based on purchase amounts.

Calculate rewards for each customer per month and total.

RESTful's endpoints for creating and managing customers and transactions.

Global exception handling for robust error management.

### Technologies Used

* Java 17
* Spring Boot
* Spring Data JPA
* MySQL
* JUnit 5
* Mockito

### Getting Started

#### Prerequisites

Java 17

Maven

MySQL

#### Installation

**Clone the repository:**

**bash**
```bash
git clone https://github.com/Satyarth29/PerksPal.git cd PerksPal
```

Configure the MySQL database:

Create a database named "perkspal".

Update the 
src/main/resources/application.properties file with your MySQL credentials:

**properties**
```
spring.datasource.url=jdbc:mysql://localhost:3306/perkspal
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=create
spring.jpa.database-platform=org.hibernate.dialect.MySQL5Dialect
```
#### Build and run the application:

**bash**
```bash
mvn spring-boot:run
```

### Usage

#### Endpoints
#### BaseURL: 
```html
http://localhost:8080
```


**_Method1_**: POST
#### Description: Creates Transaction
```
/api/transaction/create
```
Request Body:

```json
{
    "name": "John Doe",
    "price": 520.0,
    "date": "2023-11-08"
}
```
**_Note:_**
no need to create the transaction to test the GET methods at first since some data is preloaded
#### Calculate Rewards 


**_Method2_**: **GET**
#### Description: **gets a customer rewards points based on ID and Start and End Date. Dates can be presented in any format in URL after writing the format in the configuration file if not already present . _**

```
/api/rewards/calculate/range/{customerID}?startDate={startDate}&endDate={endDate}
```


**_Method3_**: **GET**
#### Description: **gets all rewards points for a customer _**
```
/api/rewards/calculate/all/{customerID}
```
**_Note:_**
wiggly brackets {} values must be replaced with actual values
#### Testing:

##### Unit-Testing:

Framework
Mockito and Junit 5
Run the tests using Maven:

**bash**
```bash
mvn test
```
#### Manual-Testing:
Description: 
* can be manual tested using postman by pasting URL as mentioned above 
                          (or)
* by clicking links mentioned below:

[getAllRewards](http://localhost:8080/api/rewards/calculate/all/1)

[getRewards from a range of dates: startDate=15/08/2024&endDate=2024-10-10 ](http://localhost:8080/api/rewards/calculate/range/1?startDate=15/08/2024&endDate=2024-10-10)

**_Note:_**
1. _make sure the DB is set and server is running before clicking the links._
2. _this fetching will be done based on the preloaded data._
3. to see the preloaded data go to config/dataInitializer class



### Project Structure

**Controller:** Handles HTTP requests and responses.

**Service:** Contains business logic.

**Repository:** Manages database interactions.

Model: Represents the data structure.

**DTO:** Data Transfer Objects for transferring data between layers.

**Exception Handling:** Global exception handler for managing errors.

### Contributing

Contributions are welcome! Please fork the repository and create a pull request with your change
