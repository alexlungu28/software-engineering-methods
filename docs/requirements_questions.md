# Requirement Questions

1. Should the grace period after a lecture be
    1. global for all rooms?
    2. a configuration setting per room?
    3. be determined by some formula depending on the capacity?
    - Currently it is set to 45 minutes. It's up to you how you will implement it. Do keep in mind that clients usually expect such things to be "easy to change"
2. Is there any other information a room should have, other than the room name and (building) address?
    - Capacity maybe? You can come up with more details, if you think they will be needed for the system
3. Do lecturers create, edit and delete their courses themselves?
    - Yes. You can also have an "admin" user to add those, it's up to you.
4. Do courses have a start and end date determining when all lectures should have been given? And if so, should the lectures be evenly divided across the weeks? Do the lecturers, when creating a course, specify how many lectures per week, or how many lectures in total?
    - You can assume that teachers will be inputing lectures manually, specifying at what day the lecture needs to be. You need to find a possible time slot
5. What information should be included for each lecture?
    - The general answer for these questions is "whatever you find necessary for the platform". You can keep it simple, i don't think there is a lot of information to be stored. Maybe how many students will attend it on campus?
6. What defines the duration of a lecture?
    - In TU Delft, a lecture time slot is 45 minutes, with a 15 minutes break (8:45 is the first lecture). Most lectures occupy more than one slot.
7. Can students only be added/removed by the admin, or can students themselves register/unregister?
    - Only by the admin. Alternately, they can be seeded directly in the database
8. Something was mentioned about what year a student is in, what do we need this information for?
    - I am not sure, where was this mentioned?
9. Do students specify that they don't want to attend on-campus lectures specify that for each course individually, or overall (, or either)?
    - Best if it's per lecture
