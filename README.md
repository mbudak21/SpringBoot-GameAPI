# Backend Engineering Case Study [![Unit Tests](https://github.com/mbudak21/backend-engineering-case-study/actions/workflows/docker-image.yml/badge.svg)](https://github.com/mbudak21/backend-engineering-case-study/actions/workflows/docker-image.yml)



# Main Structure

**Model Layer:**
Contains entity classes representing the database tables (e.g., User, Country, Tournament, TournamentParticipant, TournamentBracket).

**Repository Layer:**
Interfaces extending JpaRepository for CRUD operations on entities (e.g., UserRepository, TournamentRepository, TournamentParticipantRepository, TournamentBracketRepository).

**Service Layer:**
Contains business logic and transactional methods (e.g., UserService, TournamentService, TournamentBracketService).
Methods in services handle operations like creating users, joining tournaments, updating levels, and retrieving leaderboards, with the accordance of business logic.

**Controller Layer:**
REST controllers to handle HTTP requests and map them to service methods (e.g., UserController, TournamentController).

**DTOs:**
Data Transfer Objects which are used to represent the requested information in a specific, custom way. In this project, the GroupLeaderboardDTO is used to represent usefull information regarding tournaments, without actually creating such an object in the database. 

# Design Choices In The Database
The database implementation utilizes a MySQL Docker container, and uses a mysql-db-dump.sql file to initialize the Schemas. Below is a visual illustration of the database design.

![image](https://github.com/user-attachments/assets/e42d2d63-8761-45fe-8242-1aeac13ed588)
For each of the tables above, there are classes in the model directory which let SpringBoot "Map" Objects defined in the model class to the database. 

**Was a separate country table really necessary?**
Having a separate country table makes sense if there is a need to add more countries in the future. Since the ISO 3166-1 alpha-3 standard is also already implemented, it is easily possible to add pretty much every country of the world.

**What does the `tournament_bracket` class represent?**
The tournament_bracket class is designed to implement a dynamic queuing system for tournaments. Instead of imposing a fixed limit on player slots per tournament, the system generates multiple brackets based on player demand. When a user joins a tournament, they are automatically assigned to an available bracket within that tournament, grouping them with other participants. This approach ensures scalability and flexibility, allowing tournaments to accommodate varying levels of participation, and enabling developers to create flexible contraints. 
