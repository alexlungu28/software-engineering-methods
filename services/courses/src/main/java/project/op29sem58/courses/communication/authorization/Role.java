package project.op29sem58.courses.communication.authorization;

public enum Role {
    Admin("Admin"),
    Student("Student"),
    Teacher("Teacher");

    private String name;

    private Role(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
