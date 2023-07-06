TDD MINI PROJECT


This project is a Order Management RESTful API built using the Spring framework and Spring Boot. It allows users to perform CRUD operations for Business Orders.

Prereq's: Command line knowhow or at least an IDE to be able to load this project.

How to run and test:

1. Load project into IDE, or direct your CLI to the project's location/root folder.
2. Run the program in the IDE or have the CLI run "mvn clean install" once successful, run "mvn spring-boot:run"
3. Open up your web browser and enter 'http://localhost:8080/orders'
4. Here you will see a blank page with brackets. In order to send appropriate HTTP requests, you'll have to download an external application like Postman or use the Postman chrome extension. Web browsers do not inherently have the ability to send detailed HTTP requests.
5. To run the java classes designed to test the functionality of the API, use your IDE and go to the project directory 'src/test/java/com/example/tddminiproject' and run either the OrderControllerTests file or OrderRepositoryTests file.