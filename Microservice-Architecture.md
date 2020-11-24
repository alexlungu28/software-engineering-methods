


|Service|In|Out|
| :- | :- | :- |
|Authentication service|<p>POST /login</p><p>{ password: string, netid: string }</p>|JWT token containing authorities, this JWT is passed in the Bearer header in every following request|
||POST /register<br>{ password: string, netid: string }||
||DELETE /unregister<br>{ netid?: string }||
||POST /modifyUser<br>{ password: string, netid?: string }||
|Courses service|<p>POST /createCourse</p><p>{ name: string, code: string, year: int }</p>|Course created in the database, get JSON back with status|
||POST /modifyCourse<br>{ courseId: int, name?: string, code?: string, year?: int }|Returns the edited course|
||<p>POST /createLecture</p><p>{ name: string, courseId: int, teacherNetId: int, date: string, requiredTimeslots: int}</p>|Lecture created in the database, get HTTP status back and the JSON contains the created lecture|
||GET /getMyCourses|<p>[</p><p>` `{ name: string, code: string, year: int, id: int },</p><p>{ name: string, code: string, year: int, id: int },</p><p>{ name: string, code: string, year: int, id: int },<br>` `etc</p><p>]</p>|
||POST /moveLectureOnline<br>{ lectureId: int }||
||GET /getAllLectures|`[{courseId: int, lectureIds: [int, int, int, ...]}, ...]`|
|Room Schedule service|<p>POST /scheduleLecturesUntil</p><p>{</p><p>` `date: string<br>}</p>|A JSON with all the scheduled lectures to rooms, including the time|
||<p>POST</p><p>/scheduleLecture</p><p>{ lectureId: int }</p>||
||<p>DELETE /cancelLecture</p><p>{ lectureId: int }</p>||
||GET /getSchedule|[ {startTime: string, endTime: string, lectureId: int, roomId: int }, … ]|
|Room service|<p>POST /createRoom</p><p>{ name: string, capacity: int }</p>|Return the created room|
||DELETE /deleteRoom<br>{ roomId: int }||
||POST /modifyRoom<br>{ roomId: int, name?: string, capacity?: int }|Update the room with the given id|
|Student Assignment service|POST /assignStudentsUntil<br>{ date: string }||
||GET /allMyLectures|<p>[</p><p>` `{ lectureId: int,</p><p>courseName: String,</p><p>roomName: String,</p><p>onCampus: boolean</p><p>` `},</p><p>` `....</p><p>]</p>|
||GET /allMyCourses|<p>[</p><p>{ courseName: string,</p><p>`  `yearOfStudy: integer</p><p>}</p><p>]</p>|
||<p>POST /declineOnCampusOffer</p><p>{ lectureId: int }</p>||
||POST /setPreferences<br>{ wantsToGoToCampus: boolean }||