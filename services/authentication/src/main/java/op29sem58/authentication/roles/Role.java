package op29sem58.authentication.roles;

import java.util.Locale;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    private int id;
    private String name;

    public Role() {
    }

    /**
     * Constructor.
     *
     * @param id role id
     * @param name role name
     */
    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** Converts the id to a name that is used for authentication.
     *
     * @return the name of the role
     */
    public String getName() {
        return "ROLE_" + name.toUpperCase(Locale.US);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        Role role = (Role) o;
        return id == role.id
                && name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + id
                + ", name='" + name + '\''
                + '}';
    }
}
