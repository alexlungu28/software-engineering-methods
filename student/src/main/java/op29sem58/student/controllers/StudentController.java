package op29sem58.student.controllers;


import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import op29sem58.student.AssignUntilOptions;
import op29sem58.student.CourseLectures;
import op29sem58.student.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import op29sem58.student.database.entities.Student;
import op29sem58.student.database.entities.RoomSchedule;
import op29sem58.student.database.entities.StudentEnrollment;
import op29sem58.student.database.repos.StudentBookingRepo;
import op29sem58.student.database.repos.StudentEnrollmentRepo;
import op29sem58.student.database.repos.StudentRepo;

@RestController
public class StudentController {
	@Autowired
	private StudentRepo students;

	@Autowired
	private StudentBookingRepo studentBookings;

	@Autowired
	private StudentEnrollmentRepo studentEnrollments;

	private Map<Integer, Integer> lectureIdToCourseId;

	@PostMapping(path = "/assignStudentsUntil")
	public void assignStudentsUntil(@RequestBody AssignUntilOptions options) {
		// initialize all courses
		this.initializeCourses();

		// get all lectures to assign students to, sorted by their starttime
		final List<Lecture> lectures = this.getAllScheduledSortedLecturesUntil(options.date);

		// get all students, where the highest priority students are at the start
		final List<Student> students = this.students.findAllByOrderByLastVisitedAsc();

		// assign students to lectures
		final List<RoomSchedule> studentSchedule = new ArrayList<>();
		for (final Lecture lecture : lectures) {
			final RoomSchedule roomSchedule = lecture.getRoomSchedule();

			int assignedStudents = 0;
			final int allowedStudents = roomSchedule.getCoronaCapacity();
			for (int i = 0; i < students.size(); i++) {
				final Student student = students.get(i);
				if (this.studentIsEnrolledFor(student, lecture)) {
					roomSchedule.addStudent(student);
					student.setLastVisited(lecture.getRoomSchedule().getStartTime());
					this.students.save(student);
					students.remove(i);
					students.add(student);
					break;
				}
			}
			studentSchedule.add(roomSchedule);
		}

		System.out.println("Assigning successful!");
		// save in database
		this.studentBookings.saveAll(studentSchedule);
		this.studentBookings.flush();
	}

	//for now it is just an example should retrieve the lectures via /getAllLectures endpoint
	private ArrayList<CourseLectures> retrieveLectures() {
		ArrayList<CourseLectures> courseLecturesList = new ArrayList<>();
		int[] exampleArray = new int[]{1, 2};
		int[] exampleArray2 = new int[]{3, 4};
		CourseLectures example1 = new CourseLectures(1, exampleArray);
		CourseLectures example2 = new CourseLectures(2, exampleArray2);
		courseLecturesList.add(example1);
		courseLecturesList.add(example2);

		return courseLecturesList;
	}

	private void initializeCourses() {
		// get all lectures via /getAllLectures endpoint
		List<CourseLectures> courseLecturesList = retrieveLectures();

		// fill map
		this.lectureIdToCourseId = new HashMap<Integer, Integer>();
		for (CourseLectures courseLectures : courseLecturesList) {
			final int courseId = courseLectures.getCourseId();
			for (final int lectureId : courseLectures.getLectureIds()) {
				this.lectureIdToCourseId.put(lectureId, courseId);
			}
		}
	}

	private boolean studentIsEnrolledFor(Student student, Lecture lecture) {
		final int courseId = this.getCourseIdForLecture(lecture);
		final Optional<StudentEnrollment> maybeStudentEnrollment = this.studentEnrollments.findByCourseIdAndStudent(courseId, student);
		return maybeStudentEnrollment.isPresent();
	}

	private int getCourseIdForLecture(Lecture lecture) {
		return this.lectureIdToCourseId.get(lecture.getId());
	}

	private List<Lecture> getAllScheduledSortedLecturesUntil(LocalDateTime date) {
		// get the schedule via getSchedule endpoint
		final List<RoomSchedule> schedule = new ArrayList<>();
		LocalDateTime exampleDate = LocalDateTime.of(2020,
				Month.NOVEMBER, 29, 8, 45, 00);
		LocalDateTime enDate = LocalDateTime.of(2020,
				Month.NOVEMBER, 29, 10, 30, 00);
		RoomSchedule example = new op29sem58.student.database.entities.RoomSchedule(exampleDate, enDate, 0, 1, 1, 100);
		schedule.add(example);

		// sort the lectures by their start time
		schedule.sort((a, b) -> a.getStartTime().compareTo(b.getStartTime()));

		// collect lectures that matter
		return schedule.stream().filter(roomSchedule -> roomSchedule.getStartTime().isBefore(date)).map(roomSchedule -> new Lecture(roomSchedule.getLectureId(), roomSchedule)).collect(Collectors.toList());
	}
}
