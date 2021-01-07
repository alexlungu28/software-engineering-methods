package project.op29sem58.courses.buildpattern;

public class Director {

    public static void constructFirstYear(Builder builder) {
        builder.setMinNoStudents(500);
    }

    public static void constructSecondYear(Builder builder) {
        builder.setMinNoStudents(350);
    }
}
