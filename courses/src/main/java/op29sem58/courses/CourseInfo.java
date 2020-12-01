package op29sem58.courses;

public class CourseInfo {
    private int id;
    private String teacherNetId;
    private String name;
    private String code;
    private int yearOfStudy;

    public CourseInfo(){}

    public CourseInfo(String teacherNetId, String name, String code, int yearOfStudy) {
        this.teacherNetId = teacherNetId;
        this.name = name;
        this.code = code;
        this.yearOfStudy = yearOfStudy;
    }

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
