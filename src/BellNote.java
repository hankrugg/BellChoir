/**
 * Represents a musical note with a specific length.
 */
class BellNote {
    final Note note; // The musical note
    final NoteLength length; // The length of the note

    /**
     * Constructs a BellNote with the given musical note and length.
     * @param note The musical note.
     * @param length The length of the note.
     */
    BellNote(Note note, NoteLength length) {
        this.note = note;
        this.length = length;
    }

    /**
     * Returns a string representation of the BellNote.
     * @return A string representation of the BellNote.
     */
    @Override
    public String toString(){
        return note + " " + length;
    }
}
