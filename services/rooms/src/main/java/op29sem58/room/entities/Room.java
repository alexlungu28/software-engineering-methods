package op29sem58.room.entities;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity // This tells Hibernate to make a table out of this class
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer capacity;

    /**
     * Creates a new room with name and number of capacity.
     *
     * @param name The name of a new room
     * @param capacity The capcacity of a room.
     */

    public Room(String name, Integer capacity) {
        int one = 1;
        if (capacity < one) {
            throw new IllegalArgumentException("invalid capacity");
        }
        this.name = name;
        this.capacity = capacity;
    }

    protected Room() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    /**
     * Sets a new capacity of a room.
     *
     * @param capacity The nwe number of people that can fit in a new room.
     */

    public void setCapacity(Integer capacity) {
        int one = 1;
        if (capacity < one) {
            throw new IllegalArgumentException("invalid capacity");
        }
        this.capacity = capacity;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(id, room.id)
                && Objects.equals(name, room.name)
                && Objects.equals(capacity, room.capacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, capacity);
    }
}

