package op29sem58.student.database.entities;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "Student")
public class Student {
    @Id
    @Column(name = "net_id", nullable = false, length = 64)
    private String netId;

    private LocalDateTime lastVisited;
    private boolean wantsToGo;

    public String getNetId() {
        return this.netId;
    }

    public LocalDateTime getLastVisited() {
        return this.lastVisited;
    }

    public void setLastVisited(LocalDateTime date) {
        this.lastVisited = date;
    }

    public boolean getWantsToGo() {
        return this.wantsToGo;
    }
}
