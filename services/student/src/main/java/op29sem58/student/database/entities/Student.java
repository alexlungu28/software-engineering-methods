package op29sem58.student.database.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import op29sem58.student.database.repos.StudentEnrollmentRepo;
import op29sem58.student.local.entities.Course;
import op29sem58.student.local.entities.Lecture;

/**
 * This is a Student class, we store the Net_ID, which also is the primary key.
 * We also store the lastVisited date and if the person wants to go.
 */
@Entity(name = "Student")
@Getter
@Setter
public class Student {
    @Id
    @Column(name = "net_id", nullable = false, length = 64)
    private String netId;

    private LocalDateTime lastVisited;
    private boolean wantsToGo;

    public Student() {}

    /**
     * Create a student.
     *
     * @param netId NetId of the student.
     * @param lastVisited When the student visited the campus last.
     * @param wantsToGo Whether they prefer to go to campus.
     */
    public Student(
        String netId, LocalDateTime lastVisited, boolean wantsToGo
    ) {
        this.netId = netId;
        this.lastVisited = lastVisited;
        this.wantsToGo = wantsToGo;
    }

    /**
     * This checks if the student is enrolled for the lecture, by iterating though the
     * list of courseLectures and retrieving the courseID by the use of the lectureID.
     * Then send a request to enrollments with the courseID and student.
     * If student is enrolled a boolean true will be returned.
     *
     * @param lecture a lecture to get the courseID
     * @return a boolean if the student is enrolled.
     */
    public boolean isEnrolledFor(List<Course> courses,
                                 Lecture lecture,
                                 StudentEnrollmentRepo studentEnrollmentRepo) {
        Optional<Course> courseLecture = courses.stream()
                .filter(e -> e.courseHasLecture(lecture.getId()))
                .findFirst();
        if (courseLecture.isEmpty()) {
            return false;
        }
        final List<StudentEnrollment> maybeStudentEnrollment =
                studentEnrollmentRepo.findByCourseIdAndStudent(courseLecture.get()
                        .getCourseId(), this);
        return !maybeStudentEnrollment.isEmpty();
    }
}
