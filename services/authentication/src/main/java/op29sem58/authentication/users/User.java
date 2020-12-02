package op29sem58.authentication.users;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import op29sem58.authentication.roles.Role;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "net_id")
    private String netID;
    private String password;
    @ManyToOne
    @JoinColumn(name = "roles_id", referencedColumnName = "id")
    private Role role;

    public User() {
    }

    /**
     * Constructor for logging in.
     *
     * @param netID user netid
     * @param password user password
     */
    public User(String netID, String password) {
        this.netID = netID;
        this.password = password;
    }

    /**
     * Constructor for adding/modifying a user.
     *
     * @param netID user netid
     * @param password user password
     * @param role user role
     */
    public User(String netID, String password, Role role) {
        this.netID = netID;
        this.password = password;
        this.role = role;
    }

    public String getNetID() {
        return netID;
    }

    public void setNetID(String netID) {
        this.netID = netID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(netID, user.netID)
                && Objects.equals(password, user.password)
                && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(netID, password, role);
    }

    @Override
    public String toString() {
        return "User{"
                + "netID='" + netID + '\''
                + ", password='" + password + '\''
                + ", role=" + role
                + '}';
    }
}
