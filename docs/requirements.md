# Application Requirements

## Functional Requirements

### Must Haves

1. Lecture rooms/halls
    1. admin creates/edits/deletes rooms
    2. each room has
        1. a capacity (#seats)
        2. a minimum period of time required between consecutive lectures (per room or the same for all rooms?)
        3. restrictions (rules) concerning Covid-19 (max. allowed operation capacity - 20% for rooms with at most 200 seats, 30% otherwise)
        4. a room ID, name (or any other information associated with a room?)
2. Courses
    1. Teachers create/edit/delete their courses?
    2. Start/End period?
    3. Each course has a number of lectures
3. Lectures
    1. Teachers create/edit/delete lectures (what information should be included for each lecture)?
    2. Does the teacher include the time slots for each lecture of his/her courses or does the system have to allocate slots for every lecture?
        1. the biggest room should be allocated for lectures of courses with many enrolled students?
    3. number of lectures per week, duration of each lecture, added by the teacher?
4. Students
    1. Who creates/edits/deletes students (sth was mentioned about 500 1st year students, 350 2nd-3rd)?
    2. Enrolled for courses
    3. Year 1/Year 2/Year 3 (why do we need this information?)
    4. Each student decides if they want to participate for on-campus lectures (for each course or overall?)
        1. For those who do want to participate:
            1. Allocate time slot (at least one allocation per two weeks - at least 2 per month) and invite
            2. The student can accept or decline the invitation (leave empty spot in case of refusal)
5. Teachers
    1. each teacher is responsible for a set of courses

### Should Haves

1. TBD

### Could Haves

1. TBD

## Non-functional Requirements

1. The microservice shall be implemented in Java
2. Backend only
3. REST API via Spring Boot Server
