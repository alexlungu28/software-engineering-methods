package roomscheduler.entities;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity // This tells Hibernate to make a table out of this class
@Table(name = "rules")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idrules;
    private String name;
    private String value;


    protected Rule() {

    }

    public Rule(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Integer getIdrules() {
        return idrules;
    }

    public void setIdrules(Integer idrules) {
        this.idrules = idrules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rule rule = (Rule) o;
        return Objects.equals(idrules, rule.idrules) &&
                Objects.equals(name, rule.name) &&
                Objects.equals(value, rule.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idrules, name, value);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "idrules=" + idrules +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}


