package op29sem58.student;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
		final List<RoomSchedule> studentSchedule = new ArrayList<RoomSchedule>();
		for (final Lecture lecture : lectures) {
			final RoomSchedule roomSchedule = new RoomSchedule(new HashSet<Student>(), lecture.getRoomSchedule().getId());
			int assignedStudents = 0;
			final int allowedStudents = this.getAllowedNumberOfStudentsForLecture(lecture);
			for (int i = 0; i < students.size(); i++) {
				final Student student = students.get(i);
				if (this.studentIsEnrolledFor(student, lecture)) {
					roomSchedule.addStudent(student);
					student.setLastVisited(lecture.getRoomSchedule().getStartTime());
					this.students.save(student);
					students.remove(i);
					students.add(student);
					if (++assignedStudents >= allowedStudents) break;
				}
			}
			studentSchedule.add(roomSchedule);
		}

		// save in database
		this.studentBookings.saveAll(studentSchedule);
		this.studentBookings.flush();
	}

	private int getAllowedNumberOfStudentsForLecture(Lecture lecture) {
		return 1;
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
		final List<op29sem58.student.RoomSchedule> schedule = new ArrayList<op29sem58.student.RoomSchedule>();

		// sort the lectures by their start time
		schedule.sort((a, b) -> a.getStartTime().compareTo(b.getStartTime()));

		// collect lectures that matter
		return schedule.stream().filter(roomSchedule -> roomSchedule.getStartTime().isBefore(date)).map(roomSchedule -> new Lecture(roomSchedule.getLectureId(), roomSchedule)).collect(Collectors.toList());
	}
}
