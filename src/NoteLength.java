/**
 * Enum representing different lengths of musical notes.
 */
enum NoteLength {
    /** Represents a whole note. */
    WHOLE(1.0f),
    /** Represents a half note. */
    HALF(0.5f),
    /** Represents a quarter note. */
    QUARTER(0.25f),
    /** Represents an eighth note. */
    EIGTH(0.125f);

    private final int timeMs; // The time duration of the note in milliseconds

    /**
     * Constructs a NoteLength with the given length and calculates its time duration in milliseconds.
     * @param length The length of the note as a fraction of a measure.
     */
    private NoteLength(float length) {
        timeMs = (int)(length * Note.MEASURE_LENGTH_SEC * 1000);
    }

    /**
     * Gets the time duration of the note in milliseconds.
     * @return The time duration of the note in milliseconds.
     */
    public int timeMs() {
        return timeMs;
    }
}
