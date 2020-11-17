# Application Requirements

## Functional Requirements

### Must Haves

1. Admins shall be able to create, edit and delete rooms
2. Admins shall be able to create, edit and delete courses
3. Admins shall be able to create, edit and delete students
4. Rooms shall only be able to use a percentage of their original capacity (#seats)
    1. For rooms with at most 200 seats, this percentage is 20%
    2. For rooms with 200 seats or more, this percentage is 30%
5. Rooms shall have a grace period of 45 minutes
6. Teachers shall be able to create, edit and delete their courses
7. Teachers shall be able to create, edit and delete lectures for their courses
    1. Lectures shall be given on the day of the week that the teacher specifies
    2. Lectures shall take as many timeslots as the teacher specifies
8. Teachers shall be able to inform that they have symptoms
    1. The lecture will be held online, the on-campus lecture will be canceled
9. Lectures from courses from the same year shall not overlap
10. Lectures shall be scheduled by the system depending on
    1. The day of the week
    2. The number of timeslots the lecture occupies
    3. How many students will be attending the lecture on campus
11. Users can authenticate themselves (using their netID)
12. Students shall be able to enroll/unenroll for courses
13. Students shall be able to specify per lecture whether they want to attend the lecture on campus or not
14. Students shall have at least one lecture per two weeks on campus
15. Students shall be able to accept or decline an invitation for an on-campus lecture

### Should Haves

1. Students shall be able to globally specify whether they prefer online or on-campus lectures

### Could Haves

1. Users shall see how many students are attending a certain lecture, and how many of them are coming to campus
2. Students shall get notified about upcoming lectures
    1. If they were schedules for an on-campus lecture, they can decline
3. Canceled lectures (for example due to the teacher having symptoms) shall be disregarded from the students' on-campus lectures

## Non-functional Requirements

1. The microservice shall be implemented in Java
2. The microservice shall have a backend only
3. The microservice shall consist of a REST API via Spring Boot Server
4. The authentication shall be done using Spring Security
