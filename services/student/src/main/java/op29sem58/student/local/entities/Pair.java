package op29sem58.student.local.entities;

public class Pair<U, V> {
    /**
     * The first element of this Pair.
     */
    private transient U first;

    /**
     * The second element of this Pair.
     */
    private transient V second;

    /**
     * Constructs a new Pair with the given values.
     *
     * @param first  the first element
     * @param second the second element
     */
    public Pair(U first, V second) {

        this.first = first;
        this.second = second;
    }
}
