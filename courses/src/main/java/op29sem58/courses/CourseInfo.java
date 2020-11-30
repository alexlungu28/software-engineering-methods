package op29sem58.courses;

public class CourseInfo {
    private int id;
    private String name;
    private String code;
    private int yearOfStudy;

    public CourseInfo(String name, String code, int yearOfStudy) {
        this.name = name;
        this.code = code;
        this.yearOfStudy = yearOfStudy;
    }

    public CourseInfo(int id, String name, String code, int yearOfStudy) {
        this.id = id;
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
