# Sketch for Microservices Architecture with Spring Cloud and HAL

This project contains five services:
- Registry is Eureka service registry (https://github.com/Netflix/eureka)
- UI-proxy is Zuul routing service https://github.com/Netflix/zuul
  - This is facade for UI communication
- Parking and User are REST entity services implemented with Spring Data Rest (https://projects.spring.io/spring-data-rest)
  - For sketch purpose db is in-memory and populated with one test entity
- Reservation is a task service with exported REST storage for reservations
  - Provides endpoint for new reservation: PUT /reservation/user/{name}/parking/{number} </br>
  Correct response is status 201 Created with Location Header
  - With Spring profile "amqp" enabled connects with RabbitMQ
    on localhost:5672 and creates Fanout exchange "parking.reservations" (https://spring.io/guides/gs/messaging-rabbitmq) </br>
    After successful reservation sends message with created reservation
 
 # How to start
 All projects contains Maven Wrapper (https://github.com/takari/maven-wrapper) and could be started with 
 ```
 $ ./mvnw spring-boot:run
 ```
 - UI-proxy starts on localhost:8000 </br>
 - Registry on localhost:8761 </br>
 - Parking, User and Reservation services on localhost:0 (random ports) </br>
 
 To enable AMQP on Resevation service run it with 
 ```
 $ ./mvnw spring-boot:run -Dspring.profiles.active=amqp
 ````
 
 After all services are started and discovery cache is updated you can create reservation
 ```
 $ curl -X PUT localhost:8000/reservation/user/user/parking/12345
 ```
 # Next possible steps
 - Forbid POST/PUT/PATCH for Reservation
 - Add authorization service and secure POST/PUT/PATCH for User and Parking
 - Add configuration service
