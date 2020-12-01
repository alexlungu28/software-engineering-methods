package op29sem58.courses;

public class CourseInfo {
    private int id;
    private String teacherNetId;
    private String name;
    private String code;
    private int yearOfStudy;

    public CourseInfo(){}

    /**
     * Constructor for courseInfo without id.
     *
     * @param teacherNetId the NetId of the teacher
     * @param name the name of the course
     * @param code the code of the course
     * @param yearOfStudy the year in which this course is taught
     */
    public CourseInfo(String teacherNetId, String name, String code, int yearOfStudy) {
        this.teacherNetId = teacherNetId;
        this.name = name;
        this.code = code;
        this.yearOfStudy = yearOfStudy;
    }

    /**
     * Constructor for courseInfo with id.
     *
     * @param id the id of the course
     * @param teacherNetId the NetId of the teacher of this course
     * @param name the name of this course
     * @param code the code of this course
     * @param yearOfStudy the year in which this course is taught
     */
    public CourseInfo(int id, String teacherNetId, String name, String code, int yearOfStudy) {
        this.id = id;
        this.teacherNetId = teacherNetId;
        this.name = name;
        this.code = code;
        this.yearOfStudy = yearOfStudy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacherNetId() {
        return teacherNetId;
    }

    public void setTeacherNetId(String teacherNetId) {
        this.teacherNetId = teacherNetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }
}
