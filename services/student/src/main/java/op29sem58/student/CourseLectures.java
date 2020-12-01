package op29sem58.student;

public class CourseLectures {
	private int courseId;
	private int[] lectureIds;

	public CourseLectures(int i, int[] exampleArray) {
		courseId = i;
		lectureIds = exampleArray;
	}

	public int getCourseId() {
		return this.courseId;
	}

	public int[] getLectureIds() {
		return this.lectureIds;
	}
}
