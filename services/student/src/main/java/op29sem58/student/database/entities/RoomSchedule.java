package op29sem58.student.database.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

/**
 * This is a RoomSchedule entity, the interesting part is that we store only the part
 * relevant to our service in the database. So the ID is the RoomSchedule ID we receive
 * from the roomScheduler, we link this with the students in a many to many relationship 
 * and store that relationship in the database. The other information like lectureId, 
 * roomId, startTime endTime and coronaCapacity is used while assigning the students, but
 * discarded afterwards, as we do not want to store, values across multiple databases.
 */
@Entity(name = "RoomSchedule")
public class RoomSchedule {
    @Id
    @Column(unique = true, nullable = false)
    private int id;

    @ManyToMany()
    private Set<Student> students;

    private int lectureId;
    @Transient
    private int roomId;
    @Transient
    private LocalDateTime startTime;
    @Transient
    private LocalDateTime endTime;
    @Transient
    private int coronaCapacity;

    /**
     * Creates a new instance of a room schedule.
     *
     * @param id Id of the roomschedule
     * @param lectureId Id of the lecture that was scheduled
     * @param roomId Id of the room the lecture was scheduled in
     * @param startTime Start time of the lecture
     * @param endTime End time of the lecture
     * @param coronaCapacity Number of students that are allowed in the room
     */
    public RoomSchedule(
        int id,
        int lectureId,
        int roomId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        int coronaCapacity
    ) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.id = id;
        this.lectureId = lectureId;
        this.roomId = roomId;
        this.coronaCapacity = coronaCapacity;
    }

    public RoomSchedule() {
        this.students = new HashSet<Student>();
    }

    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getCoronaCapacity() {
        return this.coronaCapacity;
    }
    
    public void setCoronaCapacity(int coronaCapacity) {
        this.coronaCapacity = coronaCapacity;
    }

    public Set<Student> getStudents() {
        return this.students;
    }
    
    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getLectureId() {
        return this.lectureId;
    }
    
    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    public int getRoomId() {
        return this.roomId;
    }
    
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
