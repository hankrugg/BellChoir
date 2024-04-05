import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * Represents a player that can play musical notes.
 */
public class Player implements Runnable {
    private final Note note; // The musical note to be played
    private final AudioFormat af; // The audio format for the player
    private final Thread thread; // The thread for running the player

    /**
     * Constructs a Player with the given note and audio format.
     * @param note The musical note to be played.
     * @param af The audio format for the player.
     */
    public Player(Note note, AudioFormat af) {
        this.note = note;
        this.af = af;
        this.thread = new Thread(this, note.toString());
        this.thread.start();
    }

    /**
     * Starts playing the note with the given length.
     * @param length The length of the note to be played.
     */
    public synchronized void startPlaying(NoteLength length) {
        try (final SourceDataLine line = AudioSystem.getSourceDataLine(af)) {
            // Open the audio line
            line.open();
            // Start playing the audio
            line.start();

            // Calculate the duration of the note in milliseconds
            final int ms = Math.min(length.timeMs(), Note.MEASURE_LENGTH_SEC * 1000);
            final int noteLength = Note.SAMPLE_RATE * ms / 1000;

            // Write the audio data of the note to the line
            line.write(note.sample(), 0, noteLength);

            // Add a brief rest after the note
            line.write(Note.REST.sample(), 0, 50);

            // Wait for any remaining data to be played before closing the line
            line.drain();

        } catch (LineUnavailableException e) {
            // Print the stack trace if an exception occurs
            e.printStackTrace();
        }
    }


    /**
     * Joins the player thread, blocking until it finishes.
     */
    public void retire() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Runs the player thread to play the note.
     */
    @Override
    public synchronized void run() {
        System.out.println("Thread " + thread.getName() + " is ready to play!");
    }

    /**
     * Returns a string representation of the Player.
     * @return A string representation of the Player.
     */
    @Override
    public String toString() {
        return "Thread " + note;
    }
}
