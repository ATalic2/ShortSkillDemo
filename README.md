# Short Skill Demo
The purpose of this project is to make a short demostration of my Java Spring knowledge using JSP for front-end development and Spring Boot for my back-end. It's a simple webapp where you are supposed to manage clients and merchants. Each merchant is supposed to handle any number of clients, while only one merchant can be assigned to a client at a time. In the webapp, you can view, create, delete, edit these clients and merchants. 

Client and merchants are stored in a HSQL in-memory database so it is easier to test. During startup, some clients and merchants are inserted into the database already so the webapp can be used from the get-go. The data is handled with Hibernate6. 

There is also an API stored for Postman where you can try to run one request after the webapp has been started and you have a session cookie ready to use. 

Before the webapp is started, please run all the test cases and ensure that all of the are completed and ensure that port 8081 is free (or change in the application.properties another port).
Once everything is up and running, you can access the webapp in the browser: [http://localhost:8081/demo/](http://localhost:8081/demo/)
