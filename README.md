# PocketWise

Student: Emmelance Ucunguyabe  
Student ID: 27617  
Project: PocketWise  
Course: INSY 8322 Web Technology and Internet

# PocketWise

Web Technology and Internet

PocketWise is a small backend for saving and investing. A saver opens a goal, a coach follows it, and an investment can be recorded only after that goal is achieved.

## Run it

PostgreSQL must be running, with a database named `pocketwise`. The host, user, and password are already in `src/main/resources/application.properties`.

```bash
mvn spring-boot:run
```

The API starts at http://localhost:8080. There is no home page. Try:

http://localhost:8080/api/savers/all

For the demo, import `pocket-wise-midterm.postman_collection.json`. Each request is named with its requirement id, such as A2, B1, or D2.

The savers, coaches, goals, and investments used in class are already saved in the database.