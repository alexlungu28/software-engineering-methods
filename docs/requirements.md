# Application Requirements

## Functional Requirements

### Must Haves

1. The system shall schedule lectures before the quarter starts
2. The system shall assign students for the coming two weeks for lectures on campus, prioritizing students that haven't gone to campus the longest
    1. The date a student last went to campus is stored in the Students table
3. Rooms shall only be able to use a percentage of their original capacity (#seats)
    1. For rooms with at most 200 seats, this percentage is 20%
    2. For rooms with 200 seats or more, this percentage is 30%
4. Rooms shall have a grace period
5. Rooms shall be stored in a database with the following information:
    1. Id
    2. location
    3. #seats
6. Teachers shall be able to create their courses before the quarter starts
7. Teachers shall be able to create lectures for their courses before the quarter starts
    1. Lectures shall be given on the day of the week that the teacher specifies
    2. Lectures shall take as many timeslots as the teacher specifies
8. Lectures from courses from the same year shall not overlap
9. Lectures shall be scheduled by the system depending on
    1. The day of the week
    2. The number of timeslots the lecture occupies
    3. How many students will be attending the lecture on campus
10. Users can authenticate themselves (using their netID)
11. Students shall have at least one lecture per two weeks on campus

### Should Haves

1. Students shall be able to globally specify whether they prefer online or on-campus lectures
2. Teachers shall be able to inform that they have symptoms
    1. The lecture will be held online, the on-campus lecture will be canceled
3. Students shall be able to specify per lecture whether they want to attend the lecture on campus or not
4. Students shall be able to accept or decline an invitation for an on-campus lecture
5. The system shall have a configuration table in the database containing:
    1. Timeslot duration
    2. Percentage of students allowed for the total room capacity
6. The scheduler shall prevent overlap of courses from the same bachelor

### Could Haves

1. Users shall see how many students are attending a certain lecture, and how many of them are coming to campus
2. Students shall get notified about upcoming lectures
    1. If they were scheduled for an on-campus lecture, they can decline
3. Canceled lectures (for example due to the teacher having symptoms) shall be disregarded from the students' on-campus lectures
4. Admins shall be able to create, edit and delete rooms
5. Admins shall be able to create, edit and delete courses
6. Admins shall be able to create, edit and delete students
7. Teachers shall be able to edit their courses

## Non-functional Requirements

1. The microservice shall be implemented in Java 14
2. The microservice shall have a backend only
3. The microservice shall consist of a REST API via Spring Boot Server
4. The authentication shall be done using Spring Security
5. The PostgreSQL database shall be used
6. Code quality shall be ensured by having at least 80% of branch-coverage using Jacoco
7. Gradle will be used for CI/CD as well as managing the dependencies and building the project
8. Checkstyle will be used to ensure code style
9. GitLab will be used for CI/CD and version management
10. Postgres JDBC driver for connecting the database
