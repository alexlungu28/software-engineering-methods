package op29sem58.student.database.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "Student")
public class Student {

	@Id
	@Column(name = "net_id", nullable = false, length = 64)
    private String netID;

    private LocalDateTime lastVisited;
    private boolean wantsToGo;

	public String getNetID() {
		return this.netID;
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
