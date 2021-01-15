package op29sem58.student.local.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPreference {
    private String studentId;
    private boolean wantsToGo;

    public UserPreference() {}

    /**
     * This it to receive Json to that we can receive student and
     * if he want's to go.
     *
     * @param studentId to find the student
     * @param wantsToGo so that we can set his want to go preference
     */
    public UserPreference(String studentId, boolean wantsToGo) {
        this.studentId = studentId;
        this.wantsToGo = wantsToGo;
    }
}
