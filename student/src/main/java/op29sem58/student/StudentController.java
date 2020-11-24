package op29sem58.student;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import op29sem58.student.database.entities.Student;
import op29sem58.student.database.entities.StudentBooking;
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

	// @GetMapping(path = "/test")
	// public List<Student> test() {
	// 	return this.students.findAllByOrderByLastVisitedAsc();
	// }

	@PostMapping(path = "/assignStudentsUntil")
	public void assignStudentsUntil(@RequestBody AssignUntilOptions options) {
		// initialize all courses
		this.initializeCourses();


		// get all lectures to assign students to, sorted by their starttime
		final List<Lecture> lectures = this.getAllScheduledSortedLecturesUntil(options.date);

		// get all students, where the highest priority students are at the start
		final List<Student> students = this.students.findAllByOrderByLastVisitedAsc();

		// assign students to lectures
		final List<StudentBooking> studentSchedule = new ArrayList<StudentBooking>();
		for (final Lecture lecture : lectures) {
			for (int i = 0; i < students.size(); i++) {
				final Student student = students.get(i);
				if (this.studentIsEnrolledFor(student, lecture)) {
					studentSchedule.add(new StudentBooking(lecture.getRoomSchedule().getId()));
					student.setLastVisited(lecture.getRoomSchedule().getStartTime());
					this.students.save(student);
					students.remove(i);
					students.add(student);
					break;
				}
			}
		}

		// save in database
		this.studentBookings.saveAll(studentSchedule);
		this.studentBookings.flush();
	}

	private void initializeCourses() {
		// get all lectures via /getAllLectures endpoint
		final List<CourseLectures> courseLecturesList = new ArrayList<CourseLectures>();

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
		final List<RoomSchedule> schedule = new ArrayList<RoomSchedule>();

		// sort the lectures by their start time
		schedule.sort((a, b) -> a.getStartTime().compareTo(b.getStartTime()));

		// collect lectures that matter
		return schedule.stream().filter(roomSchedule -> roomSchedule.getStartTime().isBefore(date)).map(roomSchedule -> new Lecture(roomSchedule.getLectureId(), roomSchedule)).collect(Collectors.toList());
	}
}
