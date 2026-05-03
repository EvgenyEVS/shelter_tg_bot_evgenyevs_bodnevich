<img width="302" height="86" alt="image" src="https://github.com/user-attachments/assets/0c2b875c-b08e-4801-bd82-0082e7a42fb8" />

**An app for an animal shelter in Astana.**

Course project by Evgeny Sinelnikov and Kristina Bodnevich  
**Team:** Evs_Bodnevich

The backend portion of the app has been implemented. Dual access is provided:
- via a Telegram bot for users
- via REST API for administration (Swagger UI)

A single database is used for different pet types (cats, dogs). The logic is built on dividing pets by type.

**Programming language and environment:**
- Java 17
- Spring (Boot, MVC, Data, Cache)
- PostgreSQL
- Liquibase
- Caffeine cache

**Tests:** Mockito, SpringBootTest.

**For starting:**
1. Configure `TELEGRAM_BOT_TOKEN` in `application.properties` or as an environment variable
2. Configure PostgreSQL database connection
3. Open the project and start the application
4. Use Telegram as a user
5. Use `http://localhost:8080/swagger-ui.html` as administrator

**Additional:**
You can add other types of pets by extending the `PetType` enum.
