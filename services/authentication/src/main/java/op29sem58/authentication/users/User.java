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
    private String netid;
    private String password;
    @ManyToOne
    @JoinColumn(name = "roles_id", referencedColumnName = "id")
    private Role role;

    public User() {
    }

    /**
     * Constructor for logging in.
     *
     * @param netid user netid
     * @param password user password
     */
    public User(String netid, String password) {
        this.netid = netid;
        this.password = password;
    }

    /**
     * Constructor for adding/modifying a user.
     *
     * @param netid user netid
     * @param password user password
     * @param role user role
     */
    public User(String netid, String password, Role role) {
        this.netid = netid;
        this.password = password;
        this.role = role;
    }

    public String getNetid() {
        return netid;
    }

    public void setNetid(String netid) {
        this.netid = netid;
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
        return Objects.equals(netid, user.netid)
                && Objects.equals(password, user.password)
                && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(netid, password, role);
    }

    @Override
    public String toString() {
        return "User{"
                + "netID='" + netid + '\''
                + ", password='" + password + '\''
                + ", role=" + role
                + '}';
    }
}
