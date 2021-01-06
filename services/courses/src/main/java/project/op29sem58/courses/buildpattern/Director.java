package project.op29sem58.courses.buildpattern;

public class Director {

    public static void constructFirstYear(LectureBuilder builder) {
        builder.setMinNoStudents(500);
    }

    public static void constructSecondYear(LectureBuilder builder) {
        builder.setMinNoStudents(350);
    }
}
